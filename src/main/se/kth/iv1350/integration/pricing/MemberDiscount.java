package se.kth.iv1350.integration.pricing;

import se.kth.iv1350.model.Customer;
import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;

/**
 * A <code>Discount Strategy</code> that applies member discounts
 */
public class MemberDiscount implements DiscountStrategy{
    private double discountRate = 0.05;
    private Amount discountAmount;
    MemberDiscount() {

    }

    @Override
    public Amount getTotal(Sale sale) {
        Customer customer = sale.getCustomer();
        Amount totalPrice = sale.calculateRunningTotal();
        Amount newPrice = (customer != null && customer.getCustomerType() == CustomerType.MEMBER)
                ? totalPrice.multiply(1 - discountRate)
                : totalPrice;

        discountAmount = totalPrice.minus(newPrice);
        return new Amount(newPrice);
    }

    @Override
    public Amount getDiscount() {
        return discountAmount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Member discount: %d %%".formatted(((int) (discountRate * 100))));

        return builder.toString();
    }

}
