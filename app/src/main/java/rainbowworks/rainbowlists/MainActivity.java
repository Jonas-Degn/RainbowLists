package rainbowworks.rainbowlists;

import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.support.v7.widget.SearchView;
import android.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;

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

    private SimpleCursorAdapter mAdapter;
    int[] searchIDs;
    String[] searchNames;
    String[] searchTypes;


    /*
     * Create application
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);

        dbh = new DatabaseHandler(this);
        //dbh.reset();
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

    /*
     * Content of options menu
     * Changes per webview page
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        register = menu.findItem(R.id.action_search);
        register.setVisible(currentPage != null);

        register = menu.findItem(R.id.action_settings);
        register.setVisible(currentPage != null);

        register = menu.findItem(R.id.action_quit);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_search:

                break;
            case R.id.action_settings:
                JavaInterface.runJS("loadPage('settings.html')");
                break;
            case R.id.action_quit:
                this.finish();
                System.exit(0);
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
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
}