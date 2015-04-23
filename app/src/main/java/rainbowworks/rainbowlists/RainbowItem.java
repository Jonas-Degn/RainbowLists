package rainbowworks.rainbowlists;

/**
 * Created by nikolaj on 4/7/15.
 */

public class RainbowItem {
    private int id;
    private String quantity;
    private String name;
    private boolean isChecked;

    /**
     * Constructor for our items/products
     * @param id is an integer to identify the item
     * @param name is the name a user has given the item
     * @param quantity is the amount as a string - can also contain text
     * @param isChecked is whether the item has been checked
     */
    public RainbowItem(int id, String name, String quantity, boolean isChecked)  {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.isChecked = isChecked;
    }

    /**
     * Get the ID of this item
     * @return int ID
     */
    protected int getID () {
        return id;
    }

    /**
     * Get the name of this item
     * @return String name
     */
    protected String getName () {
        return name;
    }

    /**
     * Get if the item has been checked
     * @return boolean isChecked
     */
    protected boolean getIsChecked () {
        return isChecked;
    }

    /**
     * Get the quantity of this item
     * Quantity can be empty, a number, a text or a number and text.
     * @return String quantity
     */
    protected String getQuantity () {
        return quantity;
    }
}
