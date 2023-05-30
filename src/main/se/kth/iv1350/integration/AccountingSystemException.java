package se.kth.iv1350.integration;

/**
 * Thrown when something fails with the accounting system
 */
public class AccountingSystemException extends RuntimeException {

    /**
     * Creates a new instance representing the condition described in the specified message.
     * @param message A message that describes what went wrong.
     */
    public AccountingSystemException(String message) {
        super(message);
    }
}
