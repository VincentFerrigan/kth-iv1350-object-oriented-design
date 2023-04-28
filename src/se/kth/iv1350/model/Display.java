package src.se.kth.iv1350.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

// TODO package private kontruktor och final attribute?
public class Display {
    private final Sale sale;
    Display(Sale sale) {
        this.sale = sale;
    }

    public String createEndOfSaleString() {
        List<Item> listOfItems = new ArrayList<>(sale.getCollectionOfItems());

        // Momsberäkning
        Amount totalVATAmount = new Amount(0);
        List<Amount> vatAmounts = listOfItems.stream().map(Item::getVatAmount).collect(toList());
        totalVATAmount = totalVATAmount.plus(vatAmounts);

        // Sorterar listan per namn
        Collections.sort(listOfItems, Comparator.comparing(Item::getName));

        // Pretty printing
        StringBuilder builder = new StringBuilder();
        for (Item item: listOfItems) {
            builder.append("%-40s%s%n".formatted(item.getItemDTO().getName(), item.getTotalAmount()));
            builder.append("(" + item.getQuantity() + " * " + item.getItemDTO().getPrice() + "/each)\n");
        }
        builder.append("\n");
        builder.append("%-40s%s%n".formatted("Running total:", this.sale.getRunningTotal()));
        builder.append("%-40s%s%n".formatted("Total VAT:", totalVATAmount));
        return builder.toString();
    }

    // TODO Eventuellt ändra till public String createRunningSaleString()
    @Override
    public String toString() {
        List<Item> listOfItems = new ArrayList<>(sale.getCollectionOfItems());

        // Sorterar listan efter när den registrerats
        Collections.sort(listOfItems, Comparator.comparing(Item::getTimeOfUpdate).reversed());

        // Pretty printing
        StringBuilder builder = new StringBuilder();
        for (Item item: listOfItems) {
            builder.append("%-40s%s%n".formatted(item.getItemDTO().getName(), item.getTotalAmount()));
            builder.append("(" + item.getQuantity() + " * " + item.getItemDTO().getPrice() + "/each)\n");
        }
        builder.append("\n");
        builder.append("%-40s%s%n".formatted("Running total:", this.sale.getRunningTotal()));
        return builder.toString();
    }
}
