package rainbowworks.rainbowlists;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;

import com.google.zxing.integration.android.IntentIntegrator;


public class JavaInterface {
    static MainActivity activity;
    static WebView webView;

    /**
     * @android.webkit.JavascriptInterface
     * Above line means that the given method is callable from the browser through Javascript.
     * A method without that line is a Java-only method.
     */

    /**
     * Constructor for our Java/Javascript interface
     * @param act parsed to keep everything in the same activity
     * @param newWebView contains the browser we'll be communicating with
     */
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

        // DAMN IT, we cannot debug the browser of older phones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT < 18) {
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        }
    }

    /**
     * Load settings, this feature is not a part of the project
     * @return String with features
     */
    @android.webkit.JavascriptInterface
    public static String loadSettings() {
        return "No settings atm";
    }

    /**
     * Load all lists from the array of RainbowList, which originates from the database
     * @param type e.g shopping or pantry
     * @return String with every list and its ID
     */
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

    /**
     * Load all items in a given list from the array of RainbowItem, which originates from the database
     * @param listID is the ID of the given list we want to load
     * @return String with every item found
     */
    @android.webkit.JavascriptInterface
    public static String loadItems(int listID) {
        String foundItems = "";

        // if ID is 0 then we are at the item overview, so show every single item
        if (listID == 0) {
            for (HashMap.Entry<Integer, RainbowList> entry : activity.getLists().entrySet()) {
                for (HashMap.Entry<Integer, RainbowItem> entry2 : entry.getValue().getItems().entrySet()){
                    RainbowItem item = entry2.getValue();
                    if (!item.getName().equals("empty")) {
                        if (foundItems.equals("")) {
                            foundItems += listID + "," + item.getID() + "," + item.getName() + "," + item.getQuantity() + "," + item.getIsChecked();
                        } else {
                            foundItems += ";" + listID + "," + item.getID() + "," + item.getName() + "," + item.getQuantity() + "," + item.getIsChecked();
                        }
                    }
                }
            }
        }
        // otherwise only show for the parsed list ID
        else {
            RainbowList currentList = activity.getList(listID);
            for(HashMap.Entry<Integer, RainbowItem> entry : currentList.getItems().entrySet()) {
                RainbowItem item = entry.getValue();
                if (!item.getName().equals("empty")) {
                    if (foundItems.equals("")) {
                        foundItems += currentList.getID() + "," + item.getID() + "," + item.getName() + "," + item.getQuantity() + "," + item.getIsChecked();
                    }
                    else {
                        foundItems += ";" + currentList.getID() + "," + item.getID() + "," + item.getName() + "," + item.getQuantity() + "," + item.getIsChecked();
                    }
                }
            }
        }
        return foundItems;
    }

    /**
     * Search for a list by name in the search field
     * @param search is partial name that a user have typed
     * @return String with suggestions which matches the search text
     */
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

    /**
     * Show a message dialog to the user with any given title and text
     * @param header is the title which the dialog will contain
     * @param text is the text which the dialog will contain
     */
    @android.webkit.JavascriptInterface
    public static void messageDialog(String header, String text) {
        new AlertDialog.Builder(activity)
                .setTitle(header)
                .setMessage(text)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Don't do anything specific when you click Okay
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Show an input dialog where everything is customizable
     * @param header is the title which the dialog will contain
     * @param description is the text which the dialog will contain
     * @param text is the initial text placed in the input field
     * @param okButton is the text that the positive button will show
     * @param cancelButton is the text that the negative button will show
     * @param action is a string which decides what to do with the given input
     */
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

    /**
     * Delete any list which matches the given ID
     * @param id is the ID of the list to delete
     */
    @android.webkit.JavascriptInterface
    public static void deleteList(String id) {
        activity.getDBH().save("DELETE FROM lists WHERE id = " + id);
        activity.getDBH().save("DELETE FROM items WHERE listID = " + id);
        activity.populateLists();
    }

    /**
     * Delete any item which matches the given ID
     * @param id is the ID of the item to delete
     */
    @android.webkit.JavascriptInterface
    public static void deleteItem(String id) {
        activity.getDBH().save("DELETE FROM items WHERE id = " + id);
        activity.populateLists();
    }

    /**
     * Markes the given item as checked/selected
     * @param id is the ID of the item to select
     */
    @android.webkit.JavascriptInterface
    public static void checkItem(String id) {
        activity.getDBH().save("UPDATE items SET isChecked=1 WHERE id = " + id);
        activity.populateLists();
    }

    /**
     * Markes the given item as unchecked/deselected
     * @param id is the ID of the item to deselect
     */
    @android.webkit.JavascriptInterface
    public static void decheckItem(String id) {
        activity.getDBH().save("UPDATE items SET isChecked=0 WHERE id = " + id);
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
                        activity.setTitle("List overview");
                    }
                });
                break;
            case "itemOverview":
                activity.runOnUiThread(new Runnable(){
                    public void run() {
                        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff27a0ab));
                        activity.setTitle("Item overview");
                    }
                });
                break;
            case "settings":
                activity.runOnUiThread(new Runnable(){
                    public void run() {
                        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffffffff));
                        activity.setTitle("Settings");
                    }
                });
                break;
        }
    }

    /**
     * Get the name of the currently shown file
     * @return String name of file
     */
    @android.webkit.JavascriptInterface
    public static String getLocation() {
        return activity.getLocation();
    }

    /**
     * A list has been selected, save which list it is
     * @param id of the selected list
     */
    @android.webkit.JavascriptInterface
    public static void setCurrentList(int id) {
        activity.setCurrentList(id);
    }

    /**
     * Get the ID of the currently selected list
     * @return int id of the selected list
     */
    @android.webkit.JavascriptInterface
    public static int getCurrentList() {
        return activity.getCurrentList();
    }

    /**
     * Get the name of the currently selected list
     * @return String name of the list
     */
    @android.webkit.JavascriptInterface
    public static String getCurrentListName() {
        return activity.getList(activity.getCurrentList()).getName();
    }

    /**
     * Sets the current action which we want to interact with back button code
     * @param action name of what the user is doing
     */
    @android.webkit.JavascriptInterface
    public static void setCurrentAction(String action) {
        activity.setCurrentAction(action);
    }

    /**
     * Get what action the user is doing right now
     * @return String name of the action
     */
    @android.webkit.JavascriptInterface
    public static String getCurrentAction() {
        return activity.getCurrentAction();
    }

    /**
     * Injects javascript code into the browser, giving us access to manipulate the view from Java
     * @param scriptSrc is whatever kind of javascript we want to run
     */
    public static void runJS(final String scriptSrc) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + scriptSrc);
            }
        });
    }

    /**
     * Runs the barcode scanner so we can scan a product
     */
    @android.webkit.JavascriptInterface
    public static void getBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.initiateScan();
    }

    /**
     * Resets database
     */
    @android.webkit.JavascriptInterface
    public void resetDB() {
        activity.getDBH().reset();
        activity.populateLists();
        messageDialog("Reset","All your lists and items have been removed.");
    }

}