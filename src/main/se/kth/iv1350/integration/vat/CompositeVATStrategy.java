package se.kth.iv1350.integration.vat;

// TODO: skriv om Javadocs

import se.kth.iv1350.integration.pricing.DiscountStrategy;
import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.ShoppingCartItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A <code>VATStrategy</code>, which performs multiple VAT and taxation algorithms.
 * All these algorithms that are added to this composite are executed,
 * in the order they were added, until the appropriate algorithm is found.
 */
public class CompositeVATStrategy implements VATStrategy {

    private List<VATStrategy> vatStrategies;
    private VATStrategy usedVATStrategy;

    CompositeVATStrategy() { vatStrategies = new ArrayList<>(); }

    /**
     * Invokes all VAT and taxation algorithms added to this composite,
     * in the order they were added, until the appropriate algorithm is found.
     * @param item the item to perform the VAT or other taxation strategies/algorithms on.
     * @ return the VAT/TAX.
     */
    @Override
    public Amount calculateVATForItem(ShoppingCartItem item) {

        for(Iterator i = vatStrategies.iterator(); i.hasNext();) {
            VATStrategy strategy = (VATStrategy) i.next();
            Amount vatCost = strategy.calculateVATForItem(item);
            if (vatCost != null) {
                usedVATStrategy = strategy;
                return new Amount(vatCost);
            }
        }
        return null;
    }
    /**
     * Adds a vat/taxation algorithm that will be invoked when this composite is calculating the VAT.
     * The newly added algorithm will be called after all previously added algorithms.
     *
     * @param vatStrategy The new <code>VATStrategy</code> to add.
     */
    public void addVATStrategy(VATStrategy vatStrategy) {
        vatStrategies.add(vatStrategy);
    }

    @Override
    public String toString() {
        return usedVATStrategy != null
                ? usedVATStrategy.toString()
                : "".toString(); // empty string
    }
}