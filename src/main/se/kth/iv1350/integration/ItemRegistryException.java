package se.kth.iv1350.integration;

/**
 * Thrown when something fails with the item registry or inventory system
 */
public class ItemRegistryException extends RuntimeException {

    /**
     * Creates a new instance representing the condition described in the specified message.
     * @param message A message that describes what went wrong.
     */
    public ItemRegistryException(String message) {
        super(message);
    }
    /**
     * Creates a new instance with the specified message and root cause.
     *
     * @param msg   The exception message.
     * @param cause The exception that caused this exception.
     */
    ItemRegistryException(String msg, Exception cause) {
        super(msg, cause);
    }
}