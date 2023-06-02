package se.kth.iv1350.view;

import se.kth.iv1350.controller.Controller;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.ItemNotFoundInItemRegistryException;
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
    private final ErrorFileLogHandler logger;

    /**
     * Creates a new instance.
     * @param contr The Controller to use for all calls to other layers.
     */
    public View(Controller contr) throws IOException {
        this.contr = contr;
        contr.addSaleObserver(new RunningSaleView());
        contr.addSaleObserver(new EndOfSaleView());
        contr.addCashRegisterObserver(new TotalRevenueView());
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
    public void basicFlow() {
        System.out.println("BASIC FLOW");
        try {
            printStep("1.", "Customer arrives at POS with goods to purchase.\n");
            printStep("2.", "Cashier starts a new sale\n");
            contr.startSale();
            printStep( "3.","Cashier enters item identifier.");
            contr.registerItem(5);
            Thread.sleep(1000);
            printStep("4.", "Program retrieves price, VAT (tax) rate,", 0);
            printStep("and item description from the external",0);
            printStep("inventory system. Program records the sold item.", 0);
            printStep("Program also presents item description,", 0);
            printStep("price, and running total (including VAT).\n");
            printStep("5.", "Steps three and four are repeated until the", 0);
            printStep("cashier has registered all items.\n");
            contr.registerItem(1);
            Thread.sleep(1000);
            contr.registerItem(3);
            Thread.sleep(1000);
            contr.registerItem(9);
            Thread.sleep(1000);

            printStep("6.", "Cashier asks customer if they want to buy", 0);
            printStep("anything more.\n");
            printStep("7.", "Customer answers ’no’",0);
            printStep("a ’yes’ answer will be considered later).\n");
            printStep("8.", "Cashier ends the sale.");
            contr.endSale();
            printStep("9.", "Program presents total price, including VAT.\n");
            printStep("10.", "Cashier tells customer the total,", 0);
            printStep("and asks for payment.\n");
            Amount paidAmount = new Amount(200);
            printStep("11.", "Customer pays cash.\n");
            printStep("Paying " + paidAmount + "\n");
            printStep("12.", "Cashier enters amount paid\n");
            printStep("13.", "Program logs completed sale.\n");
            printStep("14.", "Program sends sale information to ", 0);
            printStep("external accounting system (for accounting)", 0);
            printStep("and external inventory system (to update", 0);
            printStep("inventory).\n");
            printStep("15.", "Program increases the amount present in the ",0);
            printStep("register with the amount paid.\n");
            printStep("16.", "Program prints receipt and tells how much ", 0);
            printStep("change to give customer.\n");

            Thread.sleep(2000);
            clearConsole();
            contr.pay(paidAmount);
            Thread.sleep(2000);
            printStep("17.", "Customer leaves with receipt and goods.",0);
            Thread.sleep(5000);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    public void alternativeFlow3AWithCheckedExceptions() {
        System.out.println("ALTERNATIVE FLOW 3-4A");
        try {
            printStep("1.", "Customer arrives at POS with goods to purchase.\n");
            printStep("2.", "Cashier starts a new sale\n");
            contr.startSale();
            printStep( "3.","Cashier enters item identifier.");
            contr.registerItem(5);
            Thread.sleep(1000);
            printStep("4.", "Program retrieves price, VAT (tax) rate,", 0);
            printStep("and item description from the external",0);
            printStep("inventory system. Program records the sold item.", 0);
            printStep("Program also presents item description,", 0);
            printStep("price, and running total (including VAT).\n");
            printStep("5.", "Steps three and four are repeated until the", 0);
            printStep("cashier has registered all items.\n");
            System.out.println( "CHECKED EXCEPTION, BUSINESS LOGIC ERRORS");
            printStep("3-4a.", "No item with the specified identifier is found.\n");
            try {
                printStep( "3a.","Cashier tries to enter a non-existing item ID, ",0);
                printStep("should generate an error.");
                Thread.sleep(1000);
                contr.registerItem(150);
                Thread.sleep(1000);
                Thread.sleep(1000);
                errorMessageHandler.log("Managed to enter a non-existing item ID.");
            } catch (ItemNotFoundInItemRegistryException ex) {
                errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
            } catch (OperationFailedException exc) {
                writeToLogAndUI("Wrong exception was thrown.", exc);
            }
            Thread.sleep(1000);
            printStep( "3.","Cashier enters item identifier.");
            contr.registerItem(3);
            Thread.sleep(1000);
            printStep( "3b", "Cashier enters item identifier.");
            contr.registerItem(3);
            Thread.sleep(1000);
            printStep( "3c","Cashier enters item identifier and quantity.");
            contr.registerItem(2, 3);
            Thread.sleep(1000);
            printStep("6.", "Cashier asks customer if they want to buy", 0);
            printStep("anything more.\n");
            printStep("7.", "Customer answers ’no’",0);
            printStep("a ’yes’ answer will be considered later).\n");
            printStep("8.", "Cashier ends the sale.");
            contr.endSale();
            printStep("9.", "Program presents total price, including VAT.\n");
            printStep("10.", "Cashier tells customer the total,", 0);
            printStep("and asks for payment.\n");
            Amount paidAmount = new Amount(500);
            printStep("11.", "Customer pays cash.\n");
            printStep("Paying " + paidAmount + "\n");
            printStep("12.", "Cashier enters amount paid\n");
            printStep("13.", "Program logs completed sale.\n");
            printStep("14.", "Program sends sale information to ", 0);
            printStep("external accounting system (for accounting)", 0);
            printStep("and external inventory system (to update", 0);
            printStep("inventory).\n");
            printStep("15.", "Program increases the amount present in the ",0);
            printStep("register with the amount paid.\n");
            printStep("16.", "Program prints receipt and tells how much ", 0);
            printStep("change to give customer.\n");

            Thread.sleep(2000);
            clearConsole();
            contr.pay(paidAmount);
            Thread.sleep(2000);
            printStep("17.", "Customer leaves with receipt and goods.",0);
            Thread.sleep(5000);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }

    public void alternativeFlow3B() {
        System.out.println("ALTERNATIVE FLOW 3-4B");
        try {
            printStep("1.", "Customer arrives at POS with goods to purchase.\n");
            printStep("2.", "Cashier starts a new sale\n");
            contr.startSale();
            printStep("3-4b.", "An item with the specified identifier has", 0);
            printStep("already been entered in the current sale.\n");
            printStep("Program increases the sold quantity of the item,",0);
            printStep("and presents item description price,", 0);
            printStep("and running total.\n");
            printStep( "3b.","Cashier enters item identifier.");
            contr.registerItem(3);
            printStep( "3b.","Cashier enters the same item identifier.");
            contr.registerItem(3);
            Thread.sleep(1000);
            Thread.sleep(1000);
            printStep("4.", "Program retrieves price, VAT (tax) rate,", 0);
            printStep("and item description from the external",0);
            printStep("inventory system. Program records the sold item.", 0);
            printStep("Program also presents item description,", 0);
            printStep("price, and running total (including VAT).\n");
            printStep("5.", "Steps three and four are repeated until the", 0);
            printStep("cashier has registered all items.\n");
            contr.registerItem(1);
            Thread.sleep(1000);
            contr.registerItem(1);
            Thread.sleep(1000);
            contr.registerItem(9);
            Thread.sleep(1000);
            contr.registerItem(9);
            Thread.sleep(1000);
            printStep("6.", "Cashier asks customer if they want to buy", 0);
            printStep("anything more.\n");
            printStep("7.", "Customer answers ’no’",0);
            printStep("a ’yes’ answer will be considered later).\n");
            printStep("8.", "Cashier ends the sale.");
            contr.endSale();
            printStep("9.", "Program presents total price, including VAT.\n");
            printStep("10.", "Cashier tells customer the total,", 0);
            printStep("and asks for payment.\n");
            Amount paidAmount = new Amount(200);
            printStep("11.", "Customer pays cash.\n");
            printStep("Paying " + paidAmount + "\n");
            printStep("12.", "Cashier enters amount paid\n");
            printStep("13.", "Program logs completed sale.\n");
            printStep("14.", "Program sends sale information to ", 0);
            printStep("external accounting system (for accounting)", 0);
            printStep("and external inventory system (to update", 0);
            printStep("inventory).\n");
            printStep("15.", "Program increases the amount present in the ",0);
            printStep("register with the amount paid.\n");
            printStep("16.", "Program prints receipt and tells how much ", 0);
            printStep("change to give customer.\n");

            Thread.sleep(2000);
            clearConsole();
            contr.pay(paidAmount);
            Thread.sleep(2000);
            printStep("17.", "Customer leaves with receipt and goods.",0);
            Thread.sleep(5000);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }

    public void alternativeFlow3C() {
        System.out.println("ALTERNATIVE FLOW 3-4C");
        try {
            printStep("1.", "Customer arrives at POS with goods to purchase.\n");
            printStep("2.", "Cashier starts a new sale\n");
            contr.startSale();
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
            contr.registerItem(8, 2);
            Thread.sleep(1000);
            contr.registerItem(8, 4);
            Thread.sleep(1000);
            contr.registerItem(10, 4);
            Thread.sleep(1000);
            printStep("4.", "Program retrieves price, VAT (tax) rate,", 0);
            printStep("and item description from the external",0);
            printStep("inventory system. Program records the sold item.", 0);
            printStep("Program also presents item description,", 0);
            printStep("price, and running total (including VAT).\n");
            printStep("5.", "Steps three and four are repeated until the", 0);
            printStep("cashier has registered all items.\n");
            printStep("6.", "Cashier asks customer if they want to buy", 0);
            printStep("anything more.\n");
            printStep("7.", "Customer answers ’no’",0);
            printStep("a ’yes’ answer will be considered later).\n");
            printStep("8.", "Cashier ends the sale.");
            contr.endSale();
            printStep("9.", "Program presents total price, including VAT.\n");
            Thread.sleep(1000);
            printStep("10.", "Cashier tells customer the total,", 0);
            printStep("and asks for payment.\n");
            Amount paidAmount = new Amount(1000);
            printStep("11.", "Customer pays cash.");
            printStep("Paying " + paidAmount + "\n");
            printStep("12.", "Cashier enters amount paid.\n");
            printStep("13.", "Program logs completed sale. \n");
            printStep("14.", "Program sends sale information to ", 0);
            printStep("external accounting system (for accounting)", 0);
            printStep("and external inventory system (to update", 0);
            printStep("inventory). \n");
            printStep("15.", "Program increases the amount present in the ",0);
            printStep("register with the amount paid. \n");
            printStep("16.", "Program prints receipt and tells how much ", 0);
            printStep("change to give customer. \n");

            Thread.sleep(2000);
            clearConsole();
            contr.pay(paidAmount);
            Thread.sleep(2000);
            printStep("17.", "Customer leaves with receipt and goods.",0);
            Thread.sleep(5000);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    public void alternativeFlow9a() {
        System.out.println("ALTERNATIVE FLOW 9A");
        try {
            printStep("1.", "Customer arrives at POS with goods to purchase.\n");
            printStep("2.", "Cashier starts a new sale\n");
            printStep( "3.c","Cashier enters item identifiers with quantities.");
            contr.registerItem(8, 2);
            Thread.sleep(1000);
            contr.registerItem(8, 4);
            Thread.sleep(1000);
            contr.registerItem(10, 4);
            Thread.sleep(1000);
            printStep("4.", "Program retrieves price, VAT (tax) rate,", 0);
            printStep("and item description from the external",0);
            printStep("inventory system. Program records the sold item.", 0);
            printStep("Program also presents item description,", 0);
            printStep("price, and running total (including VAT).\n");
            printStep("5.", "Steps three and four are repeated until the", 0);
            printStep("cashier has registered all items.\n");
            printStep("6.", "Cashier asks customer if they want to buy", 0);
            printStep("anything more.\n");
            printStep("7.", "Customer answers ’no’",0);
            printStep("a ’yes’ answer will be considered later).\n");
            printStep("8.", "Cashier ends the sale.");
            contr.endSale();
            Thread.sleep(1000);

            printStep("9a", "(may also be 10a or 11a) ",0);
            printStep("Customer says they are eligible for a discount.");
            printStep("I.", "Cashier signals discount request.");
            printStep("II.", "Cashier enters customer identification.");
            printStep("III.", "Program fetches discount from the discount",0);
            printStep("database");
            printStep("IV.", "Program presents price after discount,",0);
            printStep("based on discount rules.\n");
            contr.registerCustomerToSale(810111);
            contr.endSale();
            Thread.sleep(1000);
            printStep("10.", "Cashier tells customer the total,", 0);
            printStep("and asks for payment.\n");
            Amount paidAmount = new Amount(1000);
            printStep("11.", "Customer pays cash.");
            printStep("Paying " + paidAmount + "\n");
            printStep("12.", "Cashier enters amount paid.\n");
            printStep("13.", "Program logs completed sale. \n");
            printStep("14.", "Program sends sale information to ", 0);
            printStep("external accounting system (for accounting)", 0);
            printStep("and external inventory system (to update", 0);
            printStep("inventory). \n");
            printStep("15.", "Program increases the amount present in the ",0);
            printStep("register with the amount paid. \n");
            printStep("16.", "Program prints receipt and tells how much ", 0);
            printStep("change to give customer. \n");

            Thread.sleep(2000);
            clearConsole();
            contr.pay(paidAmount);
            Thread.sleep(2000);
            printStep("17.", "Customer leaves with receipt and goods.",0);
            Thread.sleep(5000);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    public void basicFlowWithUnCheckedExceptions() {
        System.out.println("BASIC FLOW WITH UNCHECKED EXCEPTION");
        try {
            printStep("1.", "Customer arrives at POS with goods to purchase.\n");
            printStep("2.", "Cashier starts a new sale\n");
            contr.startSale();
            printStep( "3.","Cashier enters item identifier.");
            contr.registerItem(5);
            Thread.sleep(1000);
            printStep("4.", "Program retrieves price, VAT (tax) rate,", 0);
            printStep("and item description from the external",0);
            printStep("inventory system. Program records the sold item.", 0);
            printStep("Program also presents item description,", 0);
            printStep("price, and running total (including VAT).\n");
            printStep("5.", "Steps three and four are repeated until the", 0);
            printStep("cashier has registered all items.\n");
            try {
                System.out.println( "UNCHECKED EXCEPTION, RUNTIME-ERROR");
                printStep( "I.", "Simulating a temporary server connection loss, ",0 );
                printStep( "should generate an error.\n");
                printStep( "3.","Cashier enters item identifier.");
                Thread.sleep(1000);
                contr.registerItem(404);
                printStep("");
                errorMessageHandler.log("Managed to register item even when database call \nfailed.");
            } catch (ItemNotFoundInItemRegistryException ex) {
                writeToLogAndUI("Wrong exception was thrown.", ex);
            } catch (OperationFailedException ex) {
                writeToLogAndUI("Correctly failed to register item when database call \nfailed", ex);
            }
            Thread.sleep(1000);
            Thread.sleep(1000);
            printStep("II.","Server connection resumed\n");
            Thread.sleep(1000);
            printStep( "3b", "Cashier enters item identifier.");
            contr.registerItem(3);
            Thread.sleep(1000);
            printStep( "3c","Cashier enters item identifier and quantity.");
            contr.registerItem(2, 3);
            Thread.sleep(1000);
            printStep("6.", "Cashier asks customer if they want to buy", 0);
            printStep("anything more.\n");
            printStep("7.", "Customer answers ’no’",0);
            printStep("a ’yes’ answer will be considered later).\n");
            printStep("8.", "Cashier ends the sale.");
            contr.endSale();
            printStep("9.", "Program presents total price, including VAT.\n");
            printStep("10.", "Cashier tells customer the total,", 0);
            printStep("and asks for payment.\n");
            Amount paidAmount = new Amount(500);
            printStep("11.", "Customer pays cash.\n");
            printStep("Paying " + paidAmount + "\n");
            printStep("12.", "Cashier enters amount paid\n");
            printStep("13.", "Program logs completed sale.\n");
            printStep("14.", "Program sends sale information to ", 0);
            printStep("external accounting system (for accounting)", 0);
            printStep("and external inventory system (to update", 0);
            printStep("inventory).\n");
            printStep("15.", "Program increases the amount present in the ",0);
            printStep("register with the amount paid.\n");
            printStep("16.", "Program prints receipt and tells how much ", 0);
            printStep("change to give customer.\n");

            Thread.sleep(2000);
            clearConsole();
            contr.pay(paidAmount);
            Thread.sleep(2000);
            printStep("17.", "Customer leaves with receipt and goods.",0);
            Thread.sleep(5000);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            logger.log(ex);
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    private final static void clearConsole()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void writeToLogAndUI(String uiMsg, Exception exc) {
        errorMessageHandler.log(uiMsg);
        logger.log(exc);
    }
}
