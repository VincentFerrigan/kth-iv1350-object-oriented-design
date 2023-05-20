package se.kth.iv1350.integration.pricing;

import se.kth.iv1350.model.Customer;
import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;

/**
 * A <code>Discount Strategy</code> that applies member discounts
 */
public class MemberDiscount implements DiscountStrategy{
    private double discountRate = 0.1;
    MemberDiscount() {

    }

    @Override
    public Amount getTotal(Sale sale) {
        Customer customer = sale.getCustomer();
        Amount totalPrice = sale.calculateRunningTotal();

        return (customer != null && customer.getDiscountType() == DiscountType.MEMBER)
                ? totalPrice.multiply(1 - discountRate)
                : totalPrice;
    }
}
