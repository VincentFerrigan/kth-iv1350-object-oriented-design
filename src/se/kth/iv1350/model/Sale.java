package src.se.kth.iv1350.model;
import java.time.LocalDateTime;
import java.util.*;

import src.se.kth.iv1350.dto.DiscountDTO;
import src.se.kth.iv1350.dto.ItemDTO;
import src.se.kth.iv1350.dto.SaleDTO;
import src.se.kth.iv1350.integration.Printer;

import static java.util.stream.Collectors.toList;

public class Sale {
    private LocalDateTime timeOfSale;
    private Amount runningTotal;
    private Map<Integer, Item> items = new HashMap<>(); // TODO Ändra namn till shoppingCart?
    private CashPayment payment;

//    private InventorySystem is; // För att kunna plocka från "lagret". Men då måste 'is' skickas med från kontrollern när Sale instansieras.

    /**
     * Create a new instance and saves the time of sale.
     */
    public Sale(){
        this.timeOfSale = LocalDateTime.now();          //TODO where is this from?
        this.runningTotal = new Amount(0);
    }

    public void addItem(ItemDTO itemInfo){
        addItem(itemInfo, 1);

    }
    public void addItem(ItemDTO itemInfo, int quantity){
        Item additionalItem = new Item(itemInfo, quantity);

        int key = itemInfo.getItemID(); // TODO hämta nyckeln från itemInfo eller additionalItem?
        if (items.containsKey(key)){
            this.items.get(key).addItem(additionalItem);
        } else {
            items.put(key, additionalItem);
        }
        this.runningTotal = this.runningTotal.plus(additionalItem.getTotalAmount());
    }

    private void increaseQuantity(){
        //TODO needs an attribute to increase: ItemDTO?
    }

    private void increaseQuantity(int quantity){
        //TODO what is happening here? is that the attribute? SaleDTO?
    }

    CashPayment getPayment(){
        return payment;
    }
    public Amount getRunningTotal() {
        return runningTotal;
    }

    private Item[] getItemArray() {
        Collection<Item> itemCollection = items.values();
        return itemCollection.toArray(new Item[0]);
    }

    Collection<Item> getCollectionOfItems() {
        return items.values();
    }

    // TODO. Bör tas bort/flyttas för att få High Cohesion.
    private Item[] getItemArraySortedByItemName() {
        List<Item> listOfItems = new ArrayList<>(items.values());
        Collections.sort(listOfItems, Comparator.comparing(Item::getName));
        return listOfItems.toArray(new Item[0]);
    }

    // TODO Bör nog ändras. Samma upplägg som Display. Logging kan ske med hjälp av SaleLog.
    public SaleDTO endSale(){
        Amount totalVATAmount = new Amount(0);
        Item[] itemArray = getItemArraySortedByItemName();
        List<Amount> vatAmounts = items.values().stream().map(Item::getVatAmount).collect(toList());
        totalVATAmount = totalVATAmount.plus(vatAmounts);
//        Amount amountPaid = new Amount(0);
//        Amount changeAmount = new Amount(0);
//        return new SaleDTO(runningTotal, itemArray, timeOfSale, totalVATAmount, amountPaid, changeAmount);
        return new SaleDTO(runningTotal, itemArray, timeOfSale, totalVATAmount);
    }

    public SaleDTO applyDiscount(DiscountDTO discount){
            //TODO also do it
        return endSale();
    }


    public void pay(CashPayment payment) {
        payment.calculateTotalCost(this);
        this.payment = payment;
    }
    public void printReceipt(Printer printer) {
        Receipt receipt = new Receipt(this);
        printer.print(receipt);
    }

    public void displayCurrentSale(Printer printer) {
        Display display = new Display(this);
        printer.print(display);
    }
//    public SaleDTO pay2(CashPayment payment){
////        Amount vatAmount = new Amount(0);
////        Item[] itemArray = getItemArraySortedByItemName(); // TODO hur vill du ha den sorterad?
////        for (Item item: itemArray) {
////            vatAmount.addAmount(item.getVatAmount());
////        }
//        Amount totalVATAmount = new Amount(0);
//        Item[] itemArray = getItemArraySortedByItemName();
//        List<Amount> vatAmounts = items.values().stream().map(Item::getVatAmount).collect(toList());
//        totalVATAmount = totalVATAmount.plus(vatAmounts);
//        CashPayment amountPaid = payment;
//        amountPaid.calculateTotalCost(this);
////        amountPaid.setTotalCost(runningTotal);
//        return new SaleDTO(runningTotal, itemArray, timeOfSale, totalVATAmount, amountPaid.getPaidAmt(), amountPaid.getChange());
//        // Tror då att CashPayment behöver kunna subtrahera.
//    }
}