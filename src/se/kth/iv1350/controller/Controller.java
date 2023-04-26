package src.se.kth.iv1350.controller;

import src.se.kth.iv1350.dto.CurrentSaleDTO;
import src.se.kth.iv1350.dto.ItemDTO;
import src.se.kth.iv1350.dto.SaleDTO;
import src.se.kth.iv1350.integration.InventorySystem;
import src.se.kth.iv1350.integration.Printer;
import src.se.kth.iv1350.model.CashPayment;
import src.se.kth.iv1350.model.Sale;
import src.se.kth.iv1350.model.Amount;
import src.se.kth.iv1350.integration.SaleLog;

public class Controller {

    private Printer printer;
    private SaleLog saleLog;
    private InventorySystem is = new InventorySystem("src/se/kth/iv1350/startup/inventory_items.csv");

    private Sale currentSale;

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
        this.currentSale = new Sale();
    }

    public CurrentSaleDTO registerItem(int itemID){
        // TODO ändra i UML
        // TODO varför inte bara skicka en ny item?
        ItemDTO itemInfo = is.getItemInfo(itemID);
        return currentSale.addItem(itemInfo);
    }

    public CurrentSaleDTO registerItem(int itemID, int quantity){
        ItemDTO itemInfo = is.getItemInfo(itemID);
        return currentSale.addItem(itemInfo, quantity);
    }

    public SaleDTO endSale(){
        return currentSale.endSale();
    }

    public SaleDTO discountRequest (int customerID){
        //TODO do it
        return currentSale.endSale();
    }

    public Amount pay(Amount paidAmt){
        CashPayment payment = new CashPayment(paidAmt);
        SaleDTO saleInfo = currentSale.pay(payment);
        // TODO
        // 1.3
        // 1.4
        // 1.5
        // 1.6
        // 1.7
        return payment.getChange();
    }

}
