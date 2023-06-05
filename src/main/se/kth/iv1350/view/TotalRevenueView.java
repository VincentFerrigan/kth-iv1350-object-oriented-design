package se.kth.iv1350.view;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.TotalRevenue;

/**
 * Shows the total revenue, each time it has changed.
 * It's a dummy implementation that prints to
 * <code>System.out</code>
 */
public class TotalRevenueView extends TotalRevenue {

    /**
     * Shows the total revenue.
     * @param totalRevenue the new total revenue
     */
    @Override
    protected void doShowTotalRevenue(Amount totalRevenue) {
        System.out.println("");
        System.out.println("--------------- Revenue update follows --------------");
        System.out.println("%-40s%s".formatted("Total revenue:", totalRevenue));
        System.out.println("--------------- End of Revenue update ---------------");
    }

    /**
     * Handles errors that are thrown.
     * @param ex the exception thrown
     */
    @Override
    protected void handleErrors(Exception ex) {
        ErrorMessageHandler.getInstance().log("Unable to show total revenue, " +
                "please contact help desk");
    }
}
