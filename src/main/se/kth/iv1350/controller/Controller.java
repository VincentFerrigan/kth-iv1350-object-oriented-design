package se.kth.iv1350.controller;
import se.kth.iv1350.integration.DiscountDTO;
import se.kth.iv1350.model.SaleDTO;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.model.CashPayment;
import se.kth.iv1350.model.CashRegister;
import se.kth.iv1350.model.Sale;
import se.kth.iv1350.model.Amount;
import se.kth.iv1350.util.LogHandler;

import java.io.IOException;

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
    private LogHandler logger;

    /**
     * Creates a new instance.
     * @param printer Interface to printer (prints receipts and display)
     * @param registerCreator Used to get all classes that handle database calls.
     */
    public Controller (Printer printer, Display display, RegisterCreator registerCreator) throws IOException {
        this.printer = printer;
        this.display = display;
        this.saleLog = registerCreator.getSaleLog();
        this.itemRegistry = registerCreator.getInventorySystem();
        this.discountRegister = registerCreator.getDiscountRegister();
        this.accountingSystem = registerCreator.getAccountingSystem();
        this.cashRegister = new CashRegister(CashRegister.INITIAL_BALANCE);
        this.logger = new LogHandler();
    }

    /**
     * Start a new sale. This method must be called before doing anything else during a sale.
     */
    public void startSale(){
        this.currentSale = new Sale(itemRegistry);
    }

    /**
     * Registers an item for sale with its item identifier and adds quantity 1.
     * @param itemID Item identifier.
     * @return Sale information as a Data Transfer Object.
     * @throws ItemNotFoundException when item ID does not exist in inventory
     * @throws OperationFailedException when there is a fail with inventory system
     */
    //TODO ILLEGAL SMEAGAL
    public SaleDTO registerItem(int itemID) throws OperationFailedException, ItemNotFoundException {
        return registerItem(itemID, 1);
    }

    /**
     * Registers an item for sale by entering its item identifier and quantity.
     * @param itemID The item identifier.
     * @param quantity The item quantity.
     * @return Sale information as a Data Transfer Object.
     * @throws ItemNotFoundException when item ID does not exist in inventory
     * @throws OperationFailedException when there is a fail with inventory system
     * @throws IllegalStateException if this method is called before initiating a new sale
     */
    //TODO ILLEGAL SMEAGAL
    public SaleDTO registerItem(int itemID, int quantity) throws ItemNotFoundException, OperationFailedException {
        if (currentSale == null) {
            throw new IllegalStateException("Registering items before initiating a new sale");
        }
        try {
            currentSale.addItem(itemID, quantity);
        } catch (ItemRegistryException itmRegExc) {
            logger.logException(itmRegExc);
            throw new OperationFailedException("No connection to inventory system. Try again.", itmRegExc);
        }
        return currentSale.displayOpenSale(display);
    }

    /**
     * Checkout. Displays the checked out shopping cart.
     * @return Sale information as a Data Transfer Object
     * @throws IllegalStateException if this method is called before initiating a new sale
     */
    public SaleDTO endSale(){
        if (currentSale == null) {
            throw new IllegalStateException("Call to endSale before initiating a new sale");
        }
        currentSale.endSale();
        return currentSale.displayCheckout(display);
    }

    /**
     * Fetches discount from the discount database and applies it to the sale.
     * @param customerID
     * @throws IllegalStateException if this method is called before calling newSale and registerItem.
     */
    public void discountRequest (int customerID){
        if (currentSale == null || currentSale.getTotalAmount() == null) {
            throw new IllegalStateException(
                    "Call to discountRequest before initiating a new sale and registering items.");
        }
        DiscountDTO discountDTO = discountRegister.getDiscount(customerID);
        currentSale.applyDiscount(discountDTO);
    }

    /**
     * Handles payment.
     * Updates the balance of the cash register where
     * the payment was performed. Calculates change. Prints the receipt.
     * Loggs sale. Updates inventory and accounting system.
     * @param paidAmt The paid amount.
     * @throws IllegalStateException if this method is called before calling newSale and registerItem.
     */
    public void pay(Amount paidAmt) {
        if (currentSale == null || currentSale.getTotalAmount() == null) {
            throw new IllegalStateException(
                    "Call to pay before initiating a new sale and registering items.");
        }
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