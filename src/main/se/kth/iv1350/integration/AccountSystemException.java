package se.kth.iv1350.integration;

/**
 * Thrown when something fails with the accounting system
 */
public class AccountSystemException extends RuntimeException {

    /**
     * Creates a new instance representing the condition described in the specified message.
     * @param message A message that describes what went wrong.
     */
    public AccountSystemException(String message) {
        super(message);
    }
}
