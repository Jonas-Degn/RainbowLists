package rainbowworks.rainbowlists;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;
public class JavaInterface {
    static MainActivity activity;
    static WebView webView;

    public JavaInterface(MainActivity act, WebView newWebView) {
        activity = act;
        webView = newWebView;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAppCacheEnabled(true);

        webView.addJavascriptInterface(this, "JSInterface");
        webView.setWebChromeClient(new WebChromeClient());

        // DAMN IT MOTHERFUCKER - old phones can't debug :(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT < 18) {
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        }
    }

    @android.webkit.JavascriptInterface
    public static String loadSettings() {
        return "No settings atm";
    }

    @android.webkit.JavascriptInterface
    public static String loadLists(String type) {
        String foundLists = "";
        try {
            for (HashMap.Entry<Integer, RainbowList> entry : activity.getLists().entrySet()) {
                int id = entry.getKey();
                RainbowList list = entry.getValue();
                if (list.getType().equals(type)) {
                    if (foundLists.equals("")) {
                        foundLists += list.getID() + "," + list.getName();
                    } else {
                        foundLists += ";" + list.getID() + "," + list.getName();
                    }
                }
            }
        }
        catch (NullPointerException e) {
            return "";
        }
        return foundLists;
    }


    @android.webkit.JavascriptInterface
    public static String loadItems(int listID) {
        String foundItems = "";
        RainbowList currentList = activity.getList(listID);

        for(HashMap.Entry<Integer, Item> entry : currentList.getItems().entrySet()) {
            int id = entry.getKey();
            Item item = entry.getValue();
            if (foundItems.equals("")) {
                foundItems += item.getID()+","+item.getName()+","+item.getQuantity()+","+item.getIsChecked();
            }
            else {
                foundItems += ";"+item.getID()+","+item.getName()+","+item.getQuantity()+","+item.getIsChecked();
            }
        }
        return foundItems;
    }

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
    public static void createList (String data) {
        String[] newData = data.split(";");
        int id = Integer.parseInt(newData[0]);
        String name = newData[1];

        // Now add this information to the
    }

    @android.webkit.JavascriptInterface
    public static void messageDialog(String header, String text) {
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

    @android.webkit.JavascriptInterface
    public static void inputDialog(String header, String description, String text, String okButton, String cancelButton, final String action) {
        final EditText input = new EditText(activity);
        input.setText(text);

        new AlertDialog.Builder(activity)
                .setTitle(header)
                .setMessage(description)
                .setView(input)
                .setPositiveButton(okButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(action) {
                            case "newShoppingList":
                                // Save in database new list with name 'input.getText().toString()'
                                // Add to HashMap 'lists' in MainActivity
                                activity.getDBH().save("INSERT INTO lists (name,type) VALUES ('"+input.getText().toString()+"','shopping')");
                                activity.populateLists();
                                break;
                            case "newPantryList":
                                // Save in database new list with name 'input.getText().toString()';
                                // Add to HashMap 'lists' in MainActivity
                                activity.getDBH().save("INSERT INTO lists (name,type) VALUES ('"+input.getText().toString()+"','pantry')");
                                activity.populateLists();
                                break;
                            case "editShoppingList":
                                // Update in database list with old name 'text' name 'input.getText().toString()'
                                // Update name in HashMap 'lists' in MainActivity
                                break;
                            case "editPantryList":
                                // Update in database list with old name 'text' name 'input.getText().toString()'
                                // Update name in HashMap 'lists' in MainActivity
                                break;
                        }
                        runJS("loadPage('"+activity.getLocation()+".html')");
                    }
                })
                .setNegativeButton(cancelButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        runJS("loadPage('"+activity.getLocation()+".html')");
                    }
                })
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @android.webkit.JavascriptInterface
    public static void deleteList(String id) {
        activity.getDBH().save("DELETE FROM lists WHERE id = " + id);
        activity.populateLists();
    }


    @android.webkit.JavascriptInterface
    public static void setLocation(String file) {
        activity.setLocation(file);
    }

    @android.webkit.JavascriptInterface
    public static void setCurrentList(int id) {
        activity.setCurrentList(id);
    }

    @android.webkit.JavascriptInterface
    public static int getCurrentList() {
        return activity.getCurrentList();
    }

    @android.webkit.JavascriptInterface
    public static void setCurrentAction(String action) {
        activity.setCurrentAction(action);
    }

    @android.webkit.JavascriptInterface
    public static String getCurrentAction() {
        return activity.getCurrentAction();
    }

    public static void runJS(final String scriptSrc) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + scriptSrc);
            }
        });
    }
}