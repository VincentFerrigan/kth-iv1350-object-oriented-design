package src.se.kth.iv1350.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

// TODO package private kontruktor och final attribute?

/**
 * The receipt of a sale
 */
public class Receipt {
    private final Sale sale;

    /**
     * Creates a new instance of {@link Receipt}.
     * @param sale The sale proved by this receipt.
     */
    Receipt(Sale sale){
        this.sale = sale;
    }

    /**
     * Creates a pretty printing -- a well-formatted string
     * with the entire content of the receipt.
     * @return The receipt string.
     */
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
            builder.append("%-40s%s%n".formatted(item.getItemDTO().getName(), item.getTotalAmount()));
            builder.append("(" + item.getQuantity() + " * " + item.getItemDTO().getPrice() + "/each)\n");
        }
        builder.append("\n");
        builder.append("%-40s%s%n".formatted("Total Cost:", this.sale.getPayment().getTotalCost()));
        builder.append("%-40s%s%n".formatted("Total VAT:", totalVATAmount));
        builder.append("%-40s%s%n".formatted("Paid Amount:", this.sale.getPayment().getPaidAmt()));
        builder.append("%-40s%s%n".formatted("Change:", this.sale.getPayment().getChange()));
        return builder.toString();
    }
}