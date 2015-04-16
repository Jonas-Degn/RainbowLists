package rainbowworks.rainbowlists;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Dimitar on 16.4.2015 г..
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {

    public DrawerItemClickListener() {
        Log.i("constructor", "test if works");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("onItemClick method", "test if works");
        selectItem(position);
    }

    public void selectItem(int position) {

        Log.i("Drawer", Integer.toString(position));
        switch (position) {
            case 0:
                JavaInterface.runJS("loadPage('settings.html')");
                break;
            case 1:
                JavaInterface.runJS("loadPage('settings.html')");
                break;
            case 2:
                JavaInterface.runJS("loadPage('settings.html')");
                break;
            default:
                break;
        }
    }
}