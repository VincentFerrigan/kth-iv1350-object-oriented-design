package src.se.kth.iv1350.model;
import java.time.LocalDateTime;

import src.se.kth.iv1350.dto.DiscountDTO;
import src.se.kth.iv1350.dto.ItemDTO;
import src.se.kth.iv1350.dto.SaleDTO;
import src.se.kth.iv1350.model.Receipt;
import src.se.kth.iv1350.integration.Printer;

public class Sale {
    private LocalDateTime timeOfSale;
    private Amount runningTotal;
    private Item[] items;
    private Receipt receipt;            //TODO where is this from?

    /**
     * Create a new instance and saves the time of sale.
     */
    public Sale(){
        this.timeOfSale = LocalDateTime.now();          //TODO where is this from?
    }

    public SaleDTO addItem(ItemDTO itemInfo){
        //TODO also do it
    }

    public SaleDTO addItem(ItemDTO itemInfo, int quantity){
        //TODO needs to be done
    }

    private void increaseQuantity(){
        //TODO needs an attribute to increase: SaleDTO?
    }

    private void increaseQuantity(int quantity){
        //TODO what is happening here? is that the attribute? SaleDTO?
    }

    private void calclationOfPrice(){
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

