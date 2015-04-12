package rainbowworks.rainbowlists;

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
