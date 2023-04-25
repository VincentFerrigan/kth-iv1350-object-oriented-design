package src.se.kth.iv1350.model;
import src.se.kth.iv1350.dto.ItemDTO;

public class Item {
    private ItemDTO itemInfo;
    private int quantity;

    public Item (ItemDTO item, int quantity){
        this.itemInfo = item;
        this.quantity = quantity;
    }
    public Item (ItemDTO item){
        this(item, 1);
    }

    // För salelog/sales
    /**
     * Increment an already added item when it's more than one of the same.
     */
    public void increment(){
        this.quantity++;
    }

    // För när vi uppdaterar inventory.
    public void decrement(){
        this.quantity--;
    }

    /**
     * Sets the quantity of items with same ID number.
     * @param   quantity    nbr of items
     */
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void addQuantity(int additionalQuantity){
        this.quantity += additionalQuantity;
    }
    public ItemDTO getItemDTO() {
        return itemInfo;
    }
    public int getQuantity() {
        return quantity;
    }
}
