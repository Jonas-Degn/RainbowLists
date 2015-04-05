package rainbowworks.rainbowlists;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class JavascriptInterface {
    static MainActivity activity;

    public JavascriptInterface(MainActivity act) {
        activity = act;
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