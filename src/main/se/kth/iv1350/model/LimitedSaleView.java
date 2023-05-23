package se.kth.iv1350.model;

import java.util.Collection;

/**
 * An observed interface exposing only certain {@link Sale} methods
 */
public interface LimitedSaleView {

    /**
     * Gets complete status, i.e. has the sale ended
     * @return complete status
     */
    boolean isComplete();

    /**
     * Gets the total price that was paid by the customer
     * return The total amount paid by the customer.
     */
    Amount getTotalPricePaid();

    /**
     * Gets the total amount for the current sale, including possible discount.
     * @return The total amount of the current sale.
     */
    Amount getTotalPrice();

    /**
     * Calculates the total cost of the shopping cart
     * @return The running total as a {@link Amount}.
     */
    Amount calculateRunningTotal();

    /**
     * Gets the total VAT amount for the current sale.
     * @return The total VAT amount of the current sale.
     */
    Amount getTotalVATCosts();


    /**
     * Gets a collection of items from the shopping cart.
     * @return A {@link Collection} of the items in the shopping cart.
     */
    Collection<ShoppingCartItem> getCollectionOfItems();

    /**
     * Gets the discount of the current sale, if any.
     * @return the total discount of the current sale.
     */
    Amount getDiscount();
    /**
     * Gets the discount information, if any.
     * @return the discount information as string. Returns empty discount if no discount was applied
     */
    String createStringDiscountInfo();
}