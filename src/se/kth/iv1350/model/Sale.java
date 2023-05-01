package src.se.kth.iv1350.model;
import java.time.LocalDateTime;
import java.util.*;

import src.se.kth.iv1350.dto.DiscountDTO;
import src.se.kth.iv1350.dto.ItemDTO;
import src.se.kth.iv1350.integration.Display;
import src.se.kth.iv1350.integration.ItemRegistry;
import src.se.kth.iv1350.integration.Printer;

import static java.util.stream.Collectors.toList;

/**
 * Represent a particular sale.
 */
public class Sale {
    // ska timeOfSale vara final?
    private LocalDateTime timeOfSale;
    private Map<Integer, Item> shoppingCart = new HashMap<>(); // TODO Ändra namn till shoppingCart?
    private CashPayment payment;

    private DiscountDTO discount = new DiscountDTO();

    // TODO ska ett discount attribute finnas med i både sale och saleDTO?
    // TODO Ska den vara en tabell av rabatter, procentssats, belopp eller själva discountDTO?
    // TODO Total cost - Total discount = total price? (Per vara eller hela försäljningen?)

    private ItemRegistry itemRegistry; // För att kunna plocka från "lagret". Men då måste 'is' skickas med från kontrollern när Sale instansieras.

    /**
     * Create a new instance, representing a sale made by a customer.
     */
    public Sale(ItemRegistry itemRegistry){
        this.timeOfSale = LocalDateTime.now();
        this.itemRegistry = itemRegistry;
    }

    // TODO varför inte Item eller itemID istället för ItemDTO?
    public void addItem(int itemID, int quantity) {
        if (shoppingCart.containsKey(itemID)) {
            this.shoppingCart.get(itemID).addToQuantity(quantity);
        }
        else {
            ItemDTO itemInfo = itemRegistry.getItemInfo(itemID);
            Item item = new Item(itemInfo, quantity);
            shoppingCart.put(itemID, item);
        }
    }
    public void addItem(int itemID) {
        addItem(itemID, 1);
    }

    public void addItem(ItemDTO itemInfo){
        addItem(itemInfo, 1);
    }
    public void addItem(ItemDTO itemInfo, int quantity){
        Item item = new Item(itemInfo, quantity);

        int key = itemInfo.getItemID(); // TODO hämta nyckeln från itemInfo eller additionalItem?
        if (shoppingCart.containsKey(key)){
            this.shoppingCart.get(key).addItem(item);
        } else {
            shoppingCart.put(key, item);
        }
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
        // Totalbelopp
        Amount runningTotal = new Amount(0);
        List<Amount> totalPrices = getCollectionOfItems()
                .stream()
                .map(Item::getTotalPrice)
                .collect(toList());
        runningTotal = runningTotal.plus(totalPrices);
        runningTotal = runningTotal.multiply(discount.getDiscountMultiplier());

        return runningTotal;
    }

    public Amount getTotalVATAmount() {
        // Momsberäkning
        Amount totalVATAmount = new Amount(0);
        List<Amount> vatAmounts = getCollectionOfItems().stream().map(Item::getVatAmount).collect(toList());
        totalVATAmount = totalVATAmount.plus(vatAmounts);
        totalVATAmount = totalVATAmount.multiply(discount.getDiscountMultiplier());

        return totalVATAmount;
    }

    Collection<Item> getCollectionOfItems() {
        return shoppingCart.values();
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
        this.discount = discount;
    }

    public void pay(CashPayment payment) {
        payment.calculateTotalCost(this);
        this.payment = payment;
    }
    public void printReceipt(Printer printer) {
        Receipt receipt = new Receipt(this);
        printer.printReceipt(receipt);
    }

//    public SaleDTO displayOpenSale(Display display) {
    public void displayOpenSale(Display display) {
        SaleOutput saleOutput = new SaleOutput(this);
        display.displayOpenSale(saleOutput);
//        return saleOutput.getSaleInfo();
    }

//    public SaleDTO displayCheckout(Display display) {
    public void displayCheckout(Display display) {
        SaleOutput saleOutput = new SaleOutput(this);
        display.displayCheckout(saleOutput);
//        return saleOutput.getSaleInfo();
    }

    public void updateInventory() {
        itemRegistry.updateInventory(getCollectionOfItems());
    }
}