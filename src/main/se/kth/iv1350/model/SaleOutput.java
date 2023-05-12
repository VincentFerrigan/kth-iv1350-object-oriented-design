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
     * Update sale information for updateForCheckout, sort item alphabetically.
     */
    public void updateForCheckout() {
        // Sorterar listan per namn
        sortShoppingCartListAfterAscendingNameOrder();
        updateSaleInfo();
    }

    /**
     * Update sale information for running sale, sort by the time when items are registered.
     */
    public void updateRunningSale() {
        // Sorterar listan efter när den reggats.
        sortShoppingCartListAfterDescendingTimeOrder();
        updateSaleInfo();
    }
    private void updateSaleInfo() {
        List<SaleItemDTO> saleItems = getSaleItemsInfo();

        // Totalbelopp
        Amount runningTotal = sale.getTotalAmount() == null ? sale.calculateRunningTotal() : sale.getTotalAmount();

        // Momsberäkning
        Amount totalVATAmount = sale.getTotalVAT() == null? sale.calculateTotalVATAmount() : sale.getTotalVAT();

        // Rabattberäkning
        Amount totalDiscounts = sale.getDiscountAmount() == null? new Amount(0) : sale.getDiscountAmount();

        this.saleInfo = new SaleDTO(
                saleItems,            // list of saleItemInfo (DTO)
                runningTotal,         // Running total
                totalVATAmount,       // VAT for the total sale
                totalDiscounts);      // Total discounts for the total sale
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

}