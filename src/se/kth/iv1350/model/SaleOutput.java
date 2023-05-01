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
    SaleOutput(Sale sale) {
        this.sale = sale;
        this.listOfItems = new ArrayList<>(sale.getCollectionOfItems());
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
    public SaleDTO getSaleInfo() {
        List<SaleItemDTO> saleItems = getSaleItemsInfo();

        // Totalbelopp
        Amount runningTotal = sale.getRunningTotal();

        // Momsberäkning
        Amount totalVATAmount = sale.getTotalVATAmount();

        return new SaleDTO(
                saleItems,            // list of saleItemInfo (DTO)
                runningTotal,         // Running total
                totalVATAmount);      // VAT for the total sale
    }
    public String createOpenSaleString() {
        // Sorterar listan efter när den reggats.
        Collections.sort(listOfItems, Comparator.comparing(Item::getTimeOfUpdate).reversed());

        // Pretty printing
        StringBuilder builder = new StringBuilder();
        SaleDTO currentSaleInfo = getSaleInfo();
        builder.append(currentSaleInfo);
        builder.append("\n");
        builder.append("%-40s%s%n".formatted("Running total:", currentSaleInfo.getTotalPrice()));
        return builder.toString();
    }

    public String createCheckoutString() {
        // Sorterar listan per namn
        Collections.sort(listOfItems, Comparator.comparing(Item::getName));

        // Pretty printing
        StringBuilder builder = new StringBuilder();
        SaleDTO saleInfo = getSaleInfo();
        builder.append(saleInfo);
        builder.append("\n");
        builder.append("%-40s%s%n".formatted("Total Price:", saleInfo.getTotalPrice()));
        builder.append("%-40s%s%n".formatted("Total VAT:", saleInfo.getTotalVATAmount()));
        return builder.toString();
    }

    // TODO Eventuellt ändra till public String createRunningSaleString()
    @Override
    public String toString() {
        return createOpenSaleString();
    }
}