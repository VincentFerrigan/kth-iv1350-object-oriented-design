package se.kth.iv1350.integration.pricing;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;

public interface DiscountStrategy {
    Amount getTotal(Sale sale);
}
