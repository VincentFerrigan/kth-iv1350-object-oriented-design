package se.kth.iv1350.model;

import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.dto.ItemDTO;
import se.kth.iv1350.integration.vat.VATFactory;
import se.kth.iv1350.integration.vat.VATStrategy;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * Contains information about a particular item added to the shopping cart. Contains item information,
 * quantity and time of update.
 */
public class ShoppingCartItem {
    private final ItemDTO itemInfo;
    private LocalDateTime timeOfUpdate;
    private int quantity;
    private VATStrategy vatCalculation;

    /**
     * Creates a new instance representing the added item.
     * @param item ShoppingCartItem information as a {@link ItemDTO}
     * @param quantity The quantity
     * @throws OperationFailedException when unable to set up VAT.
     */
    public ShoppingCartItem(ItemDTO item, int quantity) throws OperationFailedException {
        this.timeOfUpdate = LocalDateTime.now();
        this.itemInfo = item;
        this.quantity = quantity;
        try {
            VATFactory vatFactory = VATFactory.getInstance();
            vatCalculation = vatFactory.getDefaultVATStrategy();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException ex) {
            throw new OperationFailedException("Unable to instantiate vat algorithms", ex);
        }
    }
    /**
     * Creates a new instance representing the added item.
     * @param item ShoppingCartItem information as a {@link ItemDTO}
     * @throws OperationFailedException when unable to set up VAT.
     */
    public ShoppingCartItem(ItemDTO item) throws OperationFailedException {
        this(item, 1);
    }

    /**
     * Add quantity to item.
     * @param additionalQuantity the added quantity
     */
    public void addToQuantity(int additionalQuantity){
        this.timeOfUpdate = LocalDateTime.now();
        this.quantity += additionalQuantity;
    }

    /**
     * Get the total price as {@link Amount} i.e. quantity x unit price
     * @return the total price for all the same items.
     */
    public Amount getTotalSubPrice() {
        return getUnitPriceWithVAT().multiply(quantity);
    }

    /**
     * Get the total VAT as {@link Amount}.
     * @return the total VAT
     */
    public Amount calculateTotalSubVATCost() {
        return calculateUnitVATCost().multiply(quantity);
    }

    private Amount calculateUnitVATCost() {
        return vatCalculation.calculateVATForItem(this);
    }

    Amount addVAT(VATStrategy vat) {
        return vat.calculateVATForItem(this);
    }
    public int getVATGroupCode() {
        return itemInfo.getVATGroupCode();
    }

    /**
     * Get time of update
     * @return time of update
     */
    public LocalDateTime getTimeOfUpdate() {
        // TODO: Används ej. Detta då vi inte skickar runt Sale utan snarare SaleDTO
        return timeOfUpdate;
    }

    /**
     * Get item data information as {@link ItemDTO}
     * @return The item data information as {@link ItemDTO}
     */
    public ItemDTO getItemDTO() {
        // TODO: Används ej. Detta då vi inte skickar runt Sale utan snarare SaleDTO
        return itemInfo;
    }

    /**
     * Get the item identifier
     * @return the item identifier
     */
    public int getItemID() {
        return itemInfo.getItemID();
    }

    /**
     * Get the quantity of the particular item.
     * @return quantity of the particular item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Get the unit price with taxes
     * @return the unit price
     */
    public Amount getUnitPriceWithVAT() {
        return getUnitPrice().plus(calculateUnitVATCost());}

    /**
     * Get the unit price without VAT
     * @return the unit price
     */
    public Amount getUnitPrice() { return itemInfo.getUnitPrice(); }

    /**
     * Get the name of the item
     * @return the name of the item
     */
    public String getName() {
        return itemInfo.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ShoppingCartItem)) return false;

        ShoppingCartItem that = (ShoppingCartItem) o;

        if (quantity != that.quantity) return false;
        return itemInfo.equals(that.itemInfo);
    }
}