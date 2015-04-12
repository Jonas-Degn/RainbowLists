package rainbowworks.rainbowlists;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;

/**
 * Created by Dimitar on 12.4.2015 г..
 * SearchableActivity extends ListView as that is how it is done traditionally.
 */
public class SearchableActivity extends ListView {
    Intent intent;
    MainActivity activity;
    private MenuInflater menuInflater;
    private ComponentName componentName;

    public SearchableActivity(Context context, MainActivity activity) {
        super(context);
        this.activity = activity;
    }

    public List<List<String>> getQuery() {

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        String query = null;
        List<List<String>> result = null;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            result = activity.getDBH().load("SELECT * FROM lists WHERE name LIKE '%"+query+"%'");
        }
        return result;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    private SearchManager getSystemService(String searchService) {
        SearchManager searchManager = null;
        return searchManager;
    }

    public Intent getIntent() {
        return intent;
    }

    public MenuInflater getMenuInflater() {
        return menuInflater;
    }

    public ComponentName getComponentName() {
        return componentName;
    }
}