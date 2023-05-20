package se.kth.iv1350.view;

import se.kth.iv1350.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A template for a sale observer that shows sale details including the shopping cart.
 */
public abstract class SaleView implements SaleObserver {
    private List<ShoppingCartItem> listOfShoppingCartItems;
    private LimitedSaleView sale;

    /**
     * Creates a new instance if the Sale View
     */
    protected SaleView() {}
    @Override
    public void updateSale(LimitedSaleView sale) {
        addNewSaleInfoDetails(sale);
        sortShoppingCart(listOfShoppingCartItems);
        printCurrentState(sale);
    }
    protected String createSaleItemsInfoString() {
        // Pretty printing
        StringBuilder builder = new StringBuilder();
        for (ShoppingCartItem item: listOfShoppingCartItems) {
            builder.append("%-40s%s%n".formatted(item.getName(), item.getTotalSubPrice()));
            builder.append("(%d * %s)\n".formatted(item.getQuantity(), item.getUnitPrice()));
        }
        return builder.toString();
    }

    private void addNewSaleInfoDetails(LimitedSaleView sale) {
        listOfShoppingCartItems = new ArrayList<>(sale.getCollectionOfItems());
        this.sale = sale;
    }

    /**
     * Shows the shopping cart and other sale details.
     * @param sale contains a limited view of the sale.
     */
    protected abstract void printCurrentState(LimitedSaleView sale);

    /**
     * Sorts the shopping cart
     * @param listOfShoppingCartItems contains the shopping cart
     */
    protected abstract void sortShoppingCart(List<ShoppingCartItem> listOfShoppingCartItems);
}

