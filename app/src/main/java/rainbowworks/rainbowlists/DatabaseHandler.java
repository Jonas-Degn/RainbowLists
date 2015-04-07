package rainbowworks.rainbowlists;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import java.util.ArrayList;

/**
 * Created by Dimitar on 7.4.2015 г..
 */
public class DatabaseHandler{

    private SQLiteDatabase db;

    protected DatabaseHandler(MainActivity activity) {
        db = activity.openOrCreateDatabase("rainbowData", activity.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS items (ID int(8) AUTO_INCREMENT, listID int(8), name VARCHAR(32), PRIMARY KEY (ID));");
        db.execSQL("CREATE TABLE IF NOT EXISTS lists (ID int(8) AUTO_INCREMENT, name VARCHAR(32), amount int, PRIMARY KEY (ID));");
    }

    //The sql string contains the command to be passed to execSQL();
    protected void save(String sql) {
        db.execSQL(sql);
    }

    protected ArrayList[][] load(String sql) {
        Cursor c=db.rawQuery(sql, null);
        //2-dim Arraylist. getCount() returns ROWS COUNT
        ArrayList[][] stringArray = new ArrayList[c.getCount()][c.getColumnCount()];

        for (int i=0; c.moveToNext(); i++){
            for (int k = 0; k < c.getColumnCount(); k++) {
                stringArray[i][k].add(c.getString(k));
            }
        }

        return stringArray;
    }
}