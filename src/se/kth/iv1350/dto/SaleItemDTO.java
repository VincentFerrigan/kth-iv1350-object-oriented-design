package src.se.kth.iv1350.dto;

import src.se.kth.iv1350.model.Amount;

// TODO ska discountDTO finnas med i SaleDTO och/eller SaleItemDTO?
public class SaleItemDTO {
    private final ItemDTO itemInfo;
    private final int quantity;
    private final Amount totalPrice;

    public SaleItemDTO(ItemDTO itemInfo, int quantity, Amount totalPrice) {
        this.itemInfo = itemInfo;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    public int getItemID() {
        return itemInfo.getItemID();
    }
    public String getName() {
        return itemInfo.getName();
    }
    public String getDescription() {
        return itemInfo.getDescription();
    }
    public Amount getUnitPrice() {
        return itemInfo.getUnitPrice();
    }
    public double getVATRate() {
        return itemInfo.getVATRate();
    }
    public int getQuantity() {
        return quantity;
    }
    public Amount getTotalPrice() {
        return totalPrice;
    }
}
