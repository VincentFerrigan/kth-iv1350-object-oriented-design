package src.se.kth.iv1350.controller;

import src.se.kth.iv1350.dto.SaleDTO;
import src.se.kth.iv1350.integration.Printer;
import src.se.kth.iv1350.model.Receipt;
import src.se.kth.iv1350.model.Sale;
import src.se.kth.iv1350.model.Amount;
import src.se.kth.iv1350.integration.SaleLog;

public class Controller {

    //TODO where are these from?
    public Printer printer;
    public SaleLog saleLog;
//    private Sale sale;


    /**
     * Constructor
     * @param printer
     * @param saleLog
     */
    public Controller (Printer printer, SaleLog saleLog){
        this.printer = printer;
        this.saleLog = saleLog;
    }

    /**
     * Start a new sale. This method must be called before doing anything else during a sale.
     */
    public void startSale(){
//        sale = new Sale();          //TODO också va?
    }

    public SaleDTO registerItem(int itemID){
        //TODO do it
    }

    public SaleDTO registerItem(int itemID, int quantity){
        //TODO do it
    }

    public SaleDTO endSale(){
        //TODO do it
    }

    public SaleDTO discountRequest (int customerID){
        //TODO do it
    }

    public void pay(Amount paidAmt){
        //TODO do it
    }

}
