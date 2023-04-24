package src.se.kth.iv1350.dto;
import java.time.LocalDateTime;
import src.se.kth.iv1350.model.Amount;
import src.se.kth.iv1350.model.CashPayment;
import src.se.kth.iv1350.model.Item;


//TODO
public class SaleDTO {
    private Amount totalPrice;
    private Item[] items;
    private LocalDateTime timeOfSale;
    private Amount VATAmount;
    private CashPayment amountPaid;
    private CashPayment changeAmont;







}