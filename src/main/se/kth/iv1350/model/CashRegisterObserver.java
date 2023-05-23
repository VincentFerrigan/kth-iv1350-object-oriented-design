package se.kth.iv1350.model;

/**
 * A listener interface for receiving notifications about
 * the total revenue in the Register.
 * The class that is interested in such
 * notifications implements this interface, and the object
 * created with that class is registered with
 * {@link se.kth.iv1350.controller.Controller#addCashRegiserObserver(CashRegisterObserver)}
 * When the revenue in the Cash Register changes, that object’s {@link #updateRenue updateRenue}
 * method is invoked.
 */
public interface CashRegisterObserver {
    /**
     * Invoked when the revenue has been changed in the Cash Registry.
     * @param totalRevenue the new total revenue
     */
    void updateRevenue(Amount totalRevenue);
}
