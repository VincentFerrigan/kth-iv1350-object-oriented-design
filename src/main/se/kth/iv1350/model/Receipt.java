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
    private List<ShoppingCartItem> listOfShoppingCartItems;
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
        listOfShoppingCartItems = new ArrayList<>(sale.getCollectionOfItems());
        sortShoppingCart(listOfShoppingCartItems);
        totalPricePreDiscount = sale.calculateRunningTotal();
        totalPricePaid = sale.getTotalPricePaid();
    }

    private void sortShoppingCart(List<ShoppingCartItem> listOfShoppingCartItems) {
        Collections.sort(listOfShoppingCartItems, Comparator.comparing(ShoppingCartItem::getName));
    }
    private String createSaleItemsInfoString() {
        // Pretty printing
        StringBuilder saleItemsInfoBuilder = new StringBuilder();
        for (ShoppingCartItem item : listOfShoppingCartItems) {
            saleItemsInfoBuilder.append("%-40s%s".formatted(item.getName(), item.getTotalSubPrice()));
            saleItemsInfoBuilder.append("%n".formatted());
            saleItemsInfoBuilder.append("(%d * %s)".formatted(item.getQuantity(), item.getUnitPriceIncVAT()));
            saleItemsInfoBuilder.append("%n".formatted());
        }
        saleItemsInfoBuilder.append("%n".formatted());
        return saleItemsInfoBuilder.toString();
    }

    private String createDiscountInfoString(){
        StringBuilder discountInfoBuilder = new StringBuilder();
        if (!totalPricePaid.equals(totalPricePreDiscount)) {
            discountInfoBuilder.append("%-40s-%s".formatted("Total discount:", sale.getDiscount()));
            discountInfoBuilder.append("%n".formatted());
            discountInfoBuilder.append("(%s)".formatted(sale.createStringDiscountInfo()));
            discountInfoBuilder.append("%n".formatted());
            discountInfoBuilder.append("%n".formatted());
        }
        return discountInfoBuilder.toString();
    }

    /**
     * Creates a pretty printing -- a well-formatted string
     * with the entire content of the receipt.
     * @return The receipt string.
     */
    @Override
    public String toString() {
        // Pretty printing
        StringBuilder receiptBuilder = new StringBuilder();
        receiptBuilder.append("%-22s%s".formatted("", "RECEIPT"));
        receiptBuilder.append("%n".formatted());
        receiptBuilder.append("%-19s%s".formatted("", "Grocery store"));
        receiptBuilder.append("%n".formatted());
        receiptBuilder.append("%-16s%s".formatted("", this.timeOfSale.format(formatter)));
        receiptBuilder.append("%n".formatted());
        receiptBuilder.append("%n".formatted());

        receiptBuilder.append(createSaleItemsInfoString());
        receiptBuilder.append(createDiscountInfoString());

        receiptBuilder.append("%-40s%s".formatted("Total Cost:", totalPricePaid));
        receiptBuilder.append("%n".formatted());
        receiptBuilder.append("%-40s%s".formatted("Total VAT:", this.sale.getTotalVATCosts()));
        receiptBuilder.append("%n".formatted());
        receiptBuilder.append("%n".formatted());
        receiptBuilder.append("%-40s%s".formatted("Paid Amount:", this.sale.getPayment().getPaidAmt()));
        receiptBuilder.append("%n".formatted());
        receiptBuilder.append("%-40s%s".formatted("Change:", this.sale.getPayment().getChange()));
        receiptBuilder.append("%n".formatted());
        return receiptBuilder.toString();
    }
}