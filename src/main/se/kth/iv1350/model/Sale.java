package se.kth.iv1350.model;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

import se.kth.iv1350.integration.*;
import se.kth.iv1350.integration.pricing.DiscountFactory;
import se.kth.iv1350.integration.pricing.DiscountStrategy;
import se.kth.iv1350.util.Event;

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
    private Map<Event, List<SaleObserver>> saleObservers;
    private LocalDateTime timeOfSale; // TODO ska den vara kvar? Syfte?
    private Map<Integer, ShoppingCartItem> shoppingCart = new HashMap<>();
    private CashPayment payment;
    private DiscountStrategy pricing;
    private boolean isComplete;

//    private Amount discountAmount;

    /**
     * Creates a new instance, representing a sale made by a customer.
     */
    public Sale(){
        this.timeOfSale = LocalDateTime.now();
        saleObservers = new HashMap<>();
        isComplete = false;
        try {
            pricing = DiscountFactory.getInstance().getDiscountStrategy();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
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
            notifyObservers(Event.NEW_ITEM);
        } else {
            throw new ItemNotFoundInShoppingCartException(itemID);
        }
    }

    /**
     * Adds an item to the shopping cart.
     * @param itemInfo item information as {@link ItemDTO}
     */
    public void addItem(ItemDTO itemInfo, int quantity) {
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
        notifyObservers(Event.NEW_ITEM);
    }

    /**
     * Gets the total amount for the current sale.
     * @return The total amount of the current sale.
     */
    public Amount getTotalPrice() {
        // TODO: bör begränsas eller slås ihop med calculate. Alternativt endast hämtas från DTO
        return calculateRunningTotal();
    }

    /**
     * Calculates the total cost of the shopping cart, including possible discount.
     * @return The running total as a {@link Amount}.
     */
    private Amount calculateRunningTotal() {
        // Totalbelopp
        Amount runningTotal = new Amount(0);
        List<Amount> totalPrices = getCollectionOfItems()
                .stream()
                .map(ShoppingCartItem::getTotalSubPrice)
                .collect(toList());
        runningTotal = runningTotal.plus(totalPrices);

        return runningTotal;
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
        List<Amount> vatAmounts = getCollectionOfItems().stream().map(ShoppingCartItem::getVATCosts).collect(toList());
        totalVATAmount = totalVATAmount.plus(vatAmounts);

        return totalVATAmount;
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
        notifyObservers(Event.CHECKED_OUT);
    }

//    /**
//     * Applies discount to the shopping cart.
//     * @param discount The discount information as a {@link DiscountDTO}.
//     */
//    public void applyDiscount(DiscountDTO discount) {
//        // TODO: change strategy to strategy design pattern with compoisition.
//        this.discount = discount;
//    }

    /**
     * The sale is paid by the specified payment
     * @param payment The payment used to pay for
     * this sale, as a {@link CashPayment}.
     */
    public void pay(CashPayment payment) {
        payment.calculateTotalCost(this);
        this.payment = payment;
        notifyObservers(Event.PAID);
    }

    /**
     * Prints a receipt for the current sale on specified printer.
     * @param printer The printer where the receipt is printed.
     */
    public void printReceipt(Printer printer) {
        Receipt receipt = new Receipt(this);
        printer.printReceipt(receipt);
    }

    private void notifyObservers(Event eventType) {
        LimitedSaleView limitedSaleView = new LimitedSaleViewWrapper(this);
        saleObservers.get(eventType).forEach(observer -> observer.updateSale(limitedSaleView));
    }

    /**
     * All the specified observers will be notified when this sale has been updated.
     *
     * @param observers The observers to notify.
     */
    public void addSaleObservers(Map<Event, List<SaleObserver>> observers) {
        saleObservers.putAll(observers);
    }
}