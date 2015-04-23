package rainbowworks.rainbowlists;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimitar on 7.4.2015 г..
 */
public class DatabaseHandler{
    private SQLiteDatabase db;

    /**
     * Constructor for our database handler
     * @param activity parsed to keep everything in the same activity
     * (we are simply not experienced with multiple activities and using intents)
     */
    protected DatabaseHandler(MainActivity activity) {
        db = activity.openOrCreateDatabase("rainbowData", activity.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS items (ID INTEGER PRIMARY KEY AUTOINCREMENT, listID INTEGER, name CHAR(32), amount CHAR(32), isChecked INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS lists (ID INTEGER PRIMARY KEY AUTOINCREMENT, name CHAR(32), type CHAR(8));");
    }

    /**
     * Reset all data, including data structures
     */
    protected void reset() {
        db.execSQL("DROP TABLE items");
        db.execSQL("DROP TABLE lists");
        db.execSQL("CREATE TABLE IF NOT EXISTS items (ID INTEGER PRIMARY KEY AUTOINCREMENT, listID INTEGER, name CHAR(32), amount CHAR(32), isChecked INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS lists (ID INTEGER PRIMARY KEY AUTOINCREMENT, name CHAR(32), type CHAR(8));");
    }

    /**
     * Run whatever sql code is parsed, used for saving, updating and deleting
     * @param sql is the given sql code
     */
    protected void save(String sql) {
        db.execSQL(sql);
    }

    /**
     * Run whatever sql code is parsed, is only used for reading and returns data
     * @param sql is the given sql code
     * @return List<List<String>> with every row and column from the sql result
     */
    protected List<List<String>> load(String sql) {
        Cursor c=db.rawQuery(sql, null);
        //2-dim List. getCount() returns ROWS COUNT
        List<List<String>> stringArray = new ArrayList<List<String>>(c.getCount());

        for (int i=0; c.moveToNext(); i++){
            stringArray.add(new ArrayList<String>(c.getColumnCount()));
            for (int k = 0; k < c.getColumnCount(); k++) {
                stringArray.get(i).add(c.getString(k));
            }
        }
        //release all of its resources and make it invalid
        c.close();
        return stringArray;
    }
}