package se.kth.iv1350.controller;

import se.kth.iv1350.integration.DiscountDTO;
import se.kth.iv1350.model.SaleDTO;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.model.CashPayment;
import se.kth.iv1350.model.CashRegister;
import se.kth.iv1350.model.Sale;
import se.kth.iv1350.model.Amount;

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

    /**
     * Registers an item for sale with its item identifier.
     * @param itemID Item identifier.
     * @return Sale information as a Data Transfer Object.
     */
    public SaleDTO registerItem(int itemID){
        return registerItem(itemID, 1);
    }

    /**
     * Registers an item for sale by entering its item identifier and quantity.
     * @param itemID The item identifier.
     * @param quantity The item quantity.
     * @return Sale information as a Data Transfer Object.
     */
    public SaleDTO registerItem(int itemID, int quantity){
        currentSale.addItem(itemID, quantity);
        return currentSale.displayOpenSale(display);
    }

    /**
     * Checkout. Displays the checked out shopping cart.
     * @return Sale information as a Data Transfer Object
     */
    public SaleDTO endSale(){
        return currentSale.displayCheckout(display);
    }

    /**
     * Fetches discount from the discount database and applies it to the sale.
     * @param customerID
     */
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
        accountingSystem.updateToAccountingSystem(currentSale);
        currentSale.printReceipt(printer);
        currentSale = null;
    }
}