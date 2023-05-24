package se.kth.iv1350.model;

import se.kth.iv1350.integration.ItemDTO;

import java.time.LocalDateTime;

/**
 * Contains information about a particular item added to the shopping cart. Contains item information,
 * quantity and time of update.
 */
public class ShoppingCartItem {
    private final ItemDTO itemInfo;
    private LocalDateTime timeOfUpdate;
    private int quantity;

    /**
     * Creates a new instance representing the added item.
     * @param item ShoppingCartItem information as a {@link ItemDTO}
     * @param quantity The quantity
     */
    public ShoppingCartItem(ItemDTO item, int quantity){
        this.timeOfUpdate = LocalDateTime.now();
        this.itemInfo = item;
        this.quantity = quantity;
    }
    /**
     * Creates a new instance representing the added item.
     * @param item ShoppingCartItem information as a {@link ItemDTO}
     */
    public ShoppingCartItem(ItemDTO item){
        this(item, 1);
    }
//    @Deprecated
//    public void addItem(ShoppingCartItem anotherShoppingCartItem){
//        //TODO denna är bättre i sale
//        this.timeOfUpdate = LocalDateTime.now();
//        if (this.equals(anotherShoppingCartItem)) {
//            addToQuantity(anotherShoppingCartItem.getQuantity());
//        }
//    }
//    /**
//     * Set the quantity.
//     * @param  quantity nbr of items.
//     */
//    public void setQuantity(int quantity){
//        this.timeOfUpdate = LocalDateTime.now();
//        this.quantity = quantity;
//    }

    /**
     * Add quantity to item.
     * @param additionalQuantity the added quantity
     */
    public void addToQuantity(int additionalQuantity){
        this.timeOfUpdate = LocalDateTime.now();
        this.quantity += additionalQuantity;
    }

    /**
     * Get the total price as {@link Amount} i.e. quantity x unit price
     * @return the total price for all the same items.
     */
    public Amount getTotalSubPrice() {
        return getUnitPrice().multiply(quantity);
    }

    /**
     * Get the total VAT as {@link Amount}.
     * @return the total VAT
     */
    public Amount calculateTotalSubVATCost() {
        double vatRate = itemInfo.getVATRate();
        return getTotalSubPrice().multiply(vatRate);
    }

    /**
     * Get time of update
     * @return time of update
     */
    public LocalDateTime getTimeOfUpdate() {
        // TODO: Används ej. Detta då vi inte skickar runt Sale utan snarare SaleDTO
        return timeOfUpdate;
    }

    /**
     * Get item data information as {@link ItemDTO}
     * @return The item data information as {@link ItemDTO}
     */
    public ItemDTO getItemDTO() {
        // TODO: Används ej. Detta då vi inte skickar runt Sale utan snarare SaleDTO
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
        if (o == null || !(o instanceof ShoppingCartItem)) return false;

        ShoppingCartItem that = (ShoppingCartItem) o;

        if (quantity != that.quantity) return false;
        return itemInfo.equals(that.itemInfo);
    }
}