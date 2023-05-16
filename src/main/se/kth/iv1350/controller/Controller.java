package se.kth.iv1350.controller;
import se.kth.iv1350.integration.DiscountDTO;
import se.kth.iv1350.model.*;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.util.Event;
import se.kth.iv1350.util.LogHandler;

import java.io.IOException;
import java.util.*;

import static se.kth.iv1350.util.Event.NEW_ITEM;

/**
 * This is the application's only controller class. All calls to the model pass
 * through here.
 */
public class Controller {
    private Map<Event, List<SaleObserver>> saleObservers;
    private Printer printer;
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
    public Controller (Printer printer, RegisterCreator registerCreator) throws IOException {
        this.printer = printer;
        saleLog = registerCreator.getSaleLog();
        itemRegistry = registerCreator.getInventorySystem();
        discountRegister = registerCreator.getDiscountRegister();
        accountingSystem = registerCreator.getAccountingSystem();
        cashRegister = new CashRegister(CashRegister.INITIAL_BALANCE);
        logger = new LogHandler();
        saleObservers = new HashMap<>();
        Arrays.stream(Event.values()).forEach(event -> saleObservers.put(event, new ArrayList<>()));
    }

    /**
     * The specified observer will be notified when this sale has been updated
     * i.e. an event has occurred.
     *
     * @param eventType The event as {@Link Event}
     * @param obs The observer to notify.
     */
    //TODO: Gäller denna alla observers oavsett syfte, Typ tänk om man vill ha observers som blir notifierade när sale ha been paid etc.
    public void addSaleObserver(Event eventType, SaleObserver obs) {
        saleObservers.get(eventType).add(obs);
    }
    /**
     * Start a new sale. This method must be called before doing anything else during a sale.
     */
    public void startSale(){
        this.currentSale = new Sale(itemRegistry);
        currentSale.addSaleObservers(saleObservers);
    }

    /**
     * Registers an item for sale with its item identifier and adds quantity 1.
     * @param itemID ShoppingCartItem identifier.
     * @return Sale information as a Data Transfer Object.
     * @throws ItemNotFoundException when item ID does not exist in inventory
     * @throws OperationFailedException when there is a fail with inventory system
     */
    public SaleDTO registerItem(int itemID) throws OperationFailedException, ItemNotFoundException {
        return registerItem(itemID, 1);
    }

    /**
     * Registers an item for sale by entering its item identifier and quantity.
     * @param itemID The item identifier.
     * @param quantity The item quantity.
     * @return SaleDTO Sale information as a Data Transfer Object.
     * @throws ItemNotFoundException when item ID does not exist in inventory
     * @throws OperationFailedException when there is a fail with inventory system
     * @throws IllegalStateException if this method is called before initiating a new sale
     */
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
        // Vill egentligen ta bort
        return currentSale.getSaleInfo();
    }

    /**
     * Checkout. Displays the checked out shopping cart.
     * @return SaleDTO Sale information as a Data Transfer Object
     * @throws IllegalStateException if this method is called before initiating a new sale
     */
    public SaleDTO endSale(){
        if (currentSale == null) {
            throw new IllegalStateException("Call to endSale before initiating a new sale");
        }
        return currentSale.endSale();
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