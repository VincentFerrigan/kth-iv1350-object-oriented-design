package se.kth.iv1350.integration.pricing;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.ShoppingCartItem;

public interface VATStrategy {
    Amount calculateVATForItem(ShoppingCartItem item);
}
