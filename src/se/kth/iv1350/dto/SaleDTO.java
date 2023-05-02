package src.se.kth.iv1350.dto;
import java.time.LocalDateTime;
import java.util.List;
import src.se.kth.iv1350.model.Amount;


// TODO ska discountDTO finnas med i SaleDTO och/eller SaleItemDTO?
public class SaleDTO {
    private final LocalDateTime timeOfSale;
    private final List<SaleItemDTO> saleItemsInfo;
    private final Amount totalPrice;
    private final Amount totalVATAmount;
    public SaleDTO(List<SaleItemDTO> saleItemsInfo, Amount totalPrice, Amount totalVATAmount) {
        this.timeOfSale = LocalDateTime.now();
        this.saleItemsInfo = saleItemsInfo;
        this.totalPrice = totalPrice;
        this.totalVATAmount = totalVATAmount;
    }
    public SaleDTO(SaleDTO other) {
        this.timeOfSale = other.timeOfSale;
        this.saleItemsInfo = other.saleItemsInfo;
        this.totalPrice = other.totalPrice;
        this.totalVATAmount = other.totalVATAmount;
    }

    public LocalDateTime getTimeOfSale() {
        return timeOfSale;
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
            builder.append("(" + itemInfo.getQuantity() + " * " + itemInfo.getUnitPrice() + "/st.)\n");
        }
//        builder.append("\n");
//        builder.append("%-40s%s%n".formatted("Running total:",totalPrice));
        return builder.toString();
    }
}