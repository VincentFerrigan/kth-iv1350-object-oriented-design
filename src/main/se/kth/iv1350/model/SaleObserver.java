package se.kth.iv1350.model;

// TODO lägg till RunningSaleView, ShoppingCartCheckoutDisplay (eller EndOfSaleDisplay), salelog/accounting?/additional highergrade task 1 + de krav som ställts i sem4 etc. Låt view inte printa något själv.

import se.kth.iv1350.util.Event;

/**
 * A listener interface for receiving notifications about
 * running sale.
 * The class that is interested in such
 * notifications implements this interface, and the object
 * created with that class is registered with
 * {@link se.kth.iv1350.controller.Controller#addSaleObserver(Event, SaleObserver)} 
 * When an item is added to sale, that object’s {@link #updateSale updateSale}
 * method is invoked.
 */
public interface SaleObserver {
    /**
     * Invoked when a rental has been paid.
     * @param sale The limited view of a sale that was updated.
     */
    void updateSale(LimitedSaleView sale);
}
