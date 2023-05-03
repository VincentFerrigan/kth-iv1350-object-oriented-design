package se.kth.iv1350.dto;

import se.kth.iv1350.model.Amount;

// TODO ska discountDTO finnas med i SaleDTO och/eller SaleItemDTO?

/**
 * Contains information about a registered item (item info, quantity and total price)
 */
public class SaleItemDTO {
    private final ItemDTO itemInfo;
    private final int quantity;
    private final Amount totalPrice;

    /**
     * Creates a new instance representing a registered item.
     * @param itemInfo Item information data transfer object from item registry.
     * @param quantity The quantity registered
     * @param totalPrice The total price i.e. unit price x quantity.
     */
    public SaleItemDTO(ItemDTO itemInfo, int quantity, Amount totalPrice) {
        this.itemInfo = itemInfo;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
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
}
