package se.kth.iv1350.model;

/**
 * A listener interface for receiving notifications about
 * running sale.
 * The class that is interested in such
 * notifications implements this interface, and the object
 * created with that class is registered with
 * {@link se.kth.iv1350.controller.Controller#addSaleObserver(SaleObserver)}
 * When an item is added to sale, that object’s {@link #updateSale updateSale}
 * method is invoked.
 */
public interface SaleObserver {
    /**
     * Invoked when a sale has been updated.
     * E.g. Item added to shopping cart, check out etc.
     * @param sale The limited view of a sale that was updated.
     */
    void updateSale(LimitedSaleView sale);
}