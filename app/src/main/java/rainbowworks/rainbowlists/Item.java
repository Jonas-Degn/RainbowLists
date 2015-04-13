package rainbowworks.rainbowlists;
/**
 * Created by Nicolai on 13-04-2015
 *
 * // Import ArrayList
 * import java.util.ArrayList;
 *
 * public class Item {
 *     public static void man(String[] args {
 *         // Construct a new empty ArrayList for type String
 *         ArrayList<String> obj = new ArrayList<String>();
 *
 *     }
 * }
 */
/**
 * Created by nikolaj on 4/7/15.
 */
public class Item {
    private int id;
    private int quantity;
    private String name;
    private boolean isChecked;

    public Item (int id, String name, int quantity)  {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.isChecked = false;
    }

    protected int getID () {
        return id;
    }

    protected void setName (String name) {
        this.name = name;
    }

    protected String getName (String name) {
        return name;
    }

    protected void setIsChecked (boolean checked) {
        this.isChecked = true;
    }

    protected boolean getIsChecked () {
        return isChecked;
    }

    protected void setQuantity (int quantity) {
        this.quantity = quantity;
    }

    protected int getQuantity () {
        return quantity;
    }
}
