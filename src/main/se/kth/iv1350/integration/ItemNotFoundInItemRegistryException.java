package se.kth.iv1350.integration;

/**
 * Thrown when item does not exist in Inventory System.
 */
public class ItemNotFoundInItemRegistryException extends Exception {
    private int itemIDNotFound;

    /**
     * Creates a new instance including a message and the ID that
     * could not be found.
     * @param itemID the item ID of the item to be found
     */
    public ItemNotFoundInItemRegistryException(int itemID) {
        super("Unable to find item with ID \"%d\" in the inventory system.".formatted(itemID));
        this.itemIDNotFound = itemID;
    }

    public int getItemIDNotFound() {
        return itemIDNotFound;
    }
}