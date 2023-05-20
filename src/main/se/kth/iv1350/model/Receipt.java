package se.kth.iv1350.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * The receipt of a sale
 */
public class Receipt {
    private final Sale sale;
    private final LocalDateTime timeOfSale;
    private Locale locale = new Locale("sv", "SE");
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);
    private Amount totalPricePreDiscount;
    private Amount totalPricePaid;

    /**
     * Creates a new instance of {@link Receipt}.
     * @param sale The sale proved by this receipt.
     */
    Receipt(Sale sale) {
        this.sale = sale;
        this.timeOfSale = LocalDateTime.now();
        totalPricePreDiscount = sale.calculateRunningTotal();
        totalPricePaid = sale.getTotalPricePaid();
    }

    /**
     * Creates a pretty printing -- a well-formatted string
     * with the entire content of the receipt.
     * @return The receipt string.
     */
    @Override
    public String toString() {
        List<ShoppingCartItem> listOfShoppingCartItems = new ArrayList<>(sale.getCollectionOfItems());

        // Sorterar listan per namn
        Collections.sort(listOfShoppingCartItems, Comparator.comparing(ShoppingCartItem::getName));

        // Pretty printing
        StringBuilder builder = new StringBuilder();
        builder.append("%-22s%s%n".formatted("", "RECEIPT"));
        builder.append("%-19s%s%n".formatted("", "Grocery store"));
        builder.append("%-16s%s%n".formatted("", this.timeOfSale.format(formatter)));
        builder.append("\n");
        for (ShoppingCartItem shoppingCartItem : listOfShoppingCartItems) {
            builder.append("%-40s%s%n".formatted(shoppingCartItem.getName(), shoppingCartItem.getTotalSubPrice()));
            builder.append("(%d * %s)\n".formatted(shoppingCartItem.getQuantity(), shoppingCartItem.getUnitPrice()));
        }
        builder.append("\n");
        if (!totalPricePaid.equals(totalPricePreDiscount)) {
            builder.append("%-40s-%s%n".formatted("Total discount:", totalPricePreDiscount.minus(totalPricePaid)));
        }
        builder.append("%-40s%s%n".formatted("Total Cost:", totalPricePaid));
        builder.append("%-40s%s%n".formatted("Total VAT:", this.sale.getTotalVATCosts()));
        builder.append("\n");
        builder.append("%-40s%s%n".formatted("Paid Amount:", this.sale.getPayment().getPaidAmt()));
        builder.append("%-40s%s%n".formatted("Change:", this.sale.getPayment().getChange()));
        return builder.toString();
    }
}