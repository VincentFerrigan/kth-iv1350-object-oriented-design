package se.kth.iv1350.integration.pricing;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A <code>DiscountStrategy</code>, which performs multiple discount and promotion
 * algorithms.
 * All these algorithms that are added to this composite are executed,
 * and the lowest total price is found.
 */
public class CompositeDiscountStrategy implements DiscountStrategy {
    private List<DiscountStrategy> discountStrategies;
    private DiscountStrategy usedDiscountStrategy;

    CompositeDiscountStrategy(){
        discountStrategies = new ArrayList<>();
    }

    /**
     * Invokes all discount and promotion algorithms added to this composite.
     * @param sale the sale to perform the discount and promotion
     *             strategies/algorithms on.
     * @return the lowest total price
     */
    @Override
    public Amount getTotal(Sale sale) {
        Amount lowestTotalPrice = sale.calculateRunningTotal();

        for (Iterator i = discountStrategies.iterator(); i.hasNext();) {
          DiscountStrategy strategy = (DiscountStrategy) i.next();
          Amount total = strategy.getTotal(sale);
          if (lowestTotalPrice.compareTo(total) > 0) {
              lowestTotalPrice = total;
              usedDiscountStrategy = strategy;
          }

        }
        return lowestTotalPrice;
    }

    /**
     * Adds a discount or promotion algorithm that will be invoked when
     * this composite is calculating the total price.
     * The newly added algorithm will be called after all previously added
     * algorithms.
     *
     * @param discountStrategy The new <code>DiscountStrategy</code> to add.
     */
    public void addDiscountStrategy(DiscountStrategy discountStrategy) {
        discountStrategies.add(discountStrategy);
    }

    public Amount getDiscount() {
        return usedDiscountStrategy != null
                ? usedDiscountStrategy.getDiscount()
                : new Amount(0);
    }

    @Override
    public String toString() {
        return usedDiscountStrategy != null
                ? usedDiscountStrategy.toString()
                : "".toString(); // empty string
    }
}