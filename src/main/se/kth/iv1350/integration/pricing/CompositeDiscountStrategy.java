package se.kth.iv1350.integration.pricing;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeDiscountStrategy implements DiscountStrategy {
    private List<DiscountStrategy> discountStrategies;
    CompositeDiscountStrategy(){
        discountStrategies = new ArrayList<>();
    }
    @Override
    public Amount getTotal(Sale sale) {
        Amount lowestTotalPrice = sale.getTotalPrice();
        for (Iterator i = discountStrategies.iterator(); i.hasNext();) {
          DiscountStrategy strategy = (DiscountStrategy) i.next();
          Amount total = strategy.getTotal(sale);
          lowestTotalPrice = lowestTotalPrice.compareTo(total) > 0 ? total : lowestTotalPrice;
        }
        return lowestTotalPrice;
    }

    public void addDiscountStrategy(DiscountStrategy discountStrategy) {
        discountStrategies.add(discountStrategy);
    }
}