package se.kth.iv1350.view;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.util.LogHandler;

public class TotalRevenueView extends TotalRevenue {
    private LogHandler logger;
    /**
     * Shows the total revenue.
     * @param totalRevenue the new total revenue
     */
    @Override
    protected void doShowTotalRevenue(Amount totalRevenue) {
        System.out.println("--------------- Revenue update follows --------------");
        System.out.println("%-40s%s".formatted("Total revenue:", totalRevenue));
        System.out.println("--------------- End of Revenue update ---------------");
    }

    @Override
    protected void handleErrors(Exception e) {
        logger.logException(e);

    }


}
