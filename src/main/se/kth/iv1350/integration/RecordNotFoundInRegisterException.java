package se.kth.iv1350.integration;

import java.time.LocalDateTime;

/**
 * Thrown when record does not exist in Accounting System.
 */
public class RecordNotFoundInRegisterException extends Exception {
    private LocalDateTime recordTimeOfUpdate;

    /**
     * Creates a new instance including a message and the time of update that
     * could not be found.
     * @param timeOfUpdate the item ID of the item to be found
     */
    public RecordNotFoundInRegisterException(LocalDateTime timeOfUpdate) {
        super("Unable to find record with time of update \"%s\" in the accounting system.".formatted(timeOfUpdate));
        this.recordTimeOfUpdate = timeOfUpdate;
    }

    public LocalDateTime getRecordNotFound() {
        return recordTimeOfUpdate;
    }
}
