package se.kth.iv1350.controller;
import se.kth.iv1350.integration.dto.CustomerDTO;
import se.kth.iv1350.model.*;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.util.ErrorFileLogHandler;

import java.io.IOException;
import java.util.*;

/* TODO: To do and keep the following for the report.
* The current pay method makes the controller bloated and tend towards low cohesion.
* Solution: Move payment creation to sale. Sale has to anyway "keep" payment details.
* Open issue: What to do with the cash register?
*
*/

/**
 * This is the application's only controller class. All calls to the model pass
 * through here.
 */
public class Controller {
    private List<SaleObserver> saleObservers;
    private Printer printer;

    private RegistryHandler registryHandler;
    private SaleLog saleLog;
    private ItemRegister itemRegister;
    private CustomerRegister customerRegister;
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
//    public Controller (Printer printer, RegistryHandler registryHandler) throws IOException {
        this.printer = printer;
        this.registryHandler = registryHandler;
        saleLog = registerCreator.getSaleLog();
        accountingSystem = registerCreator.getAccountingSystem();
        customerRegister = registerCreator.getCustomerRegistry();
        itemRegister = registerCreator.getInventorySystem();
        cashRegister = new CashRegister(CashRegister.INITIAL_BALANCE);
        logger = ErrorFileLogHandler.getInstance();
        saleObservers = new ArrayList<>();
    }

    /**
     * The specified observer will be notified when this sale has been updated.
     * @param observer The observer to notify.
     */
    public void addSaleObserver(SaleObserver observer) {
        saleObservers.add(observer);
    }

    /**
     * The specified observer will be notified when the revenue has been changed.
     * @param observer The observer to notify.
     */
    public void addCashRegisterObserver(CashRegisterObserver observer) {
        cashRegister.addCashRegisterObserver(observer);
    }

    /**
     * Start a new sale. This method must be called before doing anything else during a sale.
     */
    public void startSale() throws OperationFailedException {
        try {
            this.currentSale = new Sale(itemRegister);
        } catch (OperationFailedException ex) {
            logger.log(ex);
            throw new OperationFailedException("Unable to start sale", ex);
        }
        currentSale.addAllSaleObservers(saleObservers);
    }

    /**
     * Registers an item for sale with its item identifier and adds quantity 1.
     * @param itemID ShoppingCartItem identifier.
     * @throws ItemNotFoundInItemRegistryException when item ID does not exist in inventory
     * @throws OperationFailedException when there is a fail with inventory system
     * @throws IllegalStateException if this method is called before initiating a new sale
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
        } catch (ItemRegistryException itmRegExc) {
            logger.log(itmRegExc);
            throw new OperationFailedException("No connection to inventory system. Try again.", itmRegExc);
        } catch (OperationFailedException vatRegExc) {
            logger.log(vatRegExc);
            throw new OperationFailedException("Unable to set up VAT, unable to register items", vatRegExc);

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
     * Add customer to Sale. Discount applied if discount or promotion exists
     * for customer with specified customer id.
     * @param customerID the customer identifier
     * @throws CustomerNotFoundInCustomerRegistryException when customer ID does not exist in registry
     * @throws OperationFailedException when there is a fail with customer database
     * @throws IllegalStateException if this method is called before calling newSale and registerItem.
     */
    public void registerCustomerToSale(int customerID)
            throws CustomerNotFoundInCustomerRegistryException, OperationFailedException {
        if (currentSale == null || currentSale.isComplete() == false) {
            throw new IllegalStateException(
                    "Call to registerCustomerToSale before initiating a new sale and registering items.");
        }
        try {
            CustomerDTO customerInfo = customerRegister.getDataInfo(customerID);
//            CustomerDTO customerInfo = registryHandler.getCustomerInfo(customerID);
            currentSale.addCustomerToSale(customerInfo);
        } catch (CustomerRegistryException custRegExc) {
            logger.log(custRegExc);
            throw new OperationFailedException("No connection customer registry. Try again.", custRegExc);
        }
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
        accountingSystem.updateRegister(currentSale);
        customerRegister.updateRegister(currentSale);
        itemRegister.updateRegister(currentSale);
        currentSale.printReceipt(printer);
        currentSale = null; // Är du säker?
    }
}