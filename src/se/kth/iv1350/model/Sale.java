package src.se.kth.iv1350.model;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import src.se.kth.iv1350.dto.CurrentSaleDTO;
import src.se.kth.iv1350.dto.DiscountDTO;
import src.se.kth.iv1350.dto.ItemDTO;
import src.se.kth.iv1350.dto.SaleDTO;
import src.se.kth.iv1350.model.Receipt;
import src.se.kth.iv1350.integration.Printer;

public class Sale {
    private LocalDateTime timeOfSale;
    private Amount runningTotal;
    private Map<Integer, Item> items = new HashMap<>();
    private Receipt receipt;            //TODO where is this from?

    /**
     * Create a new instance and saves the time of sale.
     */
    public Sale(){
        this.timeOfSale = LocalDateTime.now();          //TODO where is this from?
        this.runningTotal = new Amount(0);
    }

    public CurrentSaleDTO addItem(ItemDTO itemInfo){
        return addItem(itemInfo, 1);

    }
    public CurrentSaleDTO addItem(ItemDTO itemInfo, int quantity){
        Item additionalItem = new Item(itemInfo, quantity);

        int key = itemInfo.getItemID(); // TODO hämta nyckeln från itemInfo eller additionalItem?
        if (items.containsKey(key)){
            this.items.get(key).addItem(additionalItem);
        } else {
            items.put(key, additionalItem);
        }
        this.runningTotal.addAmount(additionalItem.getTotalAmount());
        Item[] itemArray = getItemArray();

        return new CurrentSaleDTO(itemArray, this.runningTotal);
    }

    private void increaseQuantity(){
        //TODO needs an attribute to increase: ItemDTO?
    }

    private void increaseQuantity(int quantity){
        //TODO what is happening here? is that the attribute? SaleDTO?
    }

    private void calclationOfPrice(){
//        items.forEach();
        //TODO needs an attribute OR is this where we use SaleDTO?
    }

    // Ska den vara sorterad? I sådana fall hur? Eller är det upp till den som kallar?
    private Item[] getItemArray() {
        Collection<Item> itemCollection = items.values();
        return itemCollection.toArray(new Item[0]);
        // TODO
        // Alt 2.
        // return items.values().toArray();
        // Alt 3. Göra en orentlig kopia för att undvika en shallow copy?
        // Alt 4. TODO sorterad? Alfabetiskt? När den las till? I sådana fall behöver vi göra om det hela till en list.

    }

    public SaleDTO endSale(){
        Amount vatAmount = new Amount(0);
        Item[] itemArray = getItemArray();
        for (Item item: itemArray) {
            vatAmount.addAmount(item.getVatAmount());
        }
        Amount amountPaid = new Amount(0);
        Amount changeAmount = new Amount(0);
        return new SaleDTO(runningTotal, itemArray, timeOfSale, vatAmount, amountPaid, changeAmount);
    }

    public SaleDTO applyDiscount(DiscountDTO discount){
            //TODO also do it
        return endSale();
    }

    public SaleDTO pay(CashPayment payment){
        Amount vatAmount = new Amount(0);
        Item[] itemArray = getItemArray();
        for (Item item: itemArray) {
            vatAmount.addAmount(item.getVatAmount());
        }
        CashPayment amountPaid = payment;
        amountPaid.setTotalCost(runningTotal);
        return new SaleDTO(runningTotal, itemArray, timeOfSale, vatAmount, amountPaid.getPaidAmt(), amountPaid.getChange());
        // Tror då att CashPayment behöver kunna subtrahera.
    }
}

