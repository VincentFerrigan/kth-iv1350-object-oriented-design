package se.kth.iv1350.dto;

import se.kth.iv1350.model.VAT;
import se.kth.iv1350.model.Amount;

//TODO @Override toString()?
// TODO Move to integration layer?

/**
 * Contains information about one particular item (as a Data Transfer Object).
 */
public class ItemDTO {
    private final int itemID; // Alt. a String
    private final String name;
    private final String description;
    private final Amount price;
    private final VAT vat;

    /**
     * Creates a new instance representing a particular item.
     *
     * @param itemID            Unique itemID
     * @param name              Item's name
     * @param description       Item description
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

    /**
     * Get the item identifier
     * @return the item identifier
     */
    public int getItemID() {
        return itemID;
    }
    /**
     * Get the name of the item
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Get the items description
     * @return the description of the item
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the unit price
     * @return the unit price
     */
    public Amount getUnitPrice() {
        return price;
    }


    // returnera VAT istället för double?

    /**
     * Get the items VAT Rate
     * @return the VAT Rate
     */
    public double getVATRate() {
        return vat.getVATRate();
    }
}