package se.kth.iv1350.view;

import se.kth.iv1350.controller.Controller;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.ItemNotFoundException;
import se.kth.iv1350.model.Amount;
import se.kth.iv1350.util.LogHandler;
import java.io.IOException;

/**
 * Represents the interface of the program. Since the program does not have
 * an interface or view on its own, this class is a placeholder.
 */
public class View {
    private Controller contr;
    private ErrorMessageHandler errorMessageHandler = new ErrorMessageHandler();
    private LogHandler logger;

    /**
     * Creates a new instance.
     * @param contr The Controller to use for all calls to other layers.
     */
    public View(Controller contr) throws IOException {
        this.contr = contr;
        this.logger = new LogHandler();
    }

    /**
     * Simulates a user input that generates calls to all system operations,
     * including with failures and errors.
     * with itemID 150 to trigger an exception since item is not in inventory system and
     * with itemID 404 to trigger an exception from inventory system.
     * with itemID -2 to trigger an IllegalArgumentException.
     */
    public void hardKodadeGrejerWithFailureAndErrors() {
        try {
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(5);
            contr.registerItem(7, 2);
            contr.registerItem(5);
            try {
                System.out.println("Trying to enter a non-existing item ID, should generate an error.");
                contr.registerItem(150);
                errorMessageHandler.showErrorMessage("Managed to enter a non-existing item ID.");
            } catch (ItemNotFoundException ex) {
                errorMessageHandler.showErrorMessage("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
            } catch (OperationFailedException exc) {
                writeToLogAndUI("Wrong exception was thrown.", exc);
            }
            try {
                System.out.println("Temporary lost connection with server, database call failed");
                contr.registerItem(404);
                errorMessageHandler.showErrorMessage("Managed to register item even when database call failed.");
            } catch (ItemNotFoundException ex) {
                writeToLogAndUI("Wrong exception was thrown.", ex);
            } catch (OperationFailedException ex) {
                writeToLogAndUI("Correctly failed to register item when database call failed", ex);
            }
            contr.registerItem(1);
            contr.registerItem(1, 2);
            contr.endSale();
            contr.pay(new Amount(500));

        } catch (ItemNotFoundException ex) {
            errorMessageHandler.showErrorMessage("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.logException(ex);
            errorMessageHandler.showErrorMessage("No connection to inventory system. Try again.");
        } catch (IllegalArgumentException ex) {
            // TODO Hur hanterar vi denna?
            writeToLogAndUI("The item ID has to be a positive number. Try again.", ex);
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }

    /**
     * Simulates a user input that generates calls to all system operations,
     * This sale will contain a staff discount (Not implemented yet)
     */
    public void HardKodadeGrejerWithStaffDiscount() {
        try {
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(5);
            contr.registerItem(8, 2);
            contr.registerItem(5);
            contr.registerItem(1);
            contr.registerItem(1, 2);
            contr.endSale();
            contr.discountRequest(880822);
            contr.endSale();
            contr.pay(new Amount(500));
        } catch (ItemNotFoundException ex) {
            errorMessageHandler.showErrorMessage("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.logException(ex);
            errorMessageHandler.showErrorMessage("No connection to inventory system. Try again.");
        } catch (IllegalArgumentException ex) {
            // TODO Hur hanterar vi denna?
            writeToLogAndUI("The item ID has to be a positive number. Try again.", ex);
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }

    /**
     * Simulates a user input that generates calls to all system operations,
     * This sale will contain a member discount (Not implemented yet)
     */
    public void hardKodadeGrejerWithMemberDiscount() {
        try {
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(7, 2);
            contr.registerItem(2);
            contr.registerItem(1);
            contr.registerItem(2);
            contr.endSale();
            contr.discountRequest(810222);
            contr.endSale();
            contr.pay(new Amount(500));
        } catch (ItemNotFoundException ex) {
            errorMessageHandler.showErrorMessage("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.logException(ex);
            errorMessageHandler.showErrorMessage("No connection to inventory system. Try again.");
        } catch (IllegalArgumentException ex) {
            // TODO Hur hanterar vi denna?
            writeToLogAndUI("The item ID has to be a positive number. Try again.", ex);
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }

    /**
     * Simulates a user input that generates calls to all system operations correctly.
     * Without discounts
     */
    public void hardKodadeGrejerWithoutDiscount() {
        try{
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(7, 2);
            contr.registerItem(1);
            contr.registerItem(1);
            contr.endSale();
            contr.pay(new Amount(500));
        } catch (ItemNotFoundException ex) {
            errorMessageHandler.showErrorMessage("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.logException(ex);
            errorMessageHandler.showErrorMessage("No connection to inventory system. Try again.");
        } catch (IllegalArgumentException ex) {
            // TODO Hur hanterar vi denna?
            writeToLogAndUI("The item ID has to be a positive number. Try again.", ex);
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }

    private void writeToLogAndUI(String uiMsg, Exception exc) {
        errorMessageHandler.showErrorMessage(uiMsg);
        logger.logException(exc);
    }
}
