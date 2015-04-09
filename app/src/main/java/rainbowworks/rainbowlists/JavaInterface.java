package rainbowworks.rainbowlists;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.List;

public class JavaInterface {
    static MainActivity activity;

    public JavaInterface(MainActivity act) {
        activity = act;
    }

    @android.webkit.JavascriptInterface
    public static String loadSettings() {
        return "No settings atm";
    }

    /* Waiting for List class;
    @android.webkit.JavascriptInterface
    public static String loadLists(String type) {
        String foundLists = "";

        for(HashMap<Integer, List> entry : activity.getLists()) {
            int id = entry.getKey();
            List list = entry.getValue();
            if (list.getType().equals(type)) {
                if (foundLists.equals("")) {
                    foundLists += list.getID()+","+list.getName();
                }
                else {
                    foundLists += ";"+list.getID()+","+list.getName();
                }
            }
        }
        return foundLists;
    }*/

    /* Waiting for Item class;
    @android.webkit.JavascriptInterface
    public static String loadItems(int listID) {
        String foundItems = "";
        List currentList = activity.getList(listID);

        for(HashMap<Integer, Item> entry : currentList.getItems()) {
            int id = entry.getKey();
            Item item = entry.getValue();
                if (foundItems.equals("")) {
                    foundItems += item.getID()+","+item.getName()+","+item.getQuantity()+","+item.getIsChecked();
                }
                else {
                    foundItems += ";"+item.getID()+","+item.getName()+","+item.getQuantity()+","+item.getIsChecked();
                }
            }
        }
        return foundItems;
    }
    */

    @android.webkit.JavascriptInterface
    public static String searchLists (String search) {
        String foundLists = "";
        List<List<String>> result = activity.getDBH().load("SELECT * FROM lists WHERE name LIKE '%"+search+"%'");

        for(int i = 0; i < result.size(); i++) {
            if (i > 0) {
                foundLists += ";";
            }
            for (int k = 0; k < result.get(i).size(); k++) {
                if (k > 0) {
                    foundLists += ",";
                }
                foundLists += result.get(i).get(k);
            }
        }
        return foundLists;
    }

    @android.webkit.JavascriptInterface
    public static String searchItems (String search) {
        String foundItems = "";
        List<List<String>> result = activity.getDBH().load("SELECT * FROM items WHERE name LIKE '%"+search+"%'");

        for(int i = 0; i < result.size(); i++) {
            if (i > 0) {
                foundItems += ";";
            }
            for (int k = 0; k < result.get(i).size(); k++) {
                if (k > 0) {
                    foundItems += ",";
                }
                foundItems += result.get(i).get(k);
            }
        }
        return foundItems;
    }


    @android.webkit.JavascriptInterface
    public static void messageDiaglog(String header, String text) {
        new AlertDialog.Builder(activity)
            .setTitle(header)
            .setMessage(text)
            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Doesn't do anything for now
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }
}