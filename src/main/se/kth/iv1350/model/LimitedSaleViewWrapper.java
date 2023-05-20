package se.kth.iv1350.model;

import java.util.Collection;

/**
 * A limited view of a particular sale. This wrapper class
 * exposes only certain {@link Sale} methods
 */
public class LimitedSaleViewWrapper implements LimitedSaleView {
    private Sale sale;

    /**
     * Creates a new instance, representing a limited view of {@link Sale}.
     */
    public LimitedSaleViewWrapper(Sale sale) {
        this.sale = sale;
    }

    @Override
    public boolean isComplete() {
        return sale.isComplete();
    }

    @Override
    public Amount getTotalPricePaid() {
        return sale.getTotalPricePaid();
    }

    @Override
    public Amount getTotalPrice() {
        return sale.getTotalPrice();
    }

    @Override
    public Amount calculateRunningTotal() {
        return sale.calculateRunningTotal();
    }

    @Override
    public Amount getTotalVATCosts() {
        return sale.getTotalVATCosts();
    }

    @Override
    public Collection<ShoppingCartItem> getCollectionOfItems() {
        return sale.getCollectionOfItems();
    }
}
