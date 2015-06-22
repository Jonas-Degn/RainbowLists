package rainbowworks.rainbowlists;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends ActionBarActivity {
    private MenuItem register;
    private String currentPage;
    private int currentList;
    private String currentAction = "";
    private JavaInterface jsInterface;
    private DatabaseHandler dbh;
    private HashMap<Integer, RainbowList> lists;
    private SimpleCursorAdapter mAdapter;
    int[] searchIDs;
    String[] searchNames;
    String[] searchTypes;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // Navigation drawer items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    /**
     * Constructor for our entire application - the main activity
     * @param savedInstanceState used in some lifecycles, resuming stuff
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //make fade color transparent, ergo NO fading color
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<>();

        // adding nav drawer items to array
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems, this);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.app_name,R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                invalidateOptionsMenu();
            }
        };

        dbh = new DatabaseHandler(this);
        populateLists();

        jsInterface = new JavaInterface(this,(WebView)findViewById(R.id.mainWebView));
        if (savedInstanceState == null) {
            JavaInterface.webView.loadUrl("file:///android_asset/www/index.html");
        }
        currentPage = "index";

        final String[] from = new String[] {"listName", "listType"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    /**
     * Saves the name of the file we changed the browser to show
     * @param file is the full filename - abcd.html
     */
    protected void setLocation(String file) {
        currentPage = file;
        invalidateOptionsMenu();
    }

    /**
     * Get the name of the currently shown file
     * @return String name of file
     */
    protected String getLocation() {
        return currentPage;
    }

    /**
     * Save our webview when android tells the application to save - Used for mechanics that may try to reset our webview
     * @param restoreInstanceState the instance to save
     */
    @Override
    protected void onSaveInstanceState(Bundle restoreInstanceState){
        JavaInterface.webView.saveState(restoreInstanceState);
        super.onSaveInstanceState(restoreInstanceState);
    }

    /**
     * Restore the state of our webview - used for mechanics that may try to reset our webview
     * @param savedInstanceState the instance to restore
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        JavaInterface.webView.restoreState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Synchronizes the burger and drawer state
     * @param savedInstanceState the instance to restore
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * Create our options menu from XML, only search is left
     * @param menu the menu to create
     * @return boolean whether the creation went okay
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Contains alle the code for preparing our menu, which now only consists of our searching
     * @param menu the menu to prepare
     * @return boolean whether the creation went okay
     */
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        register = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setIconified(true);

        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                if (searchIDs[position] == -1) return false;

                if (getLocation().equals("listOverview")) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    int id = searchIDs[position];
                    setCurrentList(id);
                    JavaInterface.runJS("loadPage('itemOverview.html');");
                } else if (getLocation().equals("itemOverview")) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    int id = searchIDs[position];
                    String name = searchNames[position];
                    JavaInterface.checkItem(Integer.toString(id));
                    JavaInterface.runJS("loadPage('itemOverview.html')");
                    JavaInterface.messageToast(name + " has been checked!");
                }
                register.collapseActionView();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                populateAdapter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }
        });

       searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    register.collapseActionView();
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                }
            }
        });
        return true;

    }

    /**
     * Handle whenever the navigation drawer or search is clicked (the option items)
     * @param item whatever item is clicked
     * @return  boolean whether the selection went okay
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Used to get the navigation drawer layout so we can control it outside of main activity
     * @return DrawerLayout
     */
    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    /**
     * Handles whenever the back button is pressed on the phone.
     */
    @Override
    public void onBackPressed() {
        if (!currentPage.equals("listOverview")) {
            JavaInterface.runJS("loadPage('listOverview.html')");
            return;
        }
        else if (currentAction.equals("showNewList")) {
            JavaInterface.runJS("$(\".bottom_piece\").animate({bottom: \"-14em\"},400,function() {\n" +"$(\".newList\").show('fast');\n" +"});\n" +"$(document).unbind(\"tap\");");
            currentAction = "";
            return;
        }
        else if (currentPage.equals("listOverview")) {
            this.finish();
            System.exit(0);
        }
        super.onBackPressed();
    }

    /**
     * Get the database handler object
     * @return DatabaseHandler
     */
    protected DatabaseHandler getDBH() {
        return dbh;
    }

    /**
     * Get a given list
     * @param listID of the list to be returned
     * @return RainbowList
     */
    protected RainbowList getList(int listID) {
        return lists.get(listID);
    }

    /**
     * Get every list found
     * @return HashMap<Integer, RainbowList> containing ID and object of lists
     */
    protected HashMap<Integer, RainbowList> getLists() {
        return lists;
    }

    /**
     * Fill up the empty lists and their items with information from the database
     */
    protected void populateLists() {
        lists = new HashMap<Integer, RainbowList>();
        List<List<String>> newLists = dbh.load("SELECT * FROM lists");
        List<List<String>> newItems = dbh.load("SELECT * FROM items");

        lists.put(Integer.valueOf(0),new RainbowList(0,"empty",""));
        lists.get(Integer.valueOf(0)).addItem(0,"empty","0",false);
        for(List<String> row : newLists) {
            int count = 0;
            int id = 0;
            String name = "empty";
            String type = "none";
            for (String column : row) {
                switch(count) {
                    case 0: id = Integer.parseInt(column); break;
                    case 1: name = column; break;
                    case 2: type = column; break;
                    default: Log.i("Column",column+" is not populated");
                }
                count ++;
            }
            lists.put(Integer.valueOf(id),new RainbowList(id,name,type));
            lists.get(id).addItem(0,"empty","0",false);
        }
        for(List<String> row2 : newItems) {
            int count = 0;
            int id = 0;
            int listID = 0;
            String name = "empty";
            String amount = "empty";
            boolean isChecked = false;
            for (String column : row2) {
                // (ID INTEGER PRIMARY KEY AUTOINCREMENT, listID INTEGER, name CHAR(32), amount CHAR(32), isChecked INTEGER)
                switch(count) {
                    case 0: id = Integer.parseInt(column); break;
                    case 1: listID = Integer.parseInt(column); break;
                    case 2: name = column; break;
                    case 3: amount = column; break;
                    case 4: isChecked = (Integer.parseInt(column) != 0); break;
                    default: Log.i("Column",column+" is not populated");
                }
                count ++;
            }
            lists.get(listID).addItem(id, name, amount, isChecked);
        }
    }

    /**
     * A list has been selected, save which list it is
     * @param id of the selected list
     */
    protected void setCurrentList(int id) {
        currentList = id;
    }

    /**
     * Get the ID of the currently selected list
     * @return int id of the selected list
     */
    protected int getCurrentList() {
        return currentList;
    }

    /**
     * Set what action the user is doing right now
     * @return String name of the action
     */
    protected void setCurrentAction(String action) {
        currentAction = action;
    }

    /**
     * Get what action the user is doing right now
     * @return String name of the action
     */
    protected String getCurrentAction() {
        return currentAction;
    }

    /**
     * Populate the empty list with search suggestions found whenever the user searches for a list
     * @param query of which the user searches something similar
     */
    private void populateAdapter(String query) {
        if (getLocation().equals("listOverview")) {
            final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "listName", "listType" });
            List<List<String>> search = dbh.load("SELECT * FROM lists WHERE name LIKE '%"+query+"%'");

            if (search.size() > 0) {
                searchIDs = new int[search.size()];
                searchNames = new String[search.size()];
                searchTypes = new String[search.size()];

                for(int i = 0; i < search.size(); i++) {
                    for (int k = 0; k < search.get(i).size(); k++) {
                        if (k == 0) {
                            searchIDs[i] = Integer.parseInt(search.get(i).get(k));
                        }
                        else if (k == 1) {
                            searchNames[i] = search.get(i).get(k);
                        }
                        else if (k == 2) {
                            searchTypes[i] = search.get(i).get(k);
                        }
                    }
                }

                for (int i=0; i<search.size(); i++) {
                    c.addRow(new Object[] {searchIDs[i], searchNames[i], searchTypes[i]});
                }
            }
            else {
                searchIDs = new int[1];
                searchNames = new String[1];
                searchTypes = new String[1];
                searchIDs[0] = -1;
                searchNames[0] = "No list found";
                searchTypes[0] = "none";
                c.addRow(new Object[] {searchIDs[0], searchNames[0], searchTypes[0]});
            }
            mAdapter.changeCursor(c);
        }
        else if(getLocation().equals("itemOverview")) {
            final MatrixCursor ci = new MatrixCursor(new String[]{ BaseColumns._ID, "listName", "listType" });
            List<List<String>> search = dbh.load("SELECT ID, name, amount, isChecked FROM items WHERE name LIKE '%"+query+"%' AND listID = "+getCurrentList());

            if (search.size() > 0) {
                searchIDs = new int[search.size()];
                searchNames = new String[search.size()];
                searchTypes = new String[search.size()];

                for(int i = 0; i < search.size(); i++) {
                    for (int k = 0; k < search.get(i).size(); k++) {
                        if (k == 0) {
                            searchIDs[i] = Integer.parseInt(search.get(i).get(k));
                        }
                        else if (k == 1) {
                            searchNames[i] = search.get(i).get(k);
                        }
                        else if (k == 2) {
                            searchNames[i] = search.get(i).get(k) + searchNames[i];
                        }
                        else if (k == 3) {
                            searchTypes[i] = search.get(i).get(k);
                        }
                    }
                }

                for (int i=0; i<search.size(); i++) {
                    ci.addRow(new Object[] {searchIDs[i], searchNames[i], searchTypes[i]});
                }
            }
            else {
                searchIDs = new int[1];
                searchNames = new String[1];
                searchTypes = new String[1];
                searchIDs[0] = -1;
                searchNames[0] = "No item found";
                searchTypes[0] = "";
                ci.addRow(new Object[] {searchIDs[0], searchNames[0], searchTypes[0]});
            }

            mAdapter.changeCursor(ci);
        }
    }

    /**
     * Set the title of the application
     * @param title to set
     */
    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    /**
     * Add a product when it has been scanned with the barcode scanner
     * @param requestCode is the requested code of the scan action
     * @param resultCode is the result code of the scan action
     * @param intent is the intent that were created when the scan started
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        try {
            dbh.save("UPDATE items SET name='"+scanResult.getContents().toString()+"' WHERE name='emptyScan'");
            populateLists();
            JavaInterface.runJS("loadItems()");
        } catch (NullPointerException e) {
            dbh.save("DELETE FROM items WHERE name='emptyScan'");
        }
    }
}