package src.se.kth.iv1350.model;
import src.se.kth.iv1350.dto.ItemDTO;

import java.time.LocalDateTime;

public class Item {
    private final ItemDTO itemInfo;
    private LocalDateTime timeOfUpdate;
    private int quantity;

    public Item(ItemDTO item, int quantity){
        this.timeOfUpdate = LocalDateTime.now();
        this.itemInfo = item;
        this.quantity = quantity;
    }
    public Item(ItemDTO item){
        this(item, 1);
    }
    public void addItem(Item anotherItem){
        this.timeOfUpdate = LocalDateTime.now();
        if (this.equals(anotherItem)) {
            addToQuantity(anotherItem.getQuantity());
        }
    }
    /**
     * Sets the quantity of items with same ID number.
     * @param   quantity    nbr of items
     */
    public void setQuantity(int quantity){
        this.timeOfUpdate = LocalDateTime.now();
        this.quantity = quantity;
    }

    public void addToQuantity(int additionalQuantity){
        this.timeOfUpdate = LocalDateTime.now();
        this.quantity += additionalQuantity;
    }

    // Getters
    public LocalDateTime getTimeOfUpdate() {
        return timeOfUpdate;
    }
    public ItemDTO getItemDTO() {
        return itemInfo;
    }

    public int getItemID() {
        return itemInfo.getItemID();
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the total amount i.e. the item price x quantity.
     */
    public Amount getTotalPrice() {
        return getUnitPrice().multiply(quantity);
    }
    public Amount getVatAmount() {
        double vatRate = itemInfo.getVATRate();
        return getTotalPrice().multiply(vatRate);
    }
    public Amount getUnitPrice() {return itemInfo.getUnitPrice();}

    public String getName() {
        return itemInfo.getName();
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {return true;}
        Item c = (Item) o;
        return Integer.compare(this.getItemID(), c.getItemID()) == 0;
    }

    // TODO exemptions?
    // TODO increment och decrement()? OBS! uppdatera totalbelopp!
    // Eventuellt för salelog/sales
    /**
     * Increment an already added item when it's more than one of the same.
     */
    public void increment(){
        this.timeOfUpdate = LocalDateTime.now();
        this.quantity++;
    }

    // TODO increment och decrement()? OBS! uppdatera totalbelopp!
    // Eventuellt för när vi uppdaterar inventory.
    public void decrement(){
        this.timeOfUpdate = LocalDateTime.now();
        this.quantity--;
    }
}