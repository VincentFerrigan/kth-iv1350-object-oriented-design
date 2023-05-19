package se.kth.iv1350.model;

/**
 * Thrown when item does not exist in Shopping Cart.
 */
public class ItemNotFoundInShoppingCartException extends Exception{
    private int itemIDNotFound;

    /**
     * Creates a new instance including a message and what item ID
     * could not be found.
     * @param itemID the item ID of the item to be found
     */
    public ItemNotFoundInShoppingCartException(int itemID) {
        super("Unable to find item with ID \"%d\" in the shopping cart.".formatted(itemID));
        this.itemIDNotFound = itemID;
    }

    public int getItemIDNotFound() {
        return itemIDNotFound;
    }
}
