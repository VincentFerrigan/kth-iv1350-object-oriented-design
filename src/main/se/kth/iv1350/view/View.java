package se.kth.iv1350.view;

import se.kth.iv1350.controller.Controller;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.ItemNotFoundInItemRegistryException;
import se.kth.iv1350.integration.TotalRevenueFileOutput;
import se.kth.iv1350.model.Amount;
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
        contr.addSaleObserver(new RunningSaleView());
        contr.addSaleObserver(new EndOfSaleView());
        contr.addCashRegisterObserver(new TotalRevenueView());
        contr.addCashRegisterObserver(new TotalRevenueFileOutput());
        logger = ErrorFileLogHandler.getInstance();
        errorMessageHandler = ErrorMessageHandler.getInstance();
    }

    private void printStep(String step, String stepDescription, int pauseSeconds) throws InterruptedException {
        System.out.println("%-5s %s".formatted(step, stepDescription));
        Thread.sleep(1000 * pauseSeconds);
    }
    private void printStep(String step, String stepDescription) throws InterruptedException {
        printStep(step, stepDescription, 1);
    }
    private void printStep(String stepDescription, int pauseSeconds) throws InterruptedException {
        printStep("",stepDescription,  pauseSeconds);
    }
    private void printStep(String stepDescription) throws InterruptedException {
        printStep("", stepDescription);
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
            printStep("1.", "Customer arrives at POS with goods to purchase.");
            printStep("2.", "Cashier starts a new sale");
            contr.startSale();
            printStep( "3.","Cashier enters item identifier.");
            contr.registerItem(5);
            printStep("4.", "Program retrieves price, VAT (tax) rate,", 0);
            printStep("and item description from the external",0);
            printStep("inventory system. Program records the sold item.", 0);
            printStep("Program also presents item description,", 0);
            printStep("price, and running total (including VAT).");
            printStep("5.", "Steps three and four are repeated until the", 0);
            printStep("cashier has registered all items.\n");
            Thread.sleep(1000);
            printStep( "3.", "Cashier enters item identifier.");
            contr.registerItem(1);
            Thread.sleep(1000);
            printStep( "3","Cashier enters item identifier.");
            contr.registerItem(3);
            Thread.sleep(1000);
            System.out.println("ALTERNATIVE FLOW 3-4A");
            printStep("3-4a.", "No item with the specified identifier is found.\n");

            try {
                printStep( "3a.","Cashier tries to enter a non-existing item ID, ",0);
                printStep("should generate an error.");
                contr.registerItem(150);
                errorMessageHandler.log("Managed to enter a non-existing item ID.");
            } catch (ItemNotFoundInItemRegistryException ex) {
                errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
            } catch (OperationFailedException exc) {
                writeToLogAndUI("Wrong exception was thrown.", exc);
            }
            Thread.sleep(1000);
            System.out.println("ALTERNATIVE FLOW 3-4B");
            printStep("3-4b.", "An item with the specified identifier has", 0);
            printStep("already been entered in the current sale.");
            printStep("I.", "Program increases the sold quantity of the item,",0);
            printStep("and presents item description price,", 0);
            printStep("and running total.\n");
            printStep( "3b.","Cashier enters item identifier.");
            contr.registerItem(5);
            printStep( "3b.","Cashier enters item identifier.");
            contr.registerItem(5);
            Thread.sleep(1000);
            System.out.println("*ALTERNATIVE FLOW 3-4C*");
            printStep("3-4c.", "Customer purchases multiple items of the same", 0);
            printStep("goods (with the same identifier),",0);
            printStep("and cashier registers them together");
            printStep("I.", "Cashier enters item identifier.");
            printStep("II.", "Cashier enters quantity");
            printStep("III.", "Program calculates price, records the sold item", 0);
            printStep("and quantity, and presents item",0);
            printStep("description, price, and running total.\n",2);
            printStep( "3.c","Cashier enters item identifiers with quantities.");
            contr.registerItem(7, 2);
            contr.registerItem(8, 4);
            contr.registerItem(10, 4);
            Thread.sleep(1000);
            try {
                Thread.sleep(1000);
                System.out.println( "* UNCHECKED EXCEPTION HANDLING, RUNTIME-ERROR *");
                printStep( "I.", "Simulating a temporary server connection loss, ",0 );
                printStep( "should generate an error.\n");
                printStep( "3.","Cashier enters item identifier.");
                contr.registerItem(404);
                printStep("");
                errorMessageHandler.log("Managed to register item even when database call \nfailed.");
            } catch (ItemNotFoundInItemRegistryException ex) {
                writeToLogAndUI("Wrong exception was thrown.", ex);
            } catch (OperationFailedException ex) {
                writeToLogAndUI("Correctly failed to register item when database call \nfailed", ex);
            }
            Thread.sleep(1000);
            printStep("II.","Server connection resumed\n");
            printStep( "3", "Cashier enters item identifier.");
            contr.registerItem(9);
            printStep( "3c","Cashier enters item identifier and quantity.");
            contr.registerItem(3, 3);
            printStep("6.", "Cashier asks customer if they want to buy", 0);
            printStep("anything more.");
            printStep("7.", "Customer answers ’no’",0);
            printStep("a ’yes’ answer will be considered later).");
            printStep("8.", "Cashier ends the sale.");
            contr.endSale();
            printStep("9.", "Program presents total price, including VAT.");
            printStep("10.", "Cashier tells customer the total,", 0);
            printStep("and asks for payment.\n");

            Thread.sleep(1000);
            System.out.println("*ALTERNATIVE FLOW 9A*");
            printStep("9a", "(may also be 10a or 11a) ",0);
            printStep("Customer says they are eligible for a discount.");
            printStep("I.", "Cashier signals discount request.");
            printStep("II.", "Cashier enters customer identification.");
            printStep("III.", "Program fetches discount from the discount",0);
            printStep("database");
            contr.registerCustomerToSale(810111);
            contr.endSale();
            printStep("IV.", "Program presents price after discount,",0);
            printStep("based on discount rules.");
            printStep("10a.", "Cashier tells customer the total,", 0);
            printStep("and asks for payment.");
            Amount paidAmount = new Amount(1000);
            printStep("11a.", "Customer pays cash.");
            printStep("Paying " + paidAmount);
            printStep("12.", "Cashier enters amount paid");
            printStep("13.", "Program logs completed sale.",0);
            printStep("14.", "Program sends sale information to ", 0);
            printStep("external accounting system (for accounting)", 0);
            printStep("and external inventory system (to update", 0);
            printStep("inventory).",0);
            printStep("15.", "Program increases the amount present in the ",0);
            printStep("register with the amount paid.",0);
            printStep("16.", "Program prints receipt and tells how much ", 0);
            printStep("change to give customer.",2);
            contr.pay(paidAmount);
            Thread.sleep(2000);
            printStep("17.", "Customer leaves with receipt and goods.",0);
        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
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
            contr.registerCustomerToSale(880822);
            contr.endSale();
            payAndWriteCheckoutToUI(new Amount(500));
        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection with external systems. Try again.");
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
            contr.registerCustomerToSale(810222);
            contr.endSale();
            payAndWriteCheckoutToUI(new Amount(500));
        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection with external systems. Try again.");
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
            errorMessageHandler.log("No connection with external systems. Try again.");
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
            contr.registerCustomerToSale(810111);
            contr.endSale();
            payAndWriteCheckoutToUI(new Amount(2000));
        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, please try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection with external systems. Try again.");
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
