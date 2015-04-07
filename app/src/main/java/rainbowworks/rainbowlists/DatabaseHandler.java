package rainbowworks.rainbowlists;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import static android.content.Context.*;

/**
 * Created by Dimitar on 7.4.2015 г..
 */
public class DatabaseHandler extends Activity{

    private SQLiteDatabase db;

    protected DatabaseHandler(String name, MainActivity activity) {
        db = SQLiteDatabase.openOrCreateDatabase(name, activity.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS");
    }

    protected static void connect() {

    }

    protected static void save(String sql) {

    }

    protected static String[] load(String sql) {

    }
}
