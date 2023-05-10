package se.kth.iv1350.integration;

/**
 * Thrown when item does not exist in Inventory System.
 */
public class ItemNotFoundException extends Exception {

    /**
     * Creates a new instance including a message and what item ID
     * could not be found.
     * @param itemID the item ID of the item to be found
     */
    public ItemNotFoundException(int itemID) {
        super("There is no item with ID \"%d\" in the inventory system.".formatted(itemID));
    }
}