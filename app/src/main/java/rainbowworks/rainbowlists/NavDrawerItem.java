package rainbowworks.rainbowlists;

public class NavDrawerItem {
	
	private String title;
	private int icon;
	private String count = "0";
	private boolean isCounterVisible = false;

    /**
     * Constructor for the Navigation Drawer Item
     * @param title of the item
     * @param icon of the item
     */
	public NavDrawerItem(String title, int icon){
		this.title = title;
		this.icon = icon;
	}

    /**
     * Get the title of the item
     * @return String title
     */
	public String getTitle(){
		return this.title;
	}

    /**
     * Get the icon of the item
     * @return int referring to the icon
     */
	public int getIcon(){
		return this.icon;
	}

}
