package src.se.kth.iv1350.model;

import src.se.kth.iv1350.dto.SaleDTO;
import src.se.kth.iv1350.dto.SaleItemDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

// TODO package private kontruktor och final attribute?
public class SaleOutput {
    private final Sale sale;
    private List<Item> listOfItems;
    private SaleDTO saleInfo;

    SaleOutput(Sale sale) {
        this.sale = sale;
        this.listOfItems = new ArrayList<>(sale.getCollectionOfItems());
    }

    public SaleDTO getSaleInfo() {
        if (saleInfo == null) {
            updateSaleInfo();
        }
        return new SaleDTO(saleInfo);
    }
    public String createOpenSaleString() {
        // Sorterar listan efter när den reggats.
        sortShoppingCartListAfterDescendingTimeOrder();

        // Pretty printing
        StringBuilder builder = new StringBuilder();
        updateSaleInfo();
        builder.append(saleInfo);
        builder.append("\n");
        builder.append("%-40s%s%n".formatted("Running total:", saleInfo.getTotalPrice()));
        return builder.toString();
    }

    public String createCheckoutString() {
        // Sorterar listan per namn
        sortShoppingCartListAfterAscendingNameOrder();

        // Pretty printing
        StringBuilder builder = new StringBuilder();
        updateSaleInfo();
        builder.append(saleInfo);
        builder.append("\n");
        builder.append("%-40s%s%n".formatted("Total Price:", saleInfo.getTotalPrice()));
        builder.append("%-40s%s%n".formatted("Total VAT:", saleInfo.getTotalVATAmount()));
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
    private void sortShoppingCartListAscendingItemID() {
        Collections.sort(listOfItems, Comparator.comparing(Item::getItemID));
    }


    // TODO Eventuellt ändra till public String createRunningSaleString()
    @Override
    public String toString() {
        return createOpenSaleString();
    }
}