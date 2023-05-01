package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.model.SaleOutput;
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
    public void printReceipt(Receipt receipt) {
        System.out.println("----------------- Receipt follows ----------------");
        System.out.println(receipt);
        System.out.println("------------------ End of receipt ----------------");

    }
    public void printSaleLog(SaleLog saleLog) {
        // TODO
    }

}
