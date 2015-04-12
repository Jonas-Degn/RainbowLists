package rainbowworks.rainbowlists;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by Dimitar on 12.4.2015 г..
 * SearchableActivity extends ListView as that is how it is done traditionally.
 */
public class SearchableActivity extends ListView {
    Intent intent;

    public SearchableActivity(Context context) {
        super(context);
    }

    public String getQuery() {

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        String query = null;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            /*
            *   or we could pass the query to another method to do sth with it
            *   doMySearch(query);
            */
        }
        return query;
    }

    public Intent getIntent() {
        return intent;
    }
}
