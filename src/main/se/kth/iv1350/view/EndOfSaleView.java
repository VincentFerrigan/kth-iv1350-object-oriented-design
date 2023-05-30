package se.kth.iv1350.view;

import se.kth.iv1350.model.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Shows the checked out sale details including the shopping cart, total price,
 * discounts, promotions and total VAT costs.
 */
public class EndOfSaleView extends SaleView {
    @Override
    protected void sortShoppingCart(List<ShoppingCartItem> listOfShoppingCartItems) {
        Collections.sort(listOfShoppingCartItems, Comparator.comparing(ShoppingCartItem::getName));
    }

    /**
     * Prints the checked out sale details (the end of sale).
     * It includes shopping cart,
     * total price, discounts/promotions and total VAT costs.
     * It's a dummy implementation that prints to
     * <code>System.out</code>
     * @param sale a limited view of {@link Sale}
     */
    @Override
    protected void printCurrentState(LimitedSaleView sale) {
        Amount totalPricePreDiscount = sale.calculateRunningTotal();
        Amount totalPriceAfterDiscount = sale.getTotalPrice();
        Amount discount = sale.getDiscount();
        String discountInfo = sale.createStringDiscountInfo();

        System.out.println("");
        System.out.println("---------------- End of Sale follows ---------------");
        StringBuilder builder = new StringBuilder();
        builder.append(createSaleItemsInfoString());
        if (!totalPriceAfterDiscount.equals(totalPricePreDiscount)) {
            builder.append(createDiscountInfoString(discount, discountInfo));
        }
        builder.append("%-40s%s".formatted("Total Price:", totalPriceAfterDiscount));
        builder.append("%n".formatted());
        builder.append("%-40s%s".formatted("Including VAT:", sale.getTotalVATCosts()));
        builder.append("%n".formatted());
        System.out.println(builder.toString());
        System.out.println("----------------- End of End of Sale ---------------");
        System.out.println("");
    }
    /**
     * Should the observer act on the update?
     * Certain observers will act if the sale is complete, others will not.
     *
     * @param isComplete is the sale complete?
     * @return if sale is complete, the view should update and therefore return true
     */
    @Override
    protected boolean shouldUpdate(boolean isComplete) {
        if (isComplete) {return true;}
        else {return false;}
    }

    private String createDiscountInfoString(Amount discount, String discountInfo){
        StringBuilder discountInfoBuilder = new StringBuilder();
        discountInfoBuilder.append("%-40s-%s".formatted("Total discount:", discount));
        discountInfoBuilder.append("%n".formatted());
        discountInfoBuilder.append("(%s)".formatted(discountInfo));
        discountInfoBuilder.append("%n".formatted());
        discountInfoBuilder.append("%n".formatted());

        return discountInfoBuilder.toString();
    }
}