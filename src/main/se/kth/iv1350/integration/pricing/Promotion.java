package se.kth.iv1350.integration.pricing;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Customer;
import se.kth.iv1350.model.Sale;

/**
 * A <code>Discount Strategy</code> that applies promotions
 * If total price is greater than a threshold the discount is applied depending.
 * The threshold depends on if the customer is registered member or not.
 */
public class Promotion implements DiscountStrategy {
    private Amount threshold  = new Amount(500);
    private Amount bonusCheck = new Amount(100);
    private Amount discountAmount;
    @Override
    public Amount getTotal(Sale sale) {
        Customer customer = sale.getCustomer();
        Amount totalPrice = sale.calculateRunningTotal();

        CustomerType customerType = customer != null ? customer.getCustomerType() : null;
        Amount newPrice = (customerType == null)
                ? totalPrice.compareTo(threshold.multiply(2)) > 0
                    ? totalPrice.minus(bonusCheck) : totalPrice
                : totalPrice.compareTo(threshold)  > 0
                    ? totalPrice.minus(bonusCheck) : totalPrice;
//        Amount newPrice2 = totalPrice.compareTo(threshold) > 0
//                ? (customerType == null ? totalPrice.minus(bonusCheck) : totalPrice.minus(bonusCheck.multiply(2)))
//                : totalPrice;

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
        builder.append("%s cashback".formatted(discountAmount));

        return builder.toString();
    }
}
