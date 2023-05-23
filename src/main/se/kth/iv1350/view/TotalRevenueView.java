package se.kth.iv1350.view;

import se.kth.iv1350.model.Amount;

// TODO UML:a och skriva JavaDocs
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
        System.out.println("");
    }

    @Override
    protected void handleErrors(Exception e) {

    }


}
