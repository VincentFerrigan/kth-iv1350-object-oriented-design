package se.kth.iv1350.view;

import se.kth.iv1350.model.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Shows the running sale details including the shopping cart, total price and total VAT costs.
 */
public class RunningSaleView extends SaleView {

    /**
     * Prints the running sale details that includes shopping cart,
     * total price and total VAT costs.
     * It's a dummy implementation that prints to
     * <code>System.out</code>
     * @param sale a limited view of {@link Sale}
     */
    @Override
    protected void printCurrentState(LimitedSaleView sale) {
        System.out.println("");
        System.out.println("------------------ Display follows -----------------");
        System.out.print(createSaleItemsInfoString());
        System.out.println("%-40s%s".formatted("Running total:", sale.calculateRunningTotal()));
        System.out.println("%-40s%s".formatted("Including VAT:", sale.getTotalVATCosts()));
        System.out.println("------------------- End of Display -----------------");
        System.out.println("");
    }

    /**
     *
     * @param isComplete is the sale complete?
     * @return if sale is not complete, the view should update and therefore return true
     */
    @Override
    protected boolean shouldUpdate(boolean isComplete) {
        if (isComplete) {return false;}
        else {return true;}
    }

    @Override
    protected void sortShoppingCart(List<ShoppingCartItem> listOfShoppingCartItems) {
        Collections.sort(listOfShoppingCartItems, Comparator.comparing(ShoppingCartItem::getTimeOfUpdate).reversed());
    }
}
