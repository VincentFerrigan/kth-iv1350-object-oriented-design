package src.se.kth.iv1350.model;
import java.time.LocalDateTime;
import java.util.*;
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
        Item[] itemArray = getItemArraySortedByTimeOfUpdate();
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

    // Alt 4. TODO sorterad? Alfabetiskt? När den las till? I sådana fall behöver vi göra om det hela till en list.
    // Alt 3. Göra en orentlig kopia för att undvika en shallow copy?
    private Item[] getItemArray() {
        Collection<Item> itemCollection = items.values();
        return itemCollection.toArray(new Item[0]);
    }

    private Item[] getItemArraySortedByItemID() {
        List<Item> listOfItems = new ArrayList<>(items.values());
        Collections.sort(listOfItems, Comparator.comparing(Item::getItemID));
        return listOfItems.toArray(new Item[0]);
    }
    private Item[] getItemArraySortedByTimeOfUpdate() {
        List<Item> listOfItems = new ArrayList<>(items.values());
//        Collections.sort(listOfItems, Comparator.comparing(Item::getTimeOfUpdate));
//        Collections.reverse(listOfItems);
        Collections.sort(listOfItems, Comparator.comparing(Item::getTimeOfUpdate).reversed());
        return listOfItems.toArray(new Item[0]);
    }

    private Item[] getItemArraySortedByItemName() {
        List<Item> listOfItems = new ArrayList<>(items.values());
        Collections.sort(listOfItems, Comparator.comparing(Item::getName));
        return listOfItems.toArray(new Item[0]);
    }

    public SaleDTO endSale(){
        Amount vatAmount = new Amount(0);
        Item[] itemArray = getItemArraySortedByItemName();
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
        Item[] itemArray = getItemArraySortedByItemName(); // TODO hur vill du ha den sorterad?
        for (Item item: itemArray) {
            vatAmount.addAmount(item.getVatAmount());
        }
        CashPayment amountPaid = payment;
        amountPaid.setTotalCost(runningTotal);
        return new SaleDTO(runningTotal, itemArray, timeOfSale, vatAmount, amountPaid.getPaidAmt(), amountPaid.getChange());
        // Tror då att CashPayment behöver kunna subtrahera.
    }
}

