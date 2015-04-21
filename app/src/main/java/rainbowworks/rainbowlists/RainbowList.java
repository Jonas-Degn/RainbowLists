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
        this.items = new HashMap<Integer, Item>();
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

    protected void addItem (int id, String name, String quantity, boolean isChecked) {
        items.put(Integer.valueOf(id), new Item(id, name, quantity, isChecked));
    }

    protected Item getItem(int searchId) {
        boolean search = true;
        int i = 1;
        Item x = items.get(1);
        while (search) {
            x = items.get(i);
            if (x.getID() == searchId){
                search = false;
            }
            i++;
        }
        return x;
    }

    protected HashMap<Integer, Item> getItems () {
        return items;
    }

}
