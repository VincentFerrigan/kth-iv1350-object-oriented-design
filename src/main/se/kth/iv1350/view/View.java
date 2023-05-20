package se.kth.iv1350.view;

import se.kth.iv1350.controller.Controller;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.ItemNotFoundInItemRegistryException;
import se.kth.iv1350.integration.TotalRevenueFileOutput;
import se.kth.iv1350.model.Amount;
import se.kth.iv1350.util.Event;
import se.kth.iv1350.util.ErrorFileLogHandler;
import java.io.IOException;

/**
 * Represents the interface of the program. Since the program does not have
 * an interface or view on its own, this class is a placeholder.
 */
public class View {
    private Controller contr;
    private ErrorMessageHandler errorMessageHandler;
    private ErrorFileLogHandler logger;

    /**
     * Creates a new instance.
     * @param contr The Controller to use for all calls to other layers.
     */
    public View(Controller contr) throws IOException {
        this.contr = contr;
        contr.addSaleObserver(Event.NEW_ITEM, new RunningSaleView());
        contr.addSaleObserver(Event.CHECKED_OUT, new EndOfSaleView());
        contr.addCashRegisterObserver(new TotalRevenueView());
        contr.addCashRegisterObserver(new TotalRevenueFileOutput());
        logger = ErrorFileLogHandler.getInstance();
        errorMessageHandler = ErrorMessageHandler.getInstance();
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
                errorMessageHandler.log("Managed to enter a non-existing item ID.");
            } catch (ItemNotFoundInItemRegistryException ex) {
                errorMessageHandler.log("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
            } catch (OperationFailedException exc) {
                writeToLogAndUI("Wrong exception was thrown.", exc);
            }
            try {
                System.out.println("Temporary lost connection with server, database call failed");
                contr.registerItem(404);
                errorMessageHandler.log("Managed to register item even when database call failed.");
            } catch (ItemNotFoundInItemRegistryException ex) {
                writeToLogAndUI("Wrong exception was thrown.", ex);
            } catch (OperationFailedException ex) {
                writeToLogAndUI("Correctly failed to register item when database call failed", ex);
            }
            contr.registerItem(1);
            contr.registerItem(1, 2);
            contr.endSale();
            payAndWriteCheckoutToUI(new Amount(500));
        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection to inventory system. Try again.");
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
            payAndWriteCheckoutToUI(new Amount(500));
        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection to inventory system. Try again.");
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
            payAndWriteCheckoutToUI(new Amount(500));
        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection to inventory system. Try again.");
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
            payAndWriteCheckoutToUI(new Amount(500));
        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }

    /**
     * Simulates a user input that generates calls to all system operations correctly.
     * With promotion
     */
    public void hardKodadeGrejerWithPromotion() {
        try{
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(7, 3);
            contr.registerItem(1);
            contr.registerItem(1);
            contr.registerItem(3, 10);
            contr.registerItem(5, 9);
            contr.registerItem(10, 4);
            contr.endSale();
            contr.discountRequest(810222);
            contr.endSale();
            payAndWriteCheckoutToUI(new Amount(2000));
        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    private void payAndWriteCheckoutToUI(Amount paidAmount) {
        System.out.println("Paying " + paidAmount);
        contr.pay(paidAmount);
    }
    private void writeToLogAndUI(String uiMsg, Exception exc) {
        errorMessageHandler.log(uiMsg);
        logger.log(exc);
    }
}
