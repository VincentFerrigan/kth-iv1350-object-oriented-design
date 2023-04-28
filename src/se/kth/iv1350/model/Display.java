package src.se.kth.iv1350.model;

import src.se.kth.iv1350.dto.SaleDTO;
import src.se.kth.iv1350.dto.SaleItemDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

// TODO package private kontruktor och final attribute?
public class Display {
    private final Sale sale;
    private List<Item> listOfItems;
    Display(Sale sale) {
        this.sale = sale;
        this.listOfItems = new ArrayList<>(sale.getCollectionOfItems());
    }

    private List<SaleItemDTO> getSaleItemsInfo() {
        List<SaleItemDTO> saleItemsInfo = new ArrayList<>();
        for (Item item : listOfItems) {
            saleItemsInfo.add(new SaleItemDTO(
                    item.getItemDTO().getName(),        //itemName
                    item.getItemDTO().getDescription(), //itemDescription
                    item.getItemDTO().getPrice(),       //itemPrice
                    item.getQuantity(),                 //itemQuantity
                    item.getTotalAmount()               //totalPrice
            ));
        }
        return saleItemsInfo;
    }
    public SaleDTO getSaleInfo() {
        List<SaleItemDTO> saleItems = getSaleItemsInfo();

        // Momsberäkning
        Amount totalVATAmount = new Amount(0);
        List<Amount> vatAmounts = listOfItems.stream().map(Item::getVatAmount).collect(toList());
        totalVATAmount = totalVATAmount.plus(vatAmounts);

        return new SaleDTO(
                saleItems,              // list of saleItemInfo (DTO)
                sale.getRunningTotal(), // Running total
                totalVATAmount);        // VAT for the total sale
    }
    public String createRunningSaleString() {
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

    public String createEndOfSaleString() {
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
        return createRunningSaleString();
    }
}