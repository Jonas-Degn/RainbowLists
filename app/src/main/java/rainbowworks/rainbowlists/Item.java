package rainbowworks.rainbowlists;

/**
 * Created by nikolaj on 4/7/15.
 */
public class Item {
    private int id;
    private String quantity;
    private String name;
    private boolean isChecked;

    public Item (int id, String name, String quantity, boolean isChecked)  {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.isChecked = isChecked;
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

    protected void setQuantity (String quantity) {
        this.quantity = quantity;
    }

    protected String getQuantity () {
        return quantity;
    }
}
