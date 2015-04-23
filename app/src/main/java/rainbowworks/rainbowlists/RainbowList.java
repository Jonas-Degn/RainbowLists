package rainbowworks.rainbowlists;

import java.util.HashMap;

/**
 * Created by nikolaj on 4/7/15.
 */
public class RainbowList {
    private int id;
    private String name;
    private String type;
    private HashMap<Integer, RainbowItem> items;

    /**
     * Constructor for our lists
     * @param id is an integer to identify the item
     * @param name is the name a user has given the item
     * @param type is type of list, e.g. shopping or pantry
     */
    public RainbowList (int id, String name, String type) {
        this.items = new HashMap<Integer, RainbowItem>();
        this.id = id;
        this.name = name;
        this.type = type;
    }

    /**
     * Get the ID of this list
     * @return int ID
     */
    protected int getID () {
        return id;
    }

    /**
     * Get the name of this list
     * @return String name
     */
    protected String getName () {
        return name;
    }

    /**
     * Get the type of this list
     * @return String list type
     */
    protected String getType () {
        return type;
    }

    /**
     * Add a new item/product to this list
     * @param id is an integer to identify the item
     * @param name is the name a user has given the item
     * @param quantity is the amount as a string - can also contain text
     * @param isChecked is whether the item has been checked
     */
    protected void addItem (int id, String name, String quantity, boolean isChecked) {
        items.put(Integer.valueOf(id), new RainbowItem(id, name, quantity, isChecked));
    }

    /**
     * Get all items/products in this list
     * @return HashMap<Integer, RainbowItem> with ID and object reference of the items
     */
    protected HashMap<Integer, RainbowItem> getItems () {
        return items;
    }

}
