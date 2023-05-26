package se.kth.iv1350.model;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.integration.pricing.DiscountFactory;
import se.kth.iv1350.integration.pricing.DiscountStrategy;
import static java.util.stream.Collectors.toList;

/* TODO: Keep the following for the report.
*  For lower coupling, move the item registry out of sale.
*  For additional high grade task 2. Adapt the Map (library) to a class that inherits from Map. It can become a ShoppingCart class.
*  With contains methods, adds methods and so on. Should it contain the ShoppingCartItem class or does Sale create them? This could increase cohesion.
*
* Is the event a state? Could it be used if payment be moved to sale instead of control.
 */

/**
 * Represents a particular sale.
 */
public class Sale {
    private List<SaleObserver> saleObservers;
    private LocalDateTime timeOfSale; // TODO ska den vara kvar? Syfte?
    private Map<Integer, ShoppingCartItem> shoppingCart = new HashMap<>();
    private CashPayment payment;
    private DiscountStrategy pricing;
    private Customer customer;
    private boolean isComplete;

    /**
     * Creates a new instance, representing a sale made by a customer.
     * @throws OperationFailedException when unable to set up pricing.
     */
    public Sale() throws OperationFailedException {
        this.timeOfSale = LocalDateTime.now();
        saleObservers = new ArrayList<>();
        isComplete = false;
        try {
            DiscountFactory discountFactory = DiscountFactory.getInstance();
            pricing = discountFactory.getDiscountStrategy();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | NoSuchMethodException | InvocationTargetException ex) {
            throw new OperationFailedException("Unable to instantiate pricing algorithms", ex);
        }
    }

    /**
     * Adds an item to the shopping cart.
     * @param itemID The item identifier.
     * @param quantity The item quantity.
     * @throws ItemNotFoundInShoppingCartException when item does not exist in Shopping Cart
     */
    public void addItem(int itemID, int quantity) throws ItemNotFoundInShoppingCartException {
        ShoppingCartItem existingShoppingCartItem = this.shoppingCart.get(itemID);
        if (existingShoppingCartItem != null) {
            existingShoppingCartItem.addToQuantity(quantity);
            isComplete = false;
            notifyObservers();
        } else {
            throw new ItemNotFoundInShoppingCartException(itemID);
        }
    }

    /**
     * Adds an item to the shopping cart.
     * @param itemInfo item information as {@link ItemDTO}
     * @throws OperationFailedException when unable to set up VAT for item.
     */
    public void addItem(ItemDTO itemInfo, int quantity) throws OperationFailedException {
        ShoppingCartItem newShoppingCartItem = new ShoppingCartItem(itemInfo, quantity);
        ShoppingCartItem alreadyExistedShoppingCartItem = shoppingCart.put(
                itemInfo.getItemID(),
                newShoppingCartItem);

        if (alreadyExistedShoppingCartItem != null) {
            this.shoppingCart
                .get(alreadyExistedShoppingCartItem.getItemID())
                .addToQuantity(alreadyExistedShoppingCartItem.getQuantity());
        }
        isComplete = false;
        notifyObservers();
    }

    /**
     * Gets the total amount for the current sale, including possible discount.
     * @return The total amount of the current sale.
     */
    public Amount calculateTotalPrice() {
        return pricing.getTotal(this);
    }

    /**
     * Calculates the total cost of the shopping cart
     * @return The running total as a {@link Amount}.
     */
    public Amount calculateRunningTotal() {
        // Totalbelopp
        Amount runningTotal = new Amount(0);
        List<Amount> totalPrices = getCollectionOfItems()
                .stream()
                .map(ShoppingCartItem::getTotalSubPrice)
                .collect(toList());
        return runningTotal.plus(totalPrices);
    }

    /**
     * Gets the total VAT amount for the current sale.
     * @return The total VAT amount of the current sale.
     */
    public Amount getTotalVATCosts() {
        // TODO: bör begränsas eller slås ihop med calculate. Alternativt endast hämtas från DTO
        return calculateTotalVATAmount();
    }

    /**
     * Calculates the total VAT of items in the shopping cart.
     * @return The total VAT amount as a {@link Amount}.
     */
    private Amount calculateTotalVATAmount() {
        // Momsberäkning
        Amount totalVATAmount = new Amount(0);
        List<Amount> vatAmounts = getCollectionOfItems().stream().map(ShoppingCartItem::calculateTotalSubVATCost).collect(toList());
        return totalVATAmount.plus(vatAmounts);
    }

    /**
     * Gets complete status, i.e. has the sale ended
     * @return complete status
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Gets payment for the current sale.
     * @return The payment of the current sale.
     */
    CashPayment getPayment(){
        return payment;
    }

    public Amount getTotalPricePaid() {
        if (payment == null){
            return new Amount(0);
        } else {
            return new Amount(payment.getTotalCostPaid());
        }
    }

    /**
     * Gets a collection of the items in the shopping cart.
     * @return A {@link Collection} of the items in the shopping cart.
     */
    public Collection<ShoppingCartItem> getCollectionOfItems() {
        return shoppingCart.values();
    }

    /**
     * Update sale information for checkout
     */
    public void endSale() {
        // what else apart from notification?
        isComplete = true;
        notifyObservers();
    }

    /**
     * Add customer to Sale. Discount applied discount or promotion exists.
     * @param customerInfo customer information as a {@link CustomerDTO}.
     */
    public void addCustomerToSale(CustomerDTO customerInfo) {
        this.customer = new Customer(customerInfo);
    }

    /**
     * Get customer details
     * @return customer details as {@link Customer}
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Gets the discount of the current sale, if any.
     * @return the total discount of the current sale.
     */
    public Amount getDiscount() {
        return pricing.getDiscount();
    }

    /**
     * Gets the discount information, if any.
     * @return the discount information as string. Returns empty discount if no discount was applied
     */
    public String createStringDiscountInfo() {
        return pricing.toString();
    }


    /**
     * Pay sale with specified payment.
     * If customer registered, bonus points will be added.
     * @param payment The payment used to pay for
     * this sale, as a {@link CashPayment}.
     */
    public void pay(CashPayment payment) {
        payment.calculateTotalCost(this);
        this.payment = payment;
    }

    /**
     * Prints a receipt for the current sale on specified printer.
     * @param printer The printer where the receipt is printed.
     */
    public void printReceipt(Printer printer) {
        Receipt receipt = new Receipt(this);
        printer.printReceipt(receipt);
    }

    /**
     * All the specified observers will be notified when this sale has been updated.
     *
     * @param observers The observers to notify.
     */
    public void addAllSaleObservers(List<SaleObserver> observers) {
        saleObservers.addAll(observers);
    }

    private void notifyObservers() {
        LimitedSaleView limitedSaleView = new LimitedSaleViewWrapper(this);
        saleObservers.forEach(observer -> observer.updateSale(limitedSaleView));
    }
}