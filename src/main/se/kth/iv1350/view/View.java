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

    /**
     * Simulates a user input that generates calls to all system operations.
     * Registers item 5,1,3,9.
     * Customer pays 200 kr in Cash.
     */
    public void basicFlow() {
        try {
            contr.startSale();
            contr.registerItem(5);
            contr.registerItem(1);
            contr.registerItem(3);
            contr.registerItem(9);

            contr.endSale();

            Amount paidAmount = new Amount(200);
            contr.pay(paidAmount);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    /**
     * Simulates a user input that generates calls to all system operations,
     * with an alternative flow - checked exception, business logic error.
     * Item ID 150 triggers an exception since item is not in inventory system.
     *
     * Registers item 5,3,4,2.
     * Customer pays 500 kr in Cash.
     */
    public void alternativeFlow3AWithCheckedExceptions() {
        try {
            contr.startSale();
            contr.registerItem(5);
            try {
                contr.registerItem(150);
                errorMessageHandler.log("Managed to enter a non-existing item ID.");
            } catch (ItemNotFoundInItemRegistryException ex) {
                errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
            } catch (OperationFailedException exc) {
                writeToLogAndUI("Wrong exception was thrown.", exc);
            }
            contr.registerItem(3);
            contr.registerItem(4);
            contr.registerItem(2 );

            contr.endSale();

            Amount paidAmount = new Amount(500);
            contr.pay(paidAmount);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }

    /**
     * Simulates a user input that generates calls to all system operations,
     * with an alternative flow - same item id.
     *
     * Registers item 3,3,1,1,9,9,9,9,9
     * Customer pays 240 kr in Cash.
     */
    public void alternativeFlow3B() {
        try {
            contr.startSale();
            contr.registerItem(3);
            contr.registerItem(3);
            contr.registerItem(1);
            contr.registerItem(1);
            contr.registerItem(9);
            contr.registerItem(9);
            contr.registerItem(9);
            contr.registerItem(9);
            contr.registerItem(9);

            contr.endSale();

            Amount paidAmount = new Amount(240);
            contr.pay(paidAmount);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception ex) {
            writeToLogAndUI("Failed to register sale, please try again.", ex);
        }
    }

    /**
     * Simulates a user input that generates calls to all system operations,
     * with an alternative flow - multiple items of the same goods.
     *
     * Registers item 2 x id 2, 4 x id 8, 4 x id 7 and 10 x id 4
     * Customer pays 1000 kr in Cash.
     */
    public void alternativeFlow3C() {
        try {
            contr.startSale();
            contr.registerItem(2, 2);
            contr.registerItem(8, 2);
            contr.registerItem(7, 4);
            contr.registerItem(10, 4);

            contr.endSale();

            Amount paidAmount = new Amount(1000);
            contr.pay(paidAmount);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    /**
     * Simulates a user input that generates calls to all system operations,
     * with an alternative flow, customer eligible for discount.
     */
    public void alternativeFlow9a() {
        try {
            contr.startSale();
            contr.registerItem(8, 2);
            contr.registerItem(8, 4);
            contr.registerItem(10, 4);

            contr.endSale();
            contr.registerCustomerToSale(810111);
            contr.endSale();

            Amount paidAmount = new Amount(1000);
            contr.pay(paidAmount);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    /**
     * Simulates a user input that generates calls to all system operations,
     * with an alternative flow -- unchecked exception.
     * Item ID 404 to trigger an exception from inventory system.
     *
     * Registers item 5, 5, 3 x id 2
     * Customer pays 500 kr in Cash.
     */
    public void alternativeFlowWithUnCheckedExceptions() {
        try {
            contr.startSale();
            contr.registerItem(5);
            try {
                contr.registerItem(404);
                errorMessageHandler.log("Managed to register item even when database call \nfailed.");
            } catch (ItemNotFoundInItemRegistryException ex) {
                writeToLogAndUI("Wrong exception was thrown.", ex);
            } catch (OperationFailedException ex) {
                writeToLogAndUI("Correctly failed to register item when database call \nfailed", ex);
            }
            contr.registerItem(5);
            contr.registerItem(2, 3);

            contr.endSale();

            Amount paidAmount = new Amount(500);
            contr.pay(paidAmount);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    /**
     * Simulates a user input that generates calls to all system operations.
     *
     * Same as {@link #basicFlow()} but with descriptive steps
     * of the POS process, for demonstration purposes.
     */
    public void basicFlowWithSteps() {
        System.out.println("BASIC FLOW");
        try {
            step1To2();

            printStep( "3.","Cashier enters item identifier.");
            contr.registerItem(5);
            Thread.sleep(1000);
            printStep("4.", "Program retrieves price, VAT (tax) rate,", 0);
            printStep("and item description from the external",0);
            printStep("inventory system. Program records the sold item.", 0);
            printStep("Program also presents item description,", 0);
            printStep("price, and running total (including VAT).\n");

            step5();
            Thread.sleep(1000);

            clearConsole();
            printStep( "3.","Cashier enters item identifier.",0);
            contr.registerItem(1);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3.","Cashier enters item identifier.",0);
            contr.registerItem(3);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3.","Cashier enters item identifier.",0);
            contr.registerItem(9);
            Thread.sleep(1000);

            step6To8();

            printStep("9.", "Program presents total price, including VAT.\n");

            step10To17(200);
        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    /**
     * Simulates a user input that generates calls to all system operations,
     * with an alternative flow - checked exception, business logic error.
     * Item ID 150 triggers an exception since item is not in inventory system.
     *
     * Same as {@link #alternativeFlow3AWithCheckedExceptions()} but with descriptive steps
     * of the POS process, for demonstration purposes.
     */
    public void alternativeFlow3AWithCheckedExceptionsWithSteps() {
        clearConsole();
        System.out.println( "ALTERNATIVE FLOW - CHECKED EXCEPTION, BUSINESS LOGIC ERROR");
        try {
            step1To2();

            printStep( "3.","Cashier enters item identifier.");
            contr.registerItem(5);
            Thread.sleep(1000);
            printStep("4.", "Program retrieves price, VAT (tax) rate,", 0);
            printStep("and item description from the external",0);
            printStep("inventory system. Program records the sold item.", 0);
            printStep("Program also presents item description,", 0);
            printStep("price, and running total (including VAT).\n");

            step5();
            Thread.sleep(1000);

            clearConsole();
            System.out.println("ALTERNATIVE FLOW 3-4A");
            printStep("3-4a.", "No item with the specified identifier is found.\n");
            try {
                printStep( "3a.","Cashier tries to enter a non-existing item ID, ",0);
                printStep("should generate an error.");
                Thread.sleep(1000);

                contr.registerItem(150);
                errorMessageHandler.log("Managed to enter a non-existing item ID.");
            } catch (ItemNotFoundInItemRegistryException ex) {
                errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
            } catch (OperationFailedException exc) {
                writeToLogAndUI("Wrong exception was thrown.", exc);
            }
            Thread.sleep(2000);

            clearConsole();
            printStep( "3a.","Cashier enters correct item identifier.", 0);
            contr.registerItem(3);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3", "Cashier enters item identifier.", 0);
            contr.registerItem(4);
            Thread.sleep(1000);

            try {
                clearConsole();
                printStep( "3a.","Cashier tries to enter a non-existing item ID, ",0);
                printStep("should generate an error.");
                Thread.sleep(1000);

                contr.registerItem(150);
                errorMessageHandler.log("Managed to enter a non-existing item ID.");
            } catch (ItemNotFoundInItemRegistryException ex) {
                errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
            } catch (OperationFailedException exc) {
                writeToLogAndUI("Wrong exception was thrown.", exc);
            }
            Thread.sleep(2000);

            clearConsole();
            printStep( "3", "Cashier enters correct item identifier.", 0);
            contr.registerItem(2 );
            Thread.sleep(1000);

            step6To8();

            printStep("9.", "Program presents total price, including VAT.\n");

            step10To17(500);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }

    /**
     * Simulates a user input that generates calls to all system operations,
     * with an alternative flow - same item id.
     *
     * Same as {@link #alternativeFlow3B()} but with descriptive steps
     * of the POS process, for demonstration purposes.
     */
    public void alternativeFlow3BWithSteps() {
        System.out.println("ALTERNATIVE FLOW - SAME ITEM ID");
        try {
            step1To2();

            System.out.println("ALTERNATIVE FLOW 3-4B");
            printStep( "3b.","Cashier enters item identifier.");
            contr.registerItem(3);
            Thread.sleep(1000);

            printStep("3-4b.", "An item with the specified identifier has", 0);
            printStep("already been entered in the current sale.\n");
            printStep("Program increases the sold quantity of the item,",0);
            printStep("and presents item description price,", 0);
            printStep("and running total.\n");

            contr.registerItem(3);
            Thread.sleep(1000);

            step5();
            Thread.sleep(1000);

            clearConsole();
            printStep( "3b.","Cashier enters item identifier.", 0);
            contr.registerItem(1);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3b.","Cashier enters the same item identifier.", 0);
            contr.registerItem(1);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3b.","Cashier enters item identifier.", 0);
            contr.registerItem(9);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3b.","Cashier enters the same item identifier.",0);
            contr.registerItem(9);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3b.","Cashier enters the same item identifier.",0);
            contr.registerItem(9);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3b.","Cashier enters the same item identifier.",0);
            contr.registerItem(9);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3b.","Cashier enters the same item identifier.",0);
            contr.registerItem(9);
            Thread.sleep(1000);

            step6To8();

            printStep("9.", "Program presents total price, including VAT.\n");

            step10To17(240);
        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception ex) {
            writeToLogAndUI("Failed to register sale, please try again.", ex);
        }
    }

    /**
     * Simulates a user input that generates calls to all system operations,
     * with an alternative flow - multiple items of the same goods.
     *
     * Same as {@link #alternativeFlow3C()} but with descriptive steps
     * of the POS process, for demonstration purposes.
     */
    public void alternativeFlow3CWithSteps() {
        System.out.println("ALTERNATIVE FLOW - MULTIPLE ITEMS OF THE SAME GOODS");
        try {
            step1To2();
            System.out.println("ALTERNATIVE FLOW 3-4C");
            printStep("3-4c.", "Customer purchases multiple items of the same", 0);
            printStep("goods (with the same identifier),",0);
            printStep("and cashier registers them together");
            printStep("I.", "Cashier enters item identifier.");
            printStep("II.", "Cashier enters quantity");
            printStep("III.", "Program calculates price, records the sold item", 0);
            printStep("and quantity, and presents item",0);
            printStep("description, price, and running total.\n",2);

            contr.registerItem(2, 2);
            Thread.sleep(1000);

            step5();
            Thread.sleep(1000);

            clearConsole();
            printStep( "3.c","Cashier enters item identifiers with quantities.", 0);
            contr.registerItem(8, 2);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3.c","Cashier enters item identifiers with quantities.", 0);
            contr.registerItem(7, 4);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3.c","Cashier enters item identifiers with quantities.", 0);
            contr.registerItem(10, 4);
            Thread.sleep(1000);

            step6To8();

            printStep("9.", "Program presents total price, including VAT.\n");

            step10To17(1000);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    /**
     * Simulates a user input that generates calls to all system operations,
     * with an alternative flow, customer eligible for discount.
     *
     * Same as {@link #alternativeFlow9a()} but with descriptive steps
     * of the POS process, for demonstration purposes.
     */
    public void alternativeFlow9aWithSteps() {
        System.out.println("ALTERNATIVE FLOW, CUSTOMER ELIGIBLE FOR DISCOUNT");
        try {
            step1To2();

            printStep( "3.c","Cashier enters item identifiers with quantities.", 0);
            printStep( "Program calculates price, records the sold item", 0);
            printStep("and quantity, and presents item");
            contr.registerItem(8, 2);
            Thread.sleep(1000);

            step5();
            Thread.sleep(1000);

            clearConsole();
            printStep( "3.c","Cashier enters item identifiers with quantities.", 0);
            contr.registerItem(8, 4);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3.c","Cashier enters item identifiers with quantities.", 0);
            contr.registerItem(10, 4);
            Thread.sleep(1000);

            step6To8();

            System.out.println("ALTERNATIVE FLOW 9A");
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
            clearConsole();
            contr.endSale();
            Thread.sleep(1000);

            step10To17(1000);

        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    /**
     * Simulates a user input that generates calls to all system operations,
     * with an alternative flow -- unchecked exception.
     * Item ID 404 to trigger an exception from inventory system.
     *
     * Same as {@link #alternativeFlowWithUnCheckedExceptions()} but with descriptive steps
     * of the POS process, for demonstration purposes.
     */
    public void basicFlowWithUnCheckedExceptionsWithSteps() {
        System.out.println("ALTERNATIVE FLOW - UNCHECKED EXCEPTION");
        try {
            step1To2();

            printStep( "3.","Cashier enters item identifier.");
            contr.registerItem(5);
            Thread.sleep(1000);
            printStep("4.", "Program retrieves price, VAT (tax) rate,", 0);
            printStep("and item description from the external",0);
            printStep("inventory system. Program records the sold item.", 0);
            printStep("Program also presents item description,", 0);
            printStep("price, and running total (including VAT).\n");

            step5();

            try {
                System.out.println( "UNCHECKED EXCEPTION, RUNTIME-ERROR");
                printStep( "I.", "Simulating a temporary server connection loss, ",0 );
                printStep( "should generate an error.\n");
                printStep( "3b.","Cashier enters the same item identifier.");
                Thread.sleep(1000);
                contr.registerItem(404);
                printStep("");
                errorMessageHandler.log("Managed to register item even when database call \nfailed.");
            } catch (ItemNotFoundInItemRegistryException ex) {
                writeToLogAndUI("Wrong exception was thrown.", ex);
            } catch (OperationFailedException ex) {
                writeToLogAndUI("Correctly failed to register item when database call \nfailed", ex);
            }
            Thread.sleep(2000);
            printStep("II.","Server connection resumed\n");
            Thread.sleep(2000);

            clearConsole();
            printStep( "3b.","Cashier enters the same item identifier.", 0);
            contr.registerItem(5);
            Thread.sleep(1000);

            clearConsole();
            printStep( "3c","Cashier enters item identifier and quantity.", 0);
            contr.registerItem(2, 3);
            Thread.sleep(1000);

            step6To8();

            printStep("9.", "Program presents total price, including VAT.\n");

            step10To17(500);
        } catch (ItemNotFoundInItemRegistryException ex) {
            errorMessageHandler.log("Unable to find item with ID %s, %nplease try again".formatted(ex.getItemIDNotFound()));
        } catch (OperationFailedException ex) {
            errorMessageHandler.log("No connection to inventory system. Try again.");
        } catch (Exception exc) {
            writeToLogAndUI("Failed to register sale, please try again.", exc);
        }
    }
    private void step1To2() throws InterruptedException, OperationFailedException {
        printStep("1.", "Customer arrives at POS with goods to purchase.\n");
        printStep("2.", "Cashier starts a new sale\n");
        contr.startSale();
    }
    private void step5() throws InterruptedException {
        printStep("5.", "Steps three and four are repeated until the", 0);
        printStep("cashier has registered all items.\n");
    }
    private void step6To8() throws InterruptedException {
        printStep("6.", "Cashier asks customer if they want to buy", 0);
        printStep("anything more.\n");
        printStep("7.", "Customer answers ’no’",0);
        printStep("a ’yes’ answer will be considered later).\n");
        printStep("8.", "Cashier ends the sale.");
        contr.endSale();
        Thread.sleep(1000);
    }
    private void step10To17(double paidAmt) throws InterruptedException, OperationFailedException {
        printStep("10.", "Cashier tells customer the total,", 0);
        printStep("and asks for payment.\n");
        Amount paidAmount = new Amount(paidAmt);
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
        Thread.sleep(1000);
        printStep("17.", "Customer leaves with receipt and goods.",0);
        Thread.sleep(2000);
        clearConsole();
        Thread.sleep(3000);
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
}