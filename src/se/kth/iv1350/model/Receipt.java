package src.se.kth.iv1350.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

// TODO package private kontruktor och final attribute?
public class Receipt {
    private final Sale sale;
    Receipt(Sale sale){
        this.sale = sale;
    }
    @Override
    public String toString() {
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
            builder.append("Item: " + item.getItemDTO().getName() + ", "); //TODO är det namn eller description
            builder.append(item.getTotalAmount());
            builder.append("\n");
        }
        builder.append("Total VAT:\t" + totalVATAmount);
        builder.append("\n");
        builder.append("Total cost:\t" + this.sale.getPayment().getTotalCost());
        builder.append("\n");
        builder.append("Paid amount:\t " + this.sale.getPayment().getPaidAmt());
        builder.append("\n");
        builder.append("Change:\t" + this.sale.getPayment().getChange());
        builder.append("\n");
        return builder.toString();
    }
}