package src.se.kth.iv1350.dto;

import src.se.kth.iv1350.model.Amount;

public class SaleItemDTO {
    private final ItemDTO itemInfo;
    private final int quantity;
    private final Amount totalPrice;

    public SaleItemDTO(ItemDTO itemInfo, int quantity, Amount totalPrice) {
        this.itemInfo = itemInfo;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return itemInfo.getName();
    }

    public String getDescription() {
        return itemInfo.getDescription();
    }

    public Amount getPrice() {
        return itemInfo.getPrice();
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
