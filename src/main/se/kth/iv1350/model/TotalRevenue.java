package se.kth.iv1350.model;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.CashRegisterObserver;

/**
 * A template for a cash register observer that displays the total revenue, each time it changes.
 */
public abstract class TotalRevenue implements CashRegisterObserver {

    /**
     * Creates a new instance
     */
    protected TotalRevenue() {}

    /**
     * Call when the total revenue has changed
     * @param totalRevenue the new total revenue
     */
    @Override
    public void updateRevenue(Amount totalRevenue) {
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

    /**
     * Handles errors that are thrown.
     * @param ex the exception thrown
     */
    protected abstract void handleErrors (Exception e );
}