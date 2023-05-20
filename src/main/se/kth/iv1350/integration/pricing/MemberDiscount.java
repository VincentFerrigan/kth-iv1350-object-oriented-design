package se.kth.iv1350.integration.pricing;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;

public class MemberDiscount implements DiscountStrategy{
    MemberDiscount() {}

    @Override
    public Amount getTotal(Sale sale) {
        return sale.getTotalPrice().multiply(0.9);
    }
}
