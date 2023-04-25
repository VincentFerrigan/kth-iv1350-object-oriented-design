package src.se.kth.iv1350.dto;

import src.se.kth.iv1350.model.Amount;
import src.se.kth.iv1350.model.VAT;

//TODO ska pris vara inkl. moms?
//TODO @Override toString()
// TODO final?

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
     * @param price             Price incl. VAT
     * @param vatRateGroupCode  The code for the VAT rate group, such as 0, 1, 2 or 3. Where currently
     *                          0 is VAT Exempt e.g. Mus,
     *                          1 is 25 %,
     *                          2 is 12 % and
     *                          3 is 6 %
     */
    public ItemDTO(int itemID, String name, String description, int price, int vatRateGroupCode) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.price = new Amount(price);
        this.vat = new VAT(vatRateGroupCode);
    }

    // TODO based on full constructor without item description
    public ItemDTO(int itemID, String name, int price, int vatRateGroupCode) {
        this(itemID, name, "", price, vatRateGroupCode);
    }

    // TODO based on full constructor without VAT rate group code
    public ItemDTO(int itemID, String name, String description, int price) {
        this(itemID, name, description, price, 1);
    }
    // TODO based on full constructor without item description and VAT rate group code
    public ItemDTO(int itemID, String name, int price) {
        this(itemID, name, "", price, 1);
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
        return vat.getVATRate();
    }
}