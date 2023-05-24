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

    /**
     * Gets complete status, i.e. has the sale ended
     * @return complete status
     */
    @Override
    public boolean isComplete() {
        return sale.isComplete();
    }

    /**
     * Gets the total price that was paid by the customer
     * return The total amount paid by the customer.
     */
    @Override
    public Amount getTotalPricePaid() {
        return sale.getTotalPricePaid();
    }

    /**
     * Gets the total amount for the current sale, including possible discount.
     * @return The total amount of the current sale.
     */
    @Override
    public Amount getTotalPrice() {
        return sale.calculateTotalPrice();
    }

    /**
     * Calculates the total cost of the shopping cart
     * @return The running total as a {@link Amount}.
     */
    @Override
    public Amount calculateRunningTotal() {
        return sale.calculateRunningTotal();
    }

    /**
     * Gets the total VAT amount for the current sale.
     * @return The total VAT amount of the current sale.
     */
    @Override
    public Amount getTotalVATCosts() {
        return sale.getTotalVATCosts();
    }

    /**
     * Gets a collection of items from the shopping cart.
     * @return A {@link Collection} of the items in the shopping cart.
     */
    @Override
    public Collection<ShoppingCartItem> getCollectionOfItems() {
        return sale.getCollectionOfItems();
    }

    /**
     * Gets the discount of the current sale, if any.
     * @return the total discount of the current sale.
     */
    @Override
    public Amount getDiscount() {
       return sale.getDiscount();
    }
    /**
     * Gets the discount information, if any.
     * @return the discount information as string. Returns empty discount if no discount was applied
     */
    @Override
    public String createStringDiscountInfo() {
        return sale.createStringDiscountInfo();
    }
}