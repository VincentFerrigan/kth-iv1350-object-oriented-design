package se.kth.iv1350.integration;

import se.kth.iv1350.model.SaleOutput;

/**
 * The interface to the display,
 * used for displaying all outputs (exempt for receipts) initiated by this program.
 */
public class Display {
    /**
     * Creates an instance of {@link Display}
     */
    public Display(){}
    /**
     * Displays the shopping cart with running total
     * It's a dummy implementation that prints to
     * <code>System.out</code>
     * @param saleOutput The display output
     */
    public void displayOpenSale(SaleOutput saleOutput) {
        // TODO Eventuellt ändra till public String createRunningSaleString()
        System.out.println("----------------- Display follows ----------------");
        System.out.println(saleOutput.createOpenSaleString());
        System.out.println("------------------ End of Display ----------------");

    }

    /**
     * Displays the checkout shopping cart with total amount and vat.
     * It's a dummy implementation that prints to
     * <code>System.out</code>
     * @param saleOutput The display output
     */
    public void displayCheckout(SaleOutput saleOutput) {
        System.out.println("--------------- End of Sale follows --------------");
        System.out.println(saleOutput.createCheckoutString());
        System.out.println("---------------- End of End of Sale --------------");
    }
}
