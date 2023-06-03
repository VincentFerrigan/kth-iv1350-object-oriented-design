package se.kth.iv1350.integration;

/**
 * Thrown when something fails with the service container {@link RegistryHandler}.
 */
public class RegistryHandlerException extends RuntimeException {
    /**
     * Creates a new instance representing the condition described in the specified message.
     * @param message A message that describes what went wrong.
     */
    RegistryHandlerException(String message) {
        super(message);
    }

    /**
     * Creates a new instance with the specified message and root cause.
     *
     * @param msg   The exception message.
     * @param cause The exception that caused this exception.
     */
    RegistryHandlerException(String msg, Exception cause) {
        super(msg, cause);
    }
}
