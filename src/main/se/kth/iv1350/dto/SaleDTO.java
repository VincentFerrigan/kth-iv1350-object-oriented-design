package se.kth.iv1350.dto;
import java.util.List;
import se.kth.iv1350.model.Amount;


// TODO ska discountDTO finnas med i SaleDTO och/eller SaleItemDTO?

/**
 * Contains information about on particular Sale.
 */
public class SaleDTO {
    private final List<SaleItemDTO> saleItemsInfo;
    private final Amount totalPrice;
    private final Amount totalVATAmount;

    /**
     * Creates a new instance representing a particular sale.
     * @param saleItemsInfo The shopping cart as a list of Sale Item Data Transfer Objects
     * @param totalPrice The total price
     * @param totalVATAmount The total VAT amount
     */
    public SaleDTO(List<SaleItemDTO> saleItemsInfo, Amount totalPrice, Amount totalVATAmount) {
        this.saleItemsInfo = saleItemsInfo;
        this.totalPrice = totalPrice;
        this.totalVATAmount = totalVATAmount;
    }

    /**
     * Creates a copy of an instance representing a particular sale.
     * @param other
     */
    public SaleDTO(SaleDTO other) {
        this.saleItemsInfo = other.saleItemsInfo;
        this.totalPrice = other.totalPrice;
        this.totalVATAmount = other.totalVATAmount;
    }

    /**
     * Gets the shopping cart as a list of sale item data transfer objects
     * @return the shopping cart as a list of sale item data transfer objets
     */
    public List<SaleItemDTO> getSaleItemsInfo() {
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

    @Override
    public String toString() {
        // Pretty printing
        StringBuilder builder = new StringBuilder();
        for (SaleItemDTO itemInfo: saleItemsInfo) {
            builder.append("%-40s%s%n".formatted(itemInfo.getName(), itemInfo.getTotalPrice()));
            builder.append("(%d * %s)\n".formatted(itemInfo.getQuantity(), itemInfo.getUnitPrice()));
        }
        return builder.toString();
    }
}