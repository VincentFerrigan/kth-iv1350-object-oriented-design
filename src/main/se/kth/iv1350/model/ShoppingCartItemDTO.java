package se.kth.iv1350.model;

import se.kth.iv1350.integration.ItemDTO;

import java.time.LocalDateTime;

// TODO ska discountDTO finnas med i SaleDTO och/eller ShoppingCartItemDTO?

/**
 * Contains information about a registered item (item info, quantity and total price) as a Data Transfer Object.
 */
public class ShoppingCartItemDTO {
    private final ItemDTO itemInfo;
    private LocalDateTime timeOfUpdate;
    private final int quantity;
    private final Amount totalPrice;

    /**
     * Creates a new instance representing a registered item.
     * @param itemInfo ShoppingCartItem information data transfer object from item registry.
     * @param timeOfUpdate The time that the item was registered.
     * @param quantity The quantity registered.
     * @param totalPrice The total price i.e. unit price x quantity.
     */
    public ShoppingCartItemDTO(ItemDTO itemInfo, LocalDateTime timeOfUpdate, int quantity, Amount totalPrice) {
        this.itemInfo = itemInfo;
        this.timeOfUpdate = timeOfUpdate;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    /**
     * Get the time of update
     * @return the time that the item was last registered
     */
    public LocalDateTime getTimeOfUpdate() {
        return timeOfUpdate;
    }

    /**
     * Get the item identifier
     * @return the item identifier
     */
    public int getItemID() {
        return itemInfo.getItemID();
    }

    /**
     * Get the name of the item
     * @return the name of the item
     */
    public String getName() {
        return itemInfo.getName();
    }

    /**
     * Get the items description
     * @return the description of the item
     */
    public String getDescription() {
        return itemInfo.getDescription();
    }

    /**
     * Get the unit price
     * @return the unit price
     */
    public Amount getUnitPrice() {
        return itemInfo.getUnitPrice();
    }

    /**
     * Get the items VAT Rate
     * @return the VAT Rate
     */
    public double getVATRate() {
        return itemInfo.getVATRate();
    }

    /**
     * Get the quantity registered of the particular item.
     * @return quantity registered of the particular item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Get the total price i.e. quantity x unit price
     * @return the total price of the same item.
     */
    public Amount getTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ShoppingCartItemDTO)) return false;

        ShoppingCartItemDTO that = (ShoppingCartItemDTO) o;

        if (quantity != that.quantity) return false;
        if (!(itemInfo.equals(that.itemInfo))) return false;
        return totalPrice.equals(that.totalPrice);
    }
}
