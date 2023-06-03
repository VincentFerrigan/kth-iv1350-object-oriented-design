package se.kth.iv1350.integration;

// TODO: kan den vara något slagts base class för dina exceptions?
import java.time.LocalDateTime;

/**
 * Thrown when record does not exist in external System.
 */
public class RecordNotFoundInRegisterException extends Exception {
    private int recordThatCouldNotBeFound;

    /**
     * Creates a new instance including a message and record that
     * could not be found.
     * @param dataItem the item ID of the item to be found
     */
    public RecordNotFoundInRegisterException(int dataItem) {
        super("Unable to find record with ID \"%d\" in Registry".formatted(dataItem));
        this.recordThatCouldNotBeFound = dataItem;
    }

    public int getRecordThatCouldNotBeFound() {
        return recordThatCouldNotBeFound;
    }
}
