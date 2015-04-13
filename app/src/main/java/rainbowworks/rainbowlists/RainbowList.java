package rainbowworks.rainbowlists;
/**
 * Created by Nicolai on 13-04-2015
 *
 * // Import ArrayList
 * import java.util.ArrayList;
 *
 * public class RainbowList {
 *     public static void man(String[] args {
 *         // Construct a new empty ArrayList for type String
 *         ArrayList<String> obj = new ArrayList<String>();
 *
 *         // Add items into ArrayList (index, String)
 *         obj.add(0, "Item1");
 *         obj.add(1, "Item2");
 *         obj.add(2, "Item3");
 *         obj.add(3, "Item4");
 *         obj.add(4, "Item5");
 *         obj.add(5, "Item6");
 *         obj.add(6, "Item7");
 *         obj.add(7, "Item8");
 *         obj.add(8, "Item9");
 *         obj.add(9, "Item10");
 *     }
 * }
 */
import java.util.HashMap;

/**
 * Created by nikolaj on 4/7/15.
 */
public class RainbowList {
    private int id;
    private String name;
    private String type;
    private HashMap<Integer, Item> items;

    public RainbowList (int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    protected int getID () {
        return id;
    }

    protected void setName (String name) {
        this.name = name;
    }

    protected String getName () {
        return name;
    }

    protected void setType (String type) {
        this.type = type;
    }

    protected String getType () {
        return type;
    }

    protected void addItem (int id, String name, int quantity) {
        items.put(id, new Item(id, name, quantity));
    }

    /* Not functional yet, keeping commented to not break build
    protected Item getItem(int searchId) {
        boolean search = true;
        while (search) {
            if ()
            search = false;
        }
        return
    }*/


}
