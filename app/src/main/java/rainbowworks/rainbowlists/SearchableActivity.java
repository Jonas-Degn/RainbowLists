package rainbowworks.rainbowlists;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Dimitar on 12.4.2015 г..
 * SearchableActivity extends ListView as that is how it is done traditionally.
 */
public class SearchableActivity extends ListView {
    Intent intent;
    MainActivity activity;

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

    public Intent getIntent() {
        return intent;
    }
}
