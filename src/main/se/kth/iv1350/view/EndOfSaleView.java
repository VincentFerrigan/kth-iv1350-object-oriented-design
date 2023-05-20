package se.kth.iv1350.view;

import se.kth.iv1350.model.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// TODO UML:a och skriva JavaDocs
/**
 * Shows the checked out sale details including the shopping cart, total price,
 * discounts, promotions and total VAT costs.
 */
public class EndOfSaleView extends SaleView {
    private Amount totalPricePreDiscount;
    private Amount totalPriceAfterDiscount;

    @Override
    protected void sortShoppingCart(List<ShoppingCartItem> listOfShoppingCartItems) {
        Collections.sort(listOfShoppingCartItems, Comparator.comparing(ShoppingCartItem::getName));
    }
    //TODO: How to display DISCOUNTs!!!
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
        totalPricePreDiscount = sale.calculateRunningTotal();
        totalPriceAfterDiscount = sale.getTotalPrice();

        System.out.println("--------------- End of Sale follows --------------");
        System.out.println(createSaleItemsInfoString());
        if (!totalPriceAfterDiscount.equals(totalPricePreDiscount)) {
            System.out.println("%-40s-%s".formatted("Total discount:", totalPricePreDiscount.minus(totalPriceAfterDiscount)));
        }
        System.out.println("%-40s%s".formatted("Total Price:", totalPriceAfterDiscount));
        System.out.println("%-40s%s".formatted("Including VAT:", sale.getTotalVATCosts()));
        System.out.println("---------------- End of End of Sale --------------");
    }
}
