package se.kth.iv1350.controller;
import se.kth.iv1350.integration.DiscountDTO;
import se.kth.iv1350.model.*;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.util.Event;
import se.kth.iv1350.util.ErrorFileLogHandler;

import java.io.IOException;
import java.util.*;

/* TODO: To do and keep the following for the report.
* The current pay method makes the controller bloated and tend towards low cohesion.
* Solution: Move payment creation to sale. Sale has to anyway "keep" payment details.
* Open issue: What to do with the cash register.
*
* Should we "keep" the item registry? Why move it to sale?
*/

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
    private ErrorFileLogHandler logger;

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
        logger = ErrorFileLogHandler.getInstance();
        saleObservers = new HashMap<>();
        Arrays.stream(Event.values()).forEach(event -> saleObservers.put(event, new ArrayList<>()));
    }

    /**
     * The specified observer will be notified when this sale has been updated
     * i.e. an event has occurred.
     *
     * @param eventType The event as {@Link Event}
     * @param observer The observer to notify.
     */
    //TODO: Gäller denna alla observers oavsett syfte, Typ tänk om man vill ha observers som blir notifierade när sale ha been paid etc.
    public void addSaleObserver(Event eventType, SaleObserver observer) {
        saleObservers.get(eventType).add(observer);
    }
    /**
     * Start a new sale. This method must be called before doing anything else during a sale.
     */
    public void startSale(){
        this.currentSale = new Sale();
        currentSale.addSaleObservers(saleObservers);
    }

    /**
     * Registers an item for sale with its item identifier and adds quantity 1.
     * @param itemID ShoppingCartItem identifier.
     * @throws ItemNotFoundInItemRegistryException when item ID does not exist in inventory
     * @throws OperationFailedException when there is a fail with inventory system
     */
    public void registerItem(int itemID) throws OperationFailedException, ItemNotFoundInItemRegistryException {
        registerItem(itemID, 1);
    }

    /**
     * Registers an item for sale by entering its item identifier and quantity.
     * @param itemID The item identifier.
     * @param quantity The item quantity.
     * @throws ItemNotFoundInItemRegistryException when item ID does not exist in inventory
     * @throws OperationFailedException when there is a fail with inventory system
     * @throws IllegalStateException if this method is called before initiating a new sale
     */
    public void registerItem(int itemID, int quantity) throws ItemNotFoundInItemRegistryException, OperationFailedException {
        if (currentSale == null) {
            throw new IllegalStateException("Registering items before initiating a new sale");
        }
        try {
            currentSale.addItem(itemID, quantity);
        } catch (ItemNotFoundInShoppingCartException ex) {
            try {
                ItemDTO itemInfo = itemRegistry.getItemInfo(ex.getItemIDNotFound());
                currentSale.addItem(itemInfo, quantity);
            } catch (ItemRegistryException itmRegExc) {
                logger.log(itmRegExc);
                throw new OperationFailedException("No connection to inventory system. Try again.", itmRegExc);
            }
        }
    }

    /**
     * Checkout. Displays the checked out shopping cart.
     * @throws IllegalStateException if this method is called before initiating a new sale
     */
    public void endSale(){
        if (currentSale == null) {
            throw new IllegalStateException("Call to endSale before initiating a new sale");
        }
        currentSale.endSale();
    }

    /**
     * Fetches discount from the discount database and applies it to the sale.
     * @param customerID
     * @throws IllegalStateException if this method is called before calling newSale and registerItem.
     */
    //TODO: How to add customer to sale? How will the discount display?
    public void discountRequest (int customerID){
        if (currentSale == null || currentSale.isComplete() == false) {
            throw new IllegalStateException(
                    "Call to discountRequest before initiating a new sale and registering items.");
        }
//        DiscountDTO discountDTO = discountRegister.getDiscountInfo(customerID);
//        currentSale.applyDiscount(discountDTO);
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
        if (currentSale == null || currentSale.isComplete() == false) {
            throw new IllegalStateException(
                    "Call to pay before initiating a new sale and registering items.");
        }
        CashPayment payment = new CashPayment(paidAmt);
        currentSale.pay(payment);
        cashRegister.addPayment(payment);
        saleLog.logSale(currentSale);
        itemRegistry.updateInventory(currentSale);
        accountingSystem.updateToAccountingSystem(currentSale);
        currentSale.printReceipt(printer);
        currentSale = null;
    }
}