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
    }


    /**
     * Simulates a user input that generates calls to all system operations,
     * in all possible ways.
     */
    //TODO Make this pretty again
    public void hardKodadeGrejer() throws OperationFailedException, ItemNotFoundException {
        try{
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
        } catch(ItemNotFoundException ex) {
        errorMessageHandler.showErrorMessage(ex.getMessage());
        } catch(OperationFailedException ex) {
            errorMessageHandler.showErrorMessage("No connection to inventory system. Try again.");
        } catch(IllegalArgumentException ex){
            //logger.logException(ex); //TODO add this
            errorMessageHandler.showErrorMessage("The item ID has to be a positive number. Try again.");
        }
        try{
            // SecondSale: with itemID 150 not in inventory -  with member discount, not yet implemented
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(5);
            contr.registerItem(7, 2);
            contr.registerItem(5);
            contr.registerItem(1);
            contr.registerItem(1, 2);
            contr.registerItem(150);
            contr.endSale();
            //        contr.discountRequest(810222);
            contr.endSale();
            contr.pay(new Amount(500));
        } catch(ItemNotFoundException ex) {
            errorMessageHandler.showErrorMessage(ex.getMessage());
        } catch(OperationFailedException ex) {
            errorMessageHandler.showErrorMessage("No connection to inventory system. Try again.");
        } catch(IllegalArgumentException ex){
            //logger.logException(ex); //TODO add this
            errorMessageHandler.showErrorMessage("The item ID has to be a positive number. Try again.");
        }
        try{
            // ThirdSale: with itemID -2 - without discount
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(7, 2);
            contr.registerItem(1);
            contr.registerItem(-2);
            contr.endSale();
            contr.pay(new Amount(500));
        } catch(ItemNotFoundException ex) {
            errorMessageHandler.showErrorMessage(ex.getMessage());
        } catch(OperationFailedException ex) {
            errorMessageHandler.showErrorMessage("No connection to inventory system. Try again.");
        } catch(IllegalArgumentException ex){
            //logger.logException(ex); //TODO add this
            errorMessageHandler.showErrorMessage("The item ID has to be a positive number. Try again.");
        }
        try{
            // FourthSale: all itemID correct - without discount
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(7, 2);
            contr.registerItem(1);
            contr.registerItem(1);
            contr.endSale();
            contr.pay(new Amount(500));
        } catch(ItemNotFoundException ex) {
            errorMessageHandler.showErrorMessage(ex.getMessage());
        } catch(OperationFailedException ex) {
            errorMessageHandler.showErrorMessage("No connection to inventory system. Try again.");
        } catch(IllegalArgumentException ex){
            //logger.logException(ex); //TODO add this
            errorMessageHandler.showErrorMessage("The item ID has to be a positive number. Try again.");
        }
    }
}
