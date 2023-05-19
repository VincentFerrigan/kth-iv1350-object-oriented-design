package se.kth.iv1350.view;

import se.kth.iv1350.model.SaleDTO;
import se.kth.iv1350.model.SaleObserver;
import se.kth.iv1350.model.ShoppingCartItemDTO;

import java.util.List;

// TODO UML:a och skriva JavaDocs
/**
 * A template for a sale observer that shows sale details including the shopping cart.
 */
public abstract class SaleView implements SaleObserver {
    private List<ShoppingCartItemDTO> listOfShoppingCartItemsInfo;
    private SaleDTO saleInfo;

    /**
     * Creates a new instance, with all the
     */
    protected SaleView() {}
    @Override
    public void updateSale(SaleDTO saleInfo) {
        addNewSaleInfoDetails(saleInfo);
        sortShoppingCart(listOfShoppingCartItemsInfo);
        printCurrentState(saleInfo);
    }
    protected String createSaleItemsInfoString() {
        // Pretty printing
        StringBuilder builder = new StringBuilder();
        for (ShoppingCartItemDTO itemInfo: listOfShoppingCartItemsInfo) {
            builder.append("%-40s%s%n".formatted(itemInfo.getName(), itemInfo.getTotalPrice()));
            builder.append("(%d * %s)\n".formatted(itemInfo.getQuantity(), itemInfo.getUnitPrice()));
        }
        return builder.toString();
    }

    private void addNewSaleInfoDetails(SaleDTO saleInfo) {
        listOfShoppingCartItemsInfo = saleInfo.getSaleItemsInfo();
        this.saleInfo = saleInfo;
    }

    /**
     * Shows the shopping cart and other sale details.
     * @param saleInfo contains the sale details
     */
    protected abstract void printCurrentState(SaleDTO saleInfo);

    /**
     * Sorts the shopping cart
     * @param listOfShoppingCartItemsInfo contains the shopping cart
     */
    protected abstract void sortShoppingCart(List<ShoppingCartItemDTO> listOfShoppingCartItemsInfo);
}

