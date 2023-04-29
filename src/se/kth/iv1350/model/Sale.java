package src.se.kth.iv1350.model;
import java.time.LocalDateTime;
import java.util.*;

import src.se.kth.iv1350.dto.DiscountDTO;
import src.se.kth.iv1350.dto.ItemDTO;
import src.se.kth.iv1350.integration.Printer;

/**
 * Represent a particular sale.
 */
public class Sale {
    private LocalDateTime timeOfSale;
    private Amount runningTotal;
    private Map<Integer, Item> items = new HashMap<>(); // TODO Ändra namn till shoppingCart?
    private CashPayment payment;

    // TODO ska ett discount attribute finnas med i både sale och saleDTO?
    // TODO Ska den vara en tabell av rabatter, procentssats, belopp eller själva discountDTO?
    // TODO Total cost - Total discount = total price? (Per vara eller hela försäljningen?)

//    private InventorySystem is; // För att kunna plocka från "lagret". Men då måste 'is' skickas med från kontrollern när Sale instansieras.

    /**
     * Create a new instance, representing a sale made by a customer.
     */
    public Sale(){
        this.timeOfSale = LocalDateTime.now();
        this.runningTotal = new Amount(0);
    }

    // TODO varför inte Item istället för ItemDTO?
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
        this.runningTotal = this.runningTotal.plus(additionalItem.getTotalPrice());
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

    Collection<Item> getCollectionOfItems() {
        return items.values();
    }

    // TODO Bör nog ändras. Samma upplägg som Display. Logging kan ske med hjälp av SaleLog.
    public void endSale(){

        //TODO also do it
//        Amount totalVATAmount = new Amount(0);
//        Item[] itemArray = getItemArraySortedByItemName();
//        List<Amount> vatAmounts = items.values().stream().map(Item::getVatAmount).collect(toList());
//        totalVATAmount = totalVATAmount.plus(vatAmounts);
//        return new SaleDTO(runningTotal, itemArray, timeOfSale, totalVATAmount);
    }

    public void applyDiscount(DiscountDTO discount){
            //TODO also do it
    }

    public void pay(CashPayment payment) {
        payment.calculateTotalCost(this);
        this.payment = payment;
    }
    public void printReceipt(Printer printer) {
        Receipt receipt = new Receipt(this);
        printer.printReceipt(receipt);
    }

    public void displayCurrentSale(Printer printer) {
        Display display = new Display(this);
        printer.printCurrentSale(display);
    }

    public void displayEndOfSale(Printer printer) {
        Display display = new Display(this);
        printer.printEndOfSale(display);
    }
}