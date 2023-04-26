package src.se.kth.iv1350.dto;
import java.time.LocalDateTime;
import src.se.kth.iv1350.model.Amount;
import src.se.kth.iv1350.model.CashPayment;
import src.se.kth.iv1350.model.Item;


//TODO
public class SaleDTO {
    private final Amount totalPrice;
    private final Item[] items;
    private final LocalDateTime timeOfSale; // TODO end of sale tidsstämpel!
    private final Amount vatAmount;
    private final Amount amountPaid;
    private final Amount changeAmount;

    public SaleDTO(Amount totalPrice, Item[] items, LocalDateTime timeOfSale, Amount vatAmount, Amount amountPaid, Amount changeAmount) {
        this.totalPrice = totalPrice;
        this.items = items;
        this.timeOfSale = timeOfSale;
        this.vatAmount = vatAmount;
        this.amountPaid = amountPaid;
        this.changeAmount = changeAmount;
    }
}