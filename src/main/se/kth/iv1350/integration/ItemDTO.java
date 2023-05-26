package se.kth.iv1350.integration;

import se.kth.iv1350.model.Amount;

import java.util.Objects;

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
    private final int vatGroupCode;

    /**
     * Creates a new instance representing a particular item.
     *
     * @param itemID            Unique itemID
     * @param name              ShoppingCartItem's name
     * @param description       ShoppingCartItem description
     * @param price             Price incl. VAT in {@link Amount}
     * @param vatGroupCode      VAT Group Code
     */
    public ItemDTO(int itemID, String name, String description, Amount price, int vatGroupCode) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.price = new Amount(price);
        this.vatGroupCode = vatGroupCode;
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

    /**
     * Get the items VAT Group Code
     * @return the VAT Group Code
     */
    public int getVATGroupCode() {
        return vatGroupCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ItemDTO)) return false;

        ItemDTO itemDTO = (ItemDTO) o;

        if (itemID != itemDTO.itemID) return false;
        if (!(name.equals(itemDTO.name))) return false;
        if (!(description.equals(itemDTO.description))) return false;
        if (!(price.equals(itemDTO.price))) return false;
        return vatGroupCode == itemDTO.vatGroupCode;
    }
}