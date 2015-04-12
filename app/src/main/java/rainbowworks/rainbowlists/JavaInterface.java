package rainbowworks.rainbowlists;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /* RainbowList class not done yet
    @android.webkit.JavascriptInterface
    public static String loadItems(int listID) {
        String foundItems = "";
        RainbowList currentList = activity.getList(listID);

        for(HashMap.Entry<Integer, Item> entry : currentList.getItems()) {
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
    }*/

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
    public static void setLocation(String file) {
        activity.setLocation(file);
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