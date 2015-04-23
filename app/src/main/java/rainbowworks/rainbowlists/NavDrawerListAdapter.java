package rainbowworks.rainbowlists;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
    private MainActivity activity;

    /**
     * Contructor for our Adapter used with our navigation drawer
     * @param context parsed to keep everything in the same context
     * @param navDrawerItems is the array of items we have in our drawer
     * @param activity parsed to keep everything in the same activity
     */
	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems, MainActivity activity){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
        this.activity = activity;
	}

    /**
     * Get the amount of items in the drawer
     * @return int amount
     */
	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

    /**
     * Get a specific item object from the drawer
     * @param position starting from 0, the number of the item
     * @return Object with the item object
     */
	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

    /**
     * Get the position of an object when knowing the location/ID
     * @param position starting from 0, the number of the item
     * @return return exact same position
     * This method is required by BaseAdapter - we don't need it to return itself...
     */
	@Override
	public long getItemId(int position) {
		return position;
	}

    /**
     * Generate the actual drawer with all the content, based on a XML template
     * @param position starting from 0, the number of the item
     * @param convertView the view in which we create the content
     * @param parent the parent view in which we place our new view
     * @return View the new view with content
     */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
         
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
         
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());        
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        convertView.setId(position);
        convertView.setOnClickListener(new NavDrawerItemClickListener(activity));
        
        return convertView;
	}

}
