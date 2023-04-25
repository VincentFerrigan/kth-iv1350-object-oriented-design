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
    }

    public CurrentSaleDTO addItem(ItemDTO itemInfo){
        int key = itemInfo.getItemID();
        if (items.containsKey(key)){
            Item item = this.items.get(key);
            item.increment();
            items.put(key,item);
        } else {
            items.put(key, new Item(itemInfo));
        }
        this.runningTotal = new Amount(itemInfo.getPrice().getAmount());
        Collection<Item> itemCollection = items.values();
        Item[] itemArray = itemCollection.toArray(new Item[0]);
        return new CurrentSaleDTO(itemArray, runningTotal);

        //items.computeIfPresent(key, (key,val) -> val.increment());
        //1. Kolla om den finns i vår "lista"
        //2. Om den finns så anrop increaseQuantity().
        // som uppdaterar antalet av varan i vår "lista"
        // om inte finns, lägg till i vår "lista"
        // 3. Uppdatera runningTotal.
        //4. Skapa en currentSaleDTO och returnera den.
    }

    public CurrentSaleDTO addItem(ItemDTO itemInfo, int quantity){
        int key = itemInfo.getItemID();
        if (items.containsKey(key)){
            Item item = this.items.get(key);
            item.addQuantity(quantity);
            items.put(key,item);

        } else {
            items.put(key, new Item(itemInfo,quantity));
        }
        this.runningTotal = new Amount(itemInfo.getPrice().getAmount() * quantity);
        Collection<Item> itemCollection = items.values();
        Item[] itemArray = itemCollection.toArray(new Item[0]);
        return new CurrentSaleDTO(itemArray, runningTotal);
    }

    private void increaseQuantity(){
        //TODO needs an attribute to increase: ItemDTO?
    }

    private void increaseQuantity(int quantity){
        //TODO what is happening here? is that the attribute? SaleDTO?
    }

    private void calclationOfPrice(){
        this.runningTotal += it
        //TODO needs an attribute OR is this where we use SaleDTO?
    }

    public SaleDTO endSale(){
        //TODO do it
    }

    public SaleDTO applyDiscount(DiscountDTO discount){
        //TODO do it
    }

    public SaleDTO pay(CashPayment payment){
        //TODO also do it
    }
}

