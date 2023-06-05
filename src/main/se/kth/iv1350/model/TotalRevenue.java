package se.kth.iv1350.model;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.CashRegisterObserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

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
            doShowTotalRevenue(totalRevenue, createTime());
        } catch(Exception e) {
            handleErrors(e);
        }
    }

    private String createTime() {
        Locale locale = new Locale("sv", "SE");
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }

    /**
     * Shows the total revenue.
     * @param totalRevenue the new total revenue
     * @param timeStamp formatted time information, identifying
     *                  when the info was displayed
     */
    protected abstract void doShowTotalRevenue(Amount totalRevenue, String timeStamp);

    /**
     * Handles errors that are thrown.
     * @param ex the exception thrown
     */
    protected abstract void handleErrors(Exception e );
}