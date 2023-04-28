package src.se.kth.iv1350.dto;

import src.se.kth.iv1350.model.Amount;

public class SaleItemDTO {
    private final String name;
    private final String description;
    private final Amount price;
    private final int quantity;
    private final Amount totalPrice;

    public SaleItemDTO(String name, String description, Amount price, int quantity, Amount totalPrice) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Amount getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Amount getTotalPrice() {
        return totalPrice;
    }
}
