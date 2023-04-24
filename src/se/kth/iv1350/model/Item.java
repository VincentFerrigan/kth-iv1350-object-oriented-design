package src.se.kth.iv1350.model;
import src.se.kth.iv1350.dto.ItemDTO;

public class Item {
    private int quantity;
    private ItemDTO itemInfo;

    public Item (ItemDTO itemInfo){
        this.itemInfo = itemInfo;
    }

    /**
     * Increment an already added item when it's more than one of the same.
     */
    public void increment(){
        this.quantity++;
    }

    /**
     * Sets the quantity of items with same ID number.
     * @param   quantity    nbr of items
     */
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
}
