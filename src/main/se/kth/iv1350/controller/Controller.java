package se.kth.iv1350.controller;

import se.kth.iv1350.dto.DiscountDTO;
import se.kth.iv1350.dto.ItemDTO;
import se.kth.iv1350.dto.SaleDTO;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.model.CashPayment;
import se.kth.iv1350.model.CashRegister;
import se.kth.iv1350.model.Sale;
import se.kth.iv1350.model.Amount;

// TODO Registry createor for external database setup?????????? Use getters. What about salelog?
/**
 * This is the application's only controller class. All calls to the model pass
 * through here.
 */
public class Controller {
    private Printer printer;
    private Display display;
    private SaleLog saleLog;
    private ItemRegistry itemRegistry;
    private DiscountRegister discountRegister;
    private AccountingSystem accountingSystem;
    private CashRegister cashRegister;
    private Sale currentSale;

    /**
     * Creates a new instance.
     * @param printer Interface to printer (prints receipts and display)
     * @param registerCreator Used to get all classes that handle database calls.
     */
    public Controller (Printer printer, Display display, RegisterCreator registerCreator){
        this.printer = printer;
        this.display = display;
        this.saleLog = registerCreator.getSaleLog();
        this.itemRegistry = registerCreator.getInventorySystem();
        this.discountRegister = registerCreator.getDiscountRegister();
        this.accountingSystem = registerCreator.getAccountingSystem();
        this.cashRegister = new CashRegister(CashRegister.INITIAL_BALANCE);
    }

    /**
     * Start a new sale. This method must be called before doing anything else during a sale.
     */
    public void startSale(){
        this.currentSale = new Sale(itemRegistry);
    }

    // TODO ändra i UML
    // TODO varför inte bara skicka en ny item?
    // TODO Ska den inte först kontrollera om itemID finns i shopping cart?
//    public CurrentSaleDTO registerItem(int itemID){
    public SaleDTO registerItem(int itemID){
        return registerItem(itemID, 1);
    }

    public SaleDTO registerItem(int itemID, int quantity){
        ItemDTO itemInfo = itemRegistry.getItemInfo(itemID);
        currentSale.addItem(itemInfo, quantity);
        return currentSale.displayOpenSale(display);
    }

    public SaleDTO endSale(){
        currentSale.endSale();
        return currentSale.displayCheckout(display);
    }

    public void discountRequest (int customerID){
        DiscountDTO discountDTO = discountRegister.getDiscount(customerID);
        currentSale.applyDiscount(discountDTO);
    }

    /**
     * Handles payment.
     * Updates the balance of the cash register where
     * the payment was performed. Calculates change. Prints the receipt.
     * Loggs sale. Updates inventory and accounting system.
     * @param paidAmt The paid amount.
     */
    public void pay(Amount paidAmt){
        CashPayment payment = new CashPayment(paidAmt);
        currentSale.pay(payment);
        cashRegister.addPayment(payment);
        saleLog.logSale(currentSale);
        currentSale.updateInventory();
        accountingSystem.updateToAccountingSystem(currentSale);  // TODO till txt fil?
        currentSale.printReceipt(printer);
        currentSale = null;
    }
}