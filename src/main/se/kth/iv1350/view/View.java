package se.kth.iv1350.view;

import se.kth.iv1350.controller.Controller;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.ItemNotFoundException;
import se.kth.iv1350.model.Amount;
import se.kth.iv1350.util.LogHandler;


/**
 * Represents the interface of the program. Since the program does not have
 * an interface or view on its own, this class is a placeholder.
 */
public class View {
    private Controller contr;
    private LogHandler logger;
    private ErrorMessageHandler errorMessageHandler;

    /**
     * Creates a new instance.
     * @param contr The Controller to use for all calls to other layers.
     */
    public View(Controller contr, LogHandler logger) {
        this.contr = contr;
        this.logger = logger;
        this.errorMessageHandler = new ErrorMessageHandler();
    }


    /**
     * Simulates a user input that generates calls to all system operations,
     * with itemID 404 to trigger an exception from inventory system.
     */
    public void hardKodadeGrejer1() {
        try {
            // FirstSale: with itemID 404 for exception handling - with staff discount, not yet implemented
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(5);
            contr.registerItem(404);
            contr.registerItem(8, 2);
            contr.registerItem(5);
            contr.registerItem(1);
            contr.registerItem(1, 2);
            contr.endSale();
            //        contr.discountRequest(880822);
            contr.endSale();
            contr.pay(new Amount(500));
        } catch (ItemNotFoundException ex) { //TODO Loggas?
            errorMessageHandler.showErrorMessage(ex.getMessage());
        } catch (OperationFailedException ex) {
            logger.logException(ex);
            errorMessageHandler.showErrorMessage("No connection to inventory system. Try again.");
        } catch (IllegalArgumentException ex) {
            logger.logException(ex); //TODO loggas, really?
            errorMessageHandler.showErrorMessage("The item ID has to be a positive number. Try again.");
        }
    }

    /**
     * Simulates a user input that generates calls to all system operations,
     * with itemID 150 to trigger an exception since item is not in inventory system.
     */
    public void hardKodadeGrejer2() {
        try {
            // SecondSale: with itemID 150 not in inventory -  with member discount, not yet implemented
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(5);
            contr.registerItem(7, 2);
            contr.registerItem(5);
            contr.registerItem(150);
            contr.registerItem(1);
            contr.registerItem(1, 2);
            contr.endSale();
            //        contr.discountRequest(810222);
            contr.endSale();
            contr.pay(new Amount(500));
        } catch (ItemNotFoundException ex) { //TODO Loggas?
            errorMessageHandler.showErrorMessage(ex.getMessage());
        } catch (OperationFailedException ex) {
            logger.logException(ex);
            errorMessageHandler.showErrorMessage("No connection to inventory system. Try again.");
        } catch (IllegalArgumentException ex) {
            logger.logException(ex); //TODO loggas, really?
            errorMessageHandler.showErrorMessage("The item ID has to be a positive number. Try again.");
        }
    }

    /**
     * Simulates a user input that generates calls to all system operations,
     * with itemID -2 to trigger an IllegalArgumentException.
     */
    public void hardKodadeGrejer3() {
        try {
            // ThirdSale: with itemID -2 - without discount
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(7, 2);
            contr.registerItem(-2);
            contr.registerItem(1);
            contr.registerItem(-2);
            contr.endSale();
            contr.pay(new Amount(500));
        } catch (ItemNotFoundException ex) { //TODO Loggas?
            errorMessageHandler.showErrorMessage(ex.getMessage());
        } catch (OperationFailedException ex) {
            logger.logException(ex);
            errorMessageHandler.showErrorMessage("No connection to inventory system. Try again.");
        } catch (IllegalArgumentException ex) {
            logger.logException(ex); //TODO loggas, really?
            errorMessageHandler.showErrorMessage("The item ID has to be a positive number. Try again.");
        }
    }

    /**
     * Simulates a user input that generates calls to all system operations correctly.
     */
    public void hardKodadeGrejer4() {
        try{
            // FourthSale: all itemID correct - without discount
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(7, 2);
            contr.registerItem(1);
            contr.registerItem(1);
            contr.endSale();
            contr.pay(new Amount(500));
        } catch (ItemNotFoundException ex) { //TODO Loggas?
            errorMessageHandler.showErrorMessage(ex.getMessage());
        } catch (OperationFailedException ex) {
            logger.logException(ex);
            errorMessageHandler.showErrorMessage("No connection to inventory system. Try again.");
        } catch (IllegalArgumentException ex) {
            logger.logException(ex); //TODO loggas, really?
            errorMessageHandler.showErrorMessage("The item ID has to be a positive number. Try again.");
        }
    }
}
