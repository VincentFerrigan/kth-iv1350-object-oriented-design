package src.se.kth.iv1350.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// TODO package private kontruktor och final attribute?
public class Display {
    private final Sale sale;
    Display(Sale sale) {
        this.sale = sale;
    }

    @Override
    public String toString() {
        List<Item> listOfItems = new ArrayList<>(sale.getCollectionOfItems());

        // Sorterar listan efter när den registrerats
        Collections.sort(listOfItems, Comparator.comparing(Item::getTimeOfUpdate).reversed());

        // Pretty printing
        StringBuilder builder = new StringBuilder();
        for (Item item: listOfItems) {
            builder.append(item.getItemDTO().getName() + ", "); //TODO är det namn eller description
            builder.append(item.getTotalAmount());
            builder.append("\n");
        }
        builder.append("\n");
        builder.append("Running total: " + this.sale.getRunningTotal());
        return builder.toString();
    }
}
