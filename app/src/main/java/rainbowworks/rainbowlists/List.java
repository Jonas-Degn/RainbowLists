package rainbowworks.rainbowlists;

/**
 * Created by nikolaj on 4/7/15.
 */
public class List {
    private int id;
    private String name;
    private String type;
    private Item[] items = new Item[100];

    public List (int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    protected int getId () {
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
        Item item = new Item(id, name, quantity);
    }

    protected Item getItem(int searchId) {
        boolean search = true;
        while (search) {
            if ()
            search = false;
        }
        return
    }


}
