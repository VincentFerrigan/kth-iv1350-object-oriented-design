package se.kth.iv1350.integration;

/**
 * Thrown when something fails with the customer registry or customer database
 */
public class CustomerRegistryException extends RuntimeException{

    /**
     * Creates a new instance representing the condition described in the specified message.
     * @param message A message that describes what went wrong.
     */
    public CustomerRegistryException(String message) {
        super(message);
    }
}
