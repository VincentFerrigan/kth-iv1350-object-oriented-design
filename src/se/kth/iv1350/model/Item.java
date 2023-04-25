package src.se.kth.iv1350.model;
import src.se.kth.iv1350.dto.ItemDTO;

public class Item {
    private ItemDTO item;
    private int quantity;

    public Item (ItemDTO item, int quantity){
        this.item = item;
        this.quantity = quantity;
    }
    public Item (ItemDTO item){
        this(item, 1);
    }

    /**
     * Increment an already added item when it's more than one of the same.
     */
    public void increment(){
        this.quantity++;
    }

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
    public ItemDTO getItemDTO() {
        return item;
    }
    public int getQuantity() {
        return quantity;
    }
}
