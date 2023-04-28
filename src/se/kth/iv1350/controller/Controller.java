package src.se.kth.iv1350.controller;

import src.se.kth.iv1350.dto.DiscountDTO;
import src.se.kth.iv1350.dto.ItemDTO;
import src.se.kth.iv1350.integration.DiscountRegister;
import src.se.kth.iv1350.integration.InventorySystem;
import src.se.kth.iv1350.integration.Printer;
import src.se.kth.iv1350.model.CashPayment;
import src.se.kth.iv1350.model.CashRegister;
import src.se.kth.iv1350.model.Sale;
import src.se.kth.iv1350.model.Amount;
import src.se.kth.iv1350.integration.SaleLog;

public class Controller {
    private Printer printer;
    private SaleLog saleLog;
    private InventorySystem is = new InventorySystem("src/se/kth/iv1350/startup/inventory_items.csv");
    private CashRegister cashRegister;
    private DiscountRegister discountRegister = new DiscountRegister();
    private Sale currentSale;

    /**
     * Constructor
     * @param printer The printer.
     * @param saleLog The object that loggs all transactions.
     */
    public Controller (Printer printer, SaleLog saleLog){
        this.printer = printer;
        this.saleLog = saleLog;
        this.cashRegister = new CashRegister();
    }

    /**
     * Start a new sale. This method must be called before doing anything else during a sale.
     */
    public void startSale(){
        this.currentSale = new Sale();
    }

    // TODO ändra i UML
    // TODO varför inte bara skicka en ny item?
    // TODO Ska den inte först kontrollera om itemID finns i shopping cart?
//    public CurrentSaleDTO registerItem(int itemID){
    public void registerItem(int itemID){
        registerItem(itemID, 1);
    }

    public void registerItem(int itemID, int quantity){
        ItemDTO itemInfo = is.getItemInfo(itemID);
        currentSale.addItem(itemInfo, quantity);
        currentSale.displayCurrentSale(printer);
    }

    public void endSale(){
        currentSale.endSale();
        currentSale.displayEndOfSale(printer);
    }

    public void discountRequest (int customerID){
        //TODO do it
        DiscountDTO discountDTO = discountRegister.getDiscount(customerID);
        currentSale.applyDiscount(discountDTO);
        currentSale.endSale();
        currentSale.displayEndOfSale(printer);
    }

    public void pay(Amount paidAmt){
        CashPayment payment = new CashPayment(paidAmt);
        currentSale.pay(payment);
        // TODO
        cashRegister.addPayment(payment);
        saleLog.logSale(currentSale);
        // 1.5 UpdateInventory   // TODO till txt fil?
        // 1.6 AccountingSystem.updateToAccounting   // TODO till txt fil?
        currentSale.printReceipt(printer);
        currentSale = null;
    }
}