package rainbowworks.rainbowlists;

import android.content.res.Configuration;
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
import android.webkit.WebView;
import android.support.v7.widget.SearchView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    private MenuItem register;
    private String currentPage;
    private int currentList;
    private String currentAction;
    private JavaInterface jsInterface;
    private DatabaseHandler dbh;
    private HashMap<Integer, RainbowList> lists;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private SimpleCursorAdapter mAdapter;
    int[] searchIDs;
    String[] searchNames;
    String[] searchTypes;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    /*
     * Create application
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);

        mNavigationDrawerItemTitles= new String[]{"Create", "Reload", "Share"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[3];

        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_action_copy, "Create");
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_action_refresh, "Reload");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_action_share, "Share");

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item1));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                null,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {

                getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {

                getActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dbh = new DatabaseHandler(this);
        dbh.reset();
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
    protected void onSaveInstanceState(Bundle restoreInstanceState)
    {
        super.onSaveInstanceState(restoreInstanceState);
        JavaInterface.webView.saveState(restoreInstanceState);

    }

    /*
     * Restore state of webview
     * Used for mechanics that may try to reset webview
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
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
        register.setVisible(currentPage != null);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
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
                int id = searchIDs[position];
                String name = searchNames[position];
                String type = searchTypes[position];
                Log.i("Search","You selected ID "+id+" named "+name+" of type "+type);

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

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onBackPressed() {
        if (!currentPage.equals("listOverview")) {
            JavaInterface.runJS("loadPage('listOverview.html')");
            return;
        }
        if (currentAction.equals("showNewList")) {
            JavaInterface.runJS("$(\".bottom_piece\").animate({bottom: \"-14em\"},400,function() {\n" +"$(\".newList\").show('fast');\n" +"});\n" +"$(document).unbind(\"tap\");");
            currentAction = "";
            return;
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
        for(List<String> row : newLists) {
            int count = 0;
            int id = 0;
            String name = "empty";
            String type = "none";
            for (String column : row) {
                switch(count) {
                    case 0:
                        id = Integer.parseInt(column);
                        break;
                    case 1:
                        name = column;
                        break;
                    case 2:
                        type = column;
                        break;
                    default:
                        Log.i("Column",column+" is not populated");
                }
                count ++;
            }

            lists.put(new Integer(id),new RainbowList(id,name,type));
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
        getActionBar().setTitle(mTitle);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}