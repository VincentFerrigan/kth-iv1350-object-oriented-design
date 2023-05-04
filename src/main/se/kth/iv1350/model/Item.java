package se.kth.iv1350.model;

import se.kth.iv1350.integration.ItemDTO;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Contains information about a particular item added to the shopping cart. Contains item information,
 * quantity and time of update.
 */
public class Item {
    private final ItemDTO itemInfo;
    private LocalDateTime timeOfUpdate;
    private int quantity;

    /**
     * Creates a new instance representing the added item.
     * @param item Item information as a {@link ItemDTO}
     * @param quantity The quantity
     */
    public Item(ItemDTO item, int quantity){
        this.timeOfUpdate = LocalDateTime.now();
        this.itemInfo = item;
        this.quantity = quantity;
    }
    /**
     * Creates a new instance representing the added item.
     * @param item Item information as a {@link ItemDTO}
     */
    public Item(ItemDTO item){
        this(item, 1);
    }
    @Deprecated
    public void addItem(Item anotherItem){
        //TODO denna är bättre i sale
        this.timeOfUpdate = LocalDateTime.now();
        if (this.equals(anotherItem)) {
            addToQuantity(anotherItem.getQuantity());
        }
    }
    /**
     * Set the quantity.
     * @param  quantity nbr of items.
     */
    public void setQuantity(int quantity){
        this.timeOfUpdate = LocalDateTime.now();
        this.quantity = quantity;
    }

    /**
     * Add quantity to item.
     * @param additionalQuantity
     */
    public void addToQuantity(int additionalQuantity){
        this.timeOfUpdate = LocalDateTime.now();
        this.quantity += additionalQuantity;
    }

    /**
     * Get time of update
     * @return time of update
     */
    public LocalDateTime getTimeOfUpdate() {
        return timeOfUpdate;
    }

    /**
     * Get item data information as {@link ItemDTO}
     * @return The item data information as {@link ItemDTO}
     */
    public ItemDTO getItemDTO() {
        return itemInfo;
    }

    /**
     * Get the item identifier
     * @return the item identifier
     */
    public int getItemID() {
        return itemInfo.getItemID();
    }

    /**
     * Get the quantity of the particular item.
     * @return quantity of the particular item
     */
    public int getQuantity() {
        return quantity;
    }

     /**
     * Get the total price as {@link Amount} i.e. quantity x unit price
     * @return the total price for all the same items.
     */
    public Amount getTotalPrice() {
        return getUnitPrice().multiply(quantity);
    }

    /**
     * Get the total VAT as {@link Amount}.
     * @return the total VAT
     */
    public Amount getVatAmount() {
        double vatRate = itemInfo.getVATRate();
        return getTotalPrice().multiply(vatRate);
    }
    /**
     * Get the unit price
     * @return the unit price
     */
    public Amount getUnitPrice() {return itemInfo.getUnitPrice();}

    /**
     * Get the name of the item
     * @return the name of the item
     */
    public String getName() {
        return itemInfo.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SaleDTO)) return false;

        Item item = (Item) o;

        if (quantity != item.quantity) return false;
        return itemInfo.equals(item.itemInfo);
    }
    /**
     * Increment an already added item when it's more than one of the same.
     */
    @Deprecated
    public void increment(){
        this.timeOfUpdate = LocalDateTime.now();
        this.quantity++;
    }

    /**
     * Decrement an already added item when it's more than one of the same.
     */
    @Deprecated
    public void decrement(){
        //TODO denna används väl inte?
        this.timeOfUpdate = LocalDateTime.now();
        this.quantity--;
    }
}