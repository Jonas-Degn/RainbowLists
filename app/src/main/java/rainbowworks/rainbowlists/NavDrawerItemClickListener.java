package rainbowworks.rainbowlists;

import android.view.View;

public class NavDrawerItemClickListener implements View.OnClickListener {
    private MainActivity activity;

    NavDrawerItemClickListener(MainActivity activity) {
        this.activity = activity;
    }

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