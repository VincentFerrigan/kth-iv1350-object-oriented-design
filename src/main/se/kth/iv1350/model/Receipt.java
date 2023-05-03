package se.kth.iv1350.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import static java.util.stream.Collectors.toList;

// TODO package private kontruktor och final attribute?

/**
 * The receipt of a sale
 */
public class Receipt {
    private final Sale sale;
    private final LocalDateTime timeOfSale;
    private Locale locale = new Locale("sv", "SE");
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);

    /**
     * Creates a new instance of {@link Receipt}.
     * @param sale The sale proved by this receipt.
     */
    Receipt(Sale sale) {
        this.sale = sale;
        this.timeOfSale = LocalDateTime.now();
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
        Amount totalVATAmount = sale.getTotalVATAmount();

        // Sorterar listan per namn
        Collections.sort(listOfItems, Comparator.comparing(Item::getName));

        // Pretty printing
        StringBuilder builder = new StringBuilder();
        builder.append("%-22s%s%n".formatted("", "RECEIPT"));
        builder.append("%-19s%s%n".formatted("", "Grocery store"));
        builder.append("%-16s%s%n".formatted("", this.timeOfSale.format(formatter)));
        builder.append("\n");
        for (Item item: listOfItems) {
            builder.append("%-40s%s%n".formatted(item.getName(), item.getTotalPrice()));
            builder.append("(" + item.getQuantity() + " * " + item.getUnitPrice() + "/each)\n");
        }
        builder.append("\n");
        builder.append("%-40s%s%n".formatted("Total Cost:", this.sale.getPayment().getTotalCost()));
        builder.append("%-40s%s%n".formatted("Total VAT:", totalVATAmount));
        builder.append("\n");
        builder.append("%-40s%s%n".formatted("Paid Amount:", this.sale.getPayment().getPaidAmt()));
        builder.append("%-40s%s%n".formatted("Change:", this.sale.getPayment().getChange()));
        return builder.toString();
    }
}