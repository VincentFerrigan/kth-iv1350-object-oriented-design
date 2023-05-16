package se.kth.iv1350.model;
import java.util.List;


// TODO ska discountDTO finnas med i SaleDTO och/eller ShoppingCartItemDTO?

/**
 * Contains information about on particular Sale as a Data Transfer Object.
 */
public class SaleDTO {
    private final List<ShoppingCartItemDTO> saleItemsInfo;
    private final Amount totalPrice;
    private final Amount totalVATAmount;
    private final Amount totalDiscounts;

    /**
     * Creates a new instance representing a particular sale.
     * @param saleItemsInfo The shopping cart as a list of Sale ShoppingCartItem Data Transfer Objects
     * @param totalPrice The total price
     * @param totalVATAmount The total VAT amount
     * @param totalDiscounts The total discount amount if any.
     */
    public SaleDTO(List<ShoppingCartItemDTO> saleItemsInfo,
                   Amount totalPrice, Amount totalVATAmount, Amount totalDiscounts) {
        this.saleItemsInfo = saleItemsInfo;
        this.totalPrice = totalPrice;
        this.totalVATAmount = totalVATAmount;
        this.totalDiscounts = totalDiscounts;
    }

    /**
     * Creates a copy of an instance representing a particular sale.
     * @param other Sale information as Data Transfer Object
     */
    public SaleDTO(SaleDTO other) {
        this.saleItemsInfo = other.saleItemsInfo;
        this.totalPrice = other.totalPrice;
        this.totalVATAmount = other.totalVATAmount;
        this.totalDiscounts = other.totalDiscounts;
    }

    /**
     * Gets the shopping cart as a list of sale item data transfer objects
     * @return the shopping cart as a list of sale item data transfer objets
     */
    public List<ShoppingCartItemDTO> getSaleItemsInfo() {
        return saleItemsInfo;
    }

    /**
     * Get the total price of a sale
     * @return the total price of a sale
     */
    public Amount getTotalPrice() {
        return totalPrice;
    }

    /**
     * Get the total VAT of a sale
     * @return the total VAT of a sale
     */
    public Amount getTotalVATAmount() {
        return totalVATAmount;
    }

    /**
     * Get the total discounts of a sale
     * @return the total discounts of a sale
     */
    public Amount getTotalDiscounts() {
        return totalDiscounts;
    }

    @Override
    public String toString() {
        // Pretty printing
        StringBuilder builder = new StringBuilder();
        for (ShoppingCartItemDTO itemInfo: saleItemsInfo) {
            builder.append("%-40s%s%n".formatted(itemInfo.getName(), itemInfo.getTotalPrice()));
            builder.append("(%d * %s)\n".formatted(itemInfo.getQuantity(), itemInfo.getUnitPrice()));
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SaleDTO)) return false;

        SaleDTO that = (SaleDTO) o;

        if (!(saleItemsInfo.equals(that.saleItemsInfo))) return false;
        if (!(totalPrice.equals(that.totalPrice))) return false;
        return totalVATAmount.equals(that.totalVATAmount);
    }
}