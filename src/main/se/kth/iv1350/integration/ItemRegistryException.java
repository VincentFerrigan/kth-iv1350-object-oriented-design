package se.kth.iv1350.integration;

/**
 * Thrown when something fails with inventory system
 */
public class ItemRegistryException extends RuntimeException {

    /**
     * Creates a new instance representing the condition described in the specified message.
     * @param message A message that describes what went wrong.
     */
    public ItemRegistryException(String message) {
        super(message);
    }
}
