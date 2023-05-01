package src.se.kth.iv1350.dto;

import src.se.kth.iv1350.model.Amount;
import src.se.kth.iv1350.model.VAT;

//TODO @Override toString()?
// TODO Move to integration layer?

/**
 * Contains information about one particular item.
 */
public class ItemDTO {
    private final int itemID; // Alt. a String
    private final String name;
    private final String description;
    private final Amount price;
    private final VAT vat; // TODO Bättre namn? OBS! Enligt namn convention.

    /**
     * Creates a new instance representing a particular item.
     *
     * @param itemID            Unique itemID
     * @param name              Item's name
     * @param description       Item description e.g. xxxx // TODO
     * @param price             Price incl. VAT in {@link Amount}
     * @param vat               {@link VAT} (with rate based on VAT Rate Group)
     */
    public ItemDTO(int itemID, String name, String description, Amount price, VAT vat) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.price = new Amount(price);
        this.vat = vat;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Amount getUnitPrice() {
        return price;
    }

    public int getItemID() {
        return itemID;
    }

    // returnera VAT istället för double?
    public double getVATRate() {
        return vat.getVATRate();
    }
}