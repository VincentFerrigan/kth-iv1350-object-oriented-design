package se.kth.iv1350.view;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.SaleDTO;
import se.kth.iv1350.model.SaleObserver;

// TODO UML:a och skriva JavaDocs
/**
 * A template for a sale observer that displays the total revenue when it has changed.
 */
public abstract class TotalRevenue implements SaleObserver {
    private static Amount totalRevenue;

    /**
     * Creates a new instance
     */
    protected TotalRevenue() {
        totalRevenue = new Amount(0);
    }

    /**
     * Call when the total revenue has changed
     * @param saleInfo The sale that was update.
     */
    @Override
    public void updateSale(SaleDTO saleInfo) {
        Amount newRevenue = saleInfo.getTotalPrice();
        totalRevenue = totalRevenue.plus(newRevenue);
        showTotalRevenue(totalRevenue);
    }

    private void showTotalRevenue(Amount totalRevenue) {
        try {
            doShowTotalRevenue(totalRevenue);
        } catch(Exception e) {
            handleErrors(e);
        }
    }

    /**
     * Shows the total revenue.
     * @param totalRevenue the new total revenue
     */
    protected abstract void doShowTotalRevenue(Amount totalRevenue);

    protected abstract void handleErrors (Exception e );
}