package src.se.kth.iv1350.dto;

import src.se.kth.iv1350.model.Amount;

//TODO
public class ItemDTO {
    private String name;
    private String description;
    private Amount price;
    private int itemID;
    private double VATRate;

    public ItemDTO(String name, String description, Amount price, int itemID, double VATRate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.itemID = itemID;
        this.VATRate = VATRate;
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

    public int getItemID() {
        return itemID;
    }

    public double getVATRate() {
        return VATRate;
    }
}