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
        int key = itemInfo.getItemID();
        if (items.containsKey(key)){
            Item item = this.items.get(key);
            item.increment();
            items.put(key,item);
        } else {
            items.put(key, new Item(itemInfo));
        }

        this.runningTotal = new Amount(this.runningTotal.getAmount() + itemInfo.getPrice().getAmount());
        Collection<Item> itemCollection = items.values();
        Item[] itemArray = itemCollection.toArray(new Item[0]);
        return new CurrentSaleDTO(itemArray, this.runningTotal);

        //items.computeIfPresent(key, (key,val) -> val.increment());
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

        this.runningTotal = new Amount(this.runningTotal.getAmount() + itemInfo.getPrice().getAmount());
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
        //TODO needs an attribute OR is this where we use SaleDTO?
    }

    public SaleDTO endSale(){
        //TODO do it
        return new SaleDTO();
    }

    public SaleDTO applyDiscount(DiscountDTO discount){
            //TODO also do it
        return new SaleDTO();
    }

    public SaleDTO pay(CashPayment payment){
        //TODO also do it
        return new SaleDTO();
    }
}

