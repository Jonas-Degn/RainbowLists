package rainbowworks.rainbowlists;

import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.HashMap;

public class MainActivity extends ActionBarActivity {
    private MenuItem register;
    private WebView webView;
    private String currentPage;
    private JavaInterface jsInterface;
    private DatabaseHandler dbh;
    private HashMap<Integer, RainbowList> lists;

    /*
     * Create application
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);

        dbh = new DatabaseHandler(this);
        jsInterface = new JavaInterface(this);

        webView = (WebView)findViewById(R.id.mainWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.addJavascriptInterface(jsInterface, "JSInterface");
        webView.setWebChromeClient(new WebChromeClient());
        if (savedInstanceState == null) {
            webView.loadUrl("file:///android_asset/www/index.html");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }

        currentPage = "index";
    }
    /*
     * For when the page is changed directly in frontend
     */
    protected void setLocation(String file) {
        currentPage = file;
        invalidateOptionsMenu();
    }

    /*
     * Change webview page from backend
     */
    protected void loadUrl(final String file) {
        setLocation(file);
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("file:///android_asset/www/"+file+".html");
            }
        });

    }

    /*
     * Save state of webview
     * Used for mechanics that may try to reset webview
     */
    @Override
    protected void onSaveInstanceState(Bundle restoreInstanceState)
    {
        webView.saveState(restoreInstanceState);
        super.onSaveInstanceState(restoreInstanceState);

    }

    /*
     * Restore state of webview
     * Used for mechanics that may try to reset webview
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        webView.restoreState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    /*
     * Create options menu from XML
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
     * Content of options menu
     * Changes per webview page
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        register = menu.findItem(R.id.action_settings);
        register.setVisible(currentPage != null);

        register = menu.findItem(R.id.action_quit);
        register.setVisible(currentPage != null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true; // INSERT SETTINGS MENU HERE
            case R.id.action_quit:
                this.finish();
                System.exit(0);
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            WebBackForwardList history = webView.copyBackForwardList();
            String lastURL = history.getItemAtIndex(history.getCurrentIndex()-1).getOriginalUrl();
            setLocation(lastURL.replace("file:///android_asset/www/", "").replace(".html", ""));
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    protected DatabaseHandler getDBH() {
        return dbh;
    }

    protected RainbowList getList(int listID) {
        return lists.get(listID);
    }

    protected HashMap<Integer, RainbowList> getLists() {
        return lists;
    }
}