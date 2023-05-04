package se.kth.iv1350.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Contains all the output to the display regarding the current sale.
 */
public class SaleOutput {
    private final Sale sale;
    private List<Item> listOfItems;
    private SaleDTO saleInfo;

    /**
     * Creates a new instance of a {@link SaleOutput} for the specified sale.
     * @param sale The sale.
     */
    SaleOutput(Sale sale) {
        this.sale = sale;
        this.listOfItems = new ArrayList<>(sale.getCollectionOfItems());
    }

    /**
     * Get information about the sale.
     * @return Information about the sale as a {@link SaleDTO}
     */
    SaleDTO getSaleInfo() {
        if (saleInfo == null) {
            updateSaleInfo();
        }
        return new SaleDTO(saleInfo);
    }

    /**
     * Pretty printing for the output of current sale, sorted by the time when items are registered.
     * @return Pretty printing of sale, with items sorted by time of registration.
     */
    public String createOpenSaleString() {
        // Sorterar listan efter när den reggats.
        sortShoppingCartListAfterDescendingTimeOrder();

        // Pretty printing
        StringBuilder builder = new StringBuilder();
        updateSaleInfo();
        builder.append(saleInfo);
        builder.append("\n");
        builder.append("%-40s%s%n".formatted("Running total:", saleInfo.getTotalPrice()));
        builder.append("%-40s%s%n".formatted("Including VAT:", saleInfo.getTotalVATAmount()));
        return builder.toString();
    }

    /**
     * Pretty printing for the output of current sale, with items sorted alphabetically.
     * @return Pretty printing of sale, with items sorted alphabetically.
     */
    public String createCheckoutString() {
        // Sorterar listan per namn
        sortShoppingCartListAfterAscendingNameOrder();

        // Pretty printing
        StringBuilder builder = new StringBuilder();
        updateSaleInfo();
        builder.append(saleInfo);
        builder.append("\n");
        builder.append("%-40s%s%n".formatted("Total Price:", saleInfo.getTotalPrice()));
        builder.append("%-40s%s%n".formatted("Including VAT:", saleInfo.getTotalVATAmount()));
        return builder.toString();
    }

    private void updateSaleInfo() {
        List<SaleItemDTO> saleItems = getSaleItemsInfo();

        // Totalbelopp
        Amount runningTotal = sale.getRunningTotal();

        // Momsberäkning
        Amount totalVATAmount = sale.getTotalVATAmount();

        this.saleInfo = new SaleDTO(
                saleItems,            // list of saleItemInfo (DTO)
                runningTotal,         // Running total
                totalVATAmount);      // VAT for the total sale
    }
    private List<SaleItemDTO> getSaleItemsInfo() {
        List<SaleItemDTO> saleItemsInfo = new ArrayList<>();
        for (Item item : listOfItems) {
            saleItemsInfo.add(new SaleItemDTO(
                    item.getItemDTO(),        //itemInfo incl. name, description, price, vat rate
                    item.getQuantity(),       //quantity
                    item.getTotalPrice()     //totalPrice
            ));
        }
        return saleItemsInfo;
    }
    private void sortShoppingCartListAfterDescendingTimeOrder() {
        Collections.sort(listOfItems, Comparator.comparing(Item::getTimeOfUpdate).reversed());
    }

    private void sortShoppingCartListAfterAscendingNameOrder() {
        Collections.sort(listOfItems, Comparator.comparing(Item::getName));
    }

    @Deprecated
    private void sortShoppingCartListAscendingItemID() {
        Collections.sort(listOfItems, Comparator.comparing(Item::getItemID));
    }

    /**
     * Pretty printing of {@link SaleOutput} based on <code>this.createOpenSaleString()</code>
     * @return Pretty printing of this instance.
     */
    @Override
    public String toString() {
        return createOpenSaleString();
    }
}