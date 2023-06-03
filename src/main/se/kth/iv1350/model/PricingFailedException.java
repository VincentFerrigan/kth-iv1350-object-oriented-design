package se.kth.iv1350.model;

/**
 * Thrown when pricing failed.
 */
public class PricingFailedException extends RuntimeException {
    /**
     * Creates a new instance representing the condition described in the specified message.
     * @param message A message that describes what went wrong.
     */
    public PricingFailedException(String message) {
        super(message);
    }
    /**
     * Creates a new instance with the specified message and root cause.
     *
     * @param message   The exception message.
     * @param cause The exception that caused this exception.
     */

    public PricingFailedException(String message, Exception cause) {
        super(message, cause);
    }
}