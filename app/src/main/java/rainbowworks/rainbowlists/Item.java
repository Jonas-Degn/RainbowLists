package rainbowworks.rainbowlists;

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

    protected String getName () {
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
