package se.kth.iv1350.integration;

/**
 * Thrown when something fails with inventory system
 */
public class InventorySystemException extends RuntimeException {

    /**
     * Creates an instance with a detailed message of what went wrong.
     * @param message the message is a description of what went wrong
     */
    public InventorySystemException(String message) {
        super(message);
    }
}
