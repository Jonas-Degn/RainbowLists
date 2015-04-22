package rainbowworks.rainbowlists;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.HashMap;
import java.util.List;
public class JavaInterface {
    private static final int RESULT_OK = 1;
    private static final int RESULT_CANCELED = 0;
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
            Item item = entry.getValue();
            if (!item.getName().equals("empty")) {
                if (foundItems.equals("")) {
                    foundItems += currentList.getID()+","+item.getID()+","+item.getName()+","+item.getQuantity()+","+item.getIsChecked();
                }
                else {
                    foundItems += ";"+currentList.getID()+","+item.getID()+","+item.getName()+","+item.getQuantity()+","+item.getIsChecked();
                }
            }
        }
        return foundItems;
    }

    @android.webkit.JavascriptInterface
    public static String loadItems() {
        String foundItems = "";

        for (HashMap.Entry<Integer, RainbowList> entry : activity.getLists().entrySet()) {
            for (HashMap.Entry<Integer, Item> entry2 : entry.getValue().getItems().entrySet()){
                Item item = entry2.getValue();
                if (!item.getName().equals("empty")) {
                    if (foundItems.equals("")) {
                        foundItems += item.getID() + "," + item.getName() + "," + item.getQuantity() + "," + (item.getIsChecked()?"1":"0");
                    } else {
                        foundItems += ";" + item.getID() + "," + item.getName() + "," + item.getQuantity() + "," + (item.getIsChecked()?"1":"0");
                    }
                }
            }
        }
        return foundItems;
    }

    @android.webkit.JavascriptInterface
    public static String searchLists(String search) {
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
    public static String searchItems(String search) {
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
    public static void createList(String data) {
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

        InputFilter noSpecialCharacters = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        input.setFilters(new InputFilter[]{noSpecialCharacters});

        new AlertDialog.Builder(activity)
                .setTitle(header)
                .setMessage(description)
                .setView(input)
                .setPositiveButton(okButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        switch (action) {
                            case "newShoppingList":
                                // Save in database new list with name 'input.getText().toString()'
                                // Add to HashMap 'lists' in MainActivity
                                activity.getDBH().save("INSERT INTO lists (name,type) VALUES ('" + input.getText().toString() + "','shopping')");
                                activity.populateLists();
                                break;
                            case "newPantryList":
                                // Save in database new list with name 'input.getText().toString()';
                                // Add to HashMap 'lists' in MainActivity
                                activity.getDBH().save("INSERT INTO lists (name,type) VALUES ('" + input.getText().toString() + "','pantry')");
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
                            case "addProduct":
                                // Save in database new product with name 'input.getText().toString()';
                                // Add to HashMap 'lists' in MainActivity
                                String[] newInput = input.getText().toString().split("\\s+");
                                String amount = "";
                                String name = "";
                                if (newInput.length > 2) {
                                    // 1 amount, 2 amount type, 3 name
                                    amount = newInput[0] + " " + newInput[1];
                                    name = input.getText().toString().replace(newInput[0] + " " + newInput[1],"");
                                }
                                else if (newInput.length == 2) {
                                    // 1 amount, 2 name
                                    amount = newInput[0];
                                    name = input.getText().toString().replace(newInput[0],"");
                                }
                                else {
                                    // 1 name
                                    name = input.getText().toString();
                                }
                                activity.getDBH().save("INSERT INTO items (listID,name,amount,isChecked) VALUES ("+activity.getCurrentList()+",'" + name + "','" + amount + "',0)");
                                activity.populateLists();
                                break;
                            case "scanProduct":
                                // Save in database new product with name 'input.getText().toString()';
                                // Add to HashMap 'lists' in MainActivity
                                amount = input.getText().toString();
                                activity.getDBH().save("INSERT INTO items (listID,name,amount,isChecked) VALUES ("+activity.getCurrentList()+",'emptyScan','" + amount + "',0)");
                                getBarcode();
                                break;
                        }
                        runJS("loadPage('" + activity.getLocation() + ".html')");
                    }
                })
                .setNegativeButton(cancelButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        dialog.cancel();
                        runJS("loadPage('"+activity.getLocation()+".html')");
                    }
                })
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        input.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @android.webkit.JavascriptInterface
    public static void deleteList(String id) {
        activity.getDBH().save("DELETE FROM lists WHERE id = " + id);
        activity.populateLists();
    }


    @android.webkit.JavascriptInterface
    public static void setLocation(String file) {
        activity.setLocation(file);
        switch(file) {
            case "listOverview":
                activity.runOnUiThread(new Runnable(){
                    public void run() {
                        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xfff2594b));
                        //activity.getActionBar().setBackgroundDrawable(new ColorDrawable(R.color.orange));
                    }
                });

                break;
            case "itemOverview":
                activity.runOnUiThread(new Runnable(){
                    public void run() {
                        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff27a0ab));
                        //activity.getActionBar().setBackgroundDrawable(new ColorDrawable(R.color.blue));
                    }
                });
                break;
        }
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

    @android.webkit.JavascriptInterface
    public static void getBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.initiateScan();
    }

}