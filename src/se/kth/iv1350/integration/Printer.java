package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.model.Display;
import src.se.kth.iv1350.model.Receipt;

/**
 * The interface to the printer and display, used for all printouts initiated by this
 * program.
 */
public class Printer {
    /**
     * Creates an instance of {@link Printer}
     */
    public Printer(){
        //TODO construct
    }

    /**
     * Prints the specified receipt.
     * It's a dummy implementation that prints to
     * <code>System.out</code>
     * @param receipt The receipt
     */
    public void print(Receipt receipt) {
        System.out.println("----------------- Receipt follows ----------------");
        System.out.println(receipt);
        System.out.println("------------------ End of receipt ----------------");

    }
    public void print(SaleLog saleLog) {
        // TODO
    }

    /**
     * Displays the shopping cart with running total
     * It's a dummy implementation that prints to
     * <code>System.out</code>
     * @param display The display
     */
    public void printCurrentSale(Display display) {
        // TODO Eventuellt ändra till public String createRunningSaleString()
        System.out.println("----------------- Display follows ----------------");
        System.out.println(display.createRunningSaleString());
        System.out.println("------------------ End of Display ----------------");

    }

    /**
     * Displays the checkout shopping cart with total amount and vat.
     * It's a dummy implementation that prints to
     * <code>System.out</code>
     * @param display The display
     */
    public void printEndOfSale(Display display) {
        System.out.println("--------------- End of Sale follows --------------");
        System.out.println(display.createEndOfSaleString());
        System.out.println("---------------- End of End of Sale --------------");
    }
}
