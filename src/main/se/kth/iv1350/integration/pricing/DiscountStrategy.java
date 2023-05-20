package se.kth.iv1350.integration.pricing;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;

/**
 * Discount strategy defining the ability to calculate total price.
 * This interface shall be implemented by a class providing a promotion or discount algorithm.
 */
public interface DiscountStrategy {

    /**
     * Calculated total price after applying discount or promotion algorithms
     * on specified sale.
     * @param sale the sale to perform the discount and promotion algorithms on.
     * @return the lowest total price.
     */
    Amount getTotal(Sale sale);
}
