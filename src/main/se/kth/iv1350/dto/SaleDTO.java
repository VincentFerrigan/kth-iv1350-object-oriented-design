package se.kth.iv1350.dto;
import java.util.List;
import se.kth.iv1350.model.Amount;


// TODO ska discountDTO finnas med i SaleDTO och/eller SaleItemDTO?
public class SaleDTO {
    private final List<SaleItemDTO> saleItemsInfo;
    private final Amount totalPrice;
    private final Amount totalVATAmount;
    public SaleDTO(List<SaleItemDTO> saleItemsInfo, Amount totalPrice, Amount totalVATAmount) {
        this.saleItemsInfo = saleItemsInfo;
        this.totalPrice = totalPrice;
        this.totalVATAmount = totalVATAmount;
    }
    public SaleDTO(SaleDTO other) {
        this.saleItemsInfo = other.saleItemsInfo;
        this.totalPrice = other.totalPrice;
        this.totalVATAmount = other.totalVATAmount;
    }

    public List<SaleItemDTO> getSaleItemsInfo() {
        return saleItemsInfo;
    }

    public Amount getTotalPrice() {
        return totalPrice;
    }

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