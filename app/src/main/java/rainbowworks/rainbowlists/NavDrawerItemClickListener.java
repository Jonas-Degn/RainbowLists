package rainbowworks.rainbowlists;

import android.view.View;

public class NavDrawerItemClickListener implements View.OnClickListener {
    private MainActivity activity;

    /**
     * Constructor for the click listener
     * @param activity parsed to keep everything in the same activity
     */
    NavDrawerItemClickListener(MainActivity activity) {
        this.activity = activity;
    }

    /**
     * Handles the clicking of items in our navigation drawer
     * @param v View of the item clicked
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case 0: JavaInterface.runJS("loadPage('listOverview.html')"); break;
            case 1: JavaInterface.runJS("loadPage('itemOverview.html')"); activity.setCurrentList(0); break;
            case 2: JavaInterface.runJS("loadPage('settings.html')"); break;
            case 3: JavaInterface.runJS("loadPage('about.html')"); break;
            case 4: activity.finish(); System.exit(0);
        }
        activity.getDrawerLayout().closeDrawers();
    }
}