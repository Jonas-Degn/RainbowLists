package rainbowworks.rainbowlists;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.support.v7.widget.SearchView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    // used to store app title
    private CharSequence mTitle;

    // Navigation drawer items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    /*
     * Create application
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

    /*
     * For when the page is changed directly in frontend
     */
    protected void setLocation(String file) {
        currentPage = file;
        invalidateOptionsMenu();
    }

    protected String getLocation() {
        return currentPage;
    }

    /*
     * Save state of webview
     * Used for mechanics that may try to reset webview
     */
    @Override
    protected void onSaveInstanceState(Bundle restoreInstanceState){
        super.onSaveInstanceState(restoreInstanceState);
        JavaInterface.webView.saveState(restoreInstanceState);

    }

    /*
     * Restore state of webview
     * Used for mechanics that may try to reset webview
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        JavaInterface.webView.restoreState(savedInstanceState);
    }

    /*
     * Create options menu from XML
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /*
     * Content of options menu
     * Changes per webview page
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        register = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setIconifiedByDefault(false);

        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                int id = searchIDs[position];
                String name = searchNames[position];
                String type = searchTypes[position];
                setCurrentList(id);
                JavaInterface.runJS("loadPage('itemOverview.html');");
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
        return true;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

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

    protected DatabaseHandler getDBH() {
        return dbh;
    }

    protected RainbowList getList(int listID) {
        return lists.get(listID);
    }

    protected HashMap<Integer, RainbowList> getLists() {
        return lists;
    }

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

    protected void setCurrentList(int id) {
        currentList = id;
    }

    protected int getCurrentList() {
        return currentList;
    }

    protected void setCurrentAction(String action) {
        currentAction = action;
    }

    protected String getCurrentAction() {
        return currentAction;
    }

    // You must implement this
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "listName", "listType" });
        List<List<String>> search = dbh.load("SELECT * FROM lists WHERE name LIKE '%"+query+"%'");
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
        mAdapter.changeCursor(c);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            dbh.save("UPDATE items SET name='"+scanResult.getContents().toString()+"' WHERE name='emptyScan'");
            populateLists();
            JavaInterface.runJS("loadItems()");
        }
    }
}