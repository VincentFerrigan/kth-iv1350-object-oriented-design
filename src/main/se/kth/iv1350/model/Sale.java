package se.kth.iv1350.model;
import java.time.LocalDateTime;
import java.util.*;

import se.kth.iv1350.integration.*;
import se.kth.iv1350.util.Event;

import static java.util.stream.Collectors.toList;

/**
 * Represents a particular sale.
 */
public class Sale {
    private LocalDateTime timeOfSale;
    private Map<Integer, ShoppingCartItem> shoppingCart = new HashMap<>();
    private CashPayment payment;
    private DiscountDTO discount = new DiscountDTO();
    private ItemRegistry itemRegistry;
    private Amount totalAmount;
    private Amount totalVAT;
    private Amount discountAmount;
    private Map<Event, List<SaleObserver>> saleObservers = new HashMap<>();

    /**
     * Creates a new instance, representing a sale made by a customer.
     * @param itemRegistry The data stores information about all items (item catalog linked to external inventory system).
     */
    public Sale(ItemRegistry itemRegistry){
        this.timeOfSale = LocalDateTime.now();
        this.itemRegistry = itemRegistry;
    }

    /**
     * Adds an item to the shopping cart.
     * @param itemID The item identifier.
     * @param quantity The item quantity.
     * @throws ItemNotFoundException when item ID does not exist in inventory
     * @throws ItemRegistryException when there is an unknown fail with inventory system
     */
    public void addItem(int itemID, int quantity) throws ItemNotFoundException {
        if (shoppingCart.containsKey(itemID)) {
            this.shoppingCart.get(itemID).addToQuantity(quantity);
        }
        else {
            ItemDTO itemInfo = itemRegistry.getItemInfo(itemID);
            ShoppingCartItem shoppingCartItem = new ShoppingCartItem(itemInfo, quantity);
            shoppingCart.put(itemID, shoppingCartItem);
        }
        notifyObservers(Event.NEW_ITEM);
    }

    /**
     * Adds an item to the shopping cart.
     * @param itemID The item identifier.
     */
    public void addItem(int itemID) throws ItemNotFoundException {
        addItem(itemID, 1);
    }

    /**
     * Gets the total amount for the current sale.
     * @return The total amount of the current sale.
     */
    public Amount getTotalAmount() {
        return totalAmount;
    }

    /**
     * Gets the total VAT amount for the current sale.
     * @return The total VAT amount of the current sale.
     */
    public Amount getTotalVAT() {
        return totalVAT;
    }

    /**
     * Gets the discount amount for the current sale.
     * @return The discount amount of the current sale.
     */
    public Amount getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Calculates the total cost of the shopping cart, including possible discount.
     * @return The running total as a {@link Amount}.
     */
    Amount calculateRunningTotal() {
        // Totalbelopp
        Amount runningTotal = new Amount(0);
        List<Amount> totalPrices = getCollectionOfItems()
                .stream()
                .map(ShoppingCartItem::getTotalPrice)
                .collect(toList());
        runningTotal = runningTotal.plus(totalPrices);
//        runningTotal = runningTotal.multiply(discount.getDiscountMultiplier());

        return runningTotal;
    }

    /**
     * Calculates the total VAT of items in the shopping cart.
     * @return The total VAT amount as a {@link Amount}.
     */
    Amount calculateTotalVATAmount() {
        // Momsberäkning
        Amount totalVATAmount = new Amount(0);
        List<Amount> vatAmounts = getCollectionOfItems().stream().map(ShoppingCartItem::getVatAmount).collect(toList());
        totalVATAmount = totalVATAmount.plus(vatAmounts);
//        totalVATAmount = totalVATAmount.multiply(discount.getDiscountMultiplier());

        return totalVATAmount;
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
    Collection<ShoppingCartItem> getCollectionOfItems() {
        return shoppingCart.values();
    }

    /**
     * Applies discount to the shopping cart.
     * @param discount The discount information as a {@link DiscountDTO}.
     */
    public void applyDiscount(DiscountDTO discount){
        this.discount = discount;
    }

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

    /**
     * Updates the external inventory system with a {@link Collection}
     * of the sold items.
     */
    public void updateInventory() {
        itemRegistry.updateInventory(getCollectionOfItems());
    }

    /**
     * Update sale information for checkout
     * @return Sale information as a {@link SaleDTO}.
     */
    public SaleDTO endSale() {
        Amount runningTotal = calculateRunningTotal();
        Amount runningVAT = calculateTotalVATAmount();
        if (discount.getDiscountRate() > 0) {
            this.discountAmount = runningTotal.multiply(discount.getDiscountRate());
            this.totalAmount = runningTotal.multiply(this.discount.getDiscountMultiplier());
            this.totalVAT = runningVAT.multiply(this.discount.getDiscountMultiplier());
        } else {
            this.totalAmount = new Amount(runningTotal);
            this.totalVAT = new Amount(runningVAT);
        }
        notifyObservers(Event.CHECKED_OUT);
        return getSaleInfo();
    }

    /**
     * Get information about the sale.
     * @return Information about the sale as a {@link SaleDTO}
     */
    public SaleDTO getSaleInfo() {
        List<ShoppingCartItemDTO> shoppingCart = getShoppingCartInfo();

        Amount runningTotal = calculateRunningTotal();
        Amount runningVAT = calculateTotalVATAmount();
        if (discount.getDiscountRate() > 0) {
            discountAmount = runningTotal.multiply(discount.getDiscountRate());
            totalAmount = runningTotal.multiply(this.discount.getDiscountMultiplier());
            totalVAT = runningVAT.multiply(this.discount.getDiscountMultiplier());
        } else {
            totalAmount = new Amount(runningTotal);
            totalVAT = new Amount(runningVAT);
            discountAmount = new Amount(0);
        }
        return new SaleDTO(
                shoppingCart,
                totalAmount,
                totalVAT,
                discountAmount);
    }

    private List<ShoppingCartItemDTO> getShoppingCartInfo() {
        List<ShoppingCartItemDTO> shoppingCartInfo = new ArrayList<>();
        List<ShoppingCartItem> listOfShoppingCartItems = new ArrayList<>(getCollectionOfItems());

        for (ShoppingCartItem shoppingCartItem : listOfShoppingCartItems) {
            shoppingCartInfo.add(shoppingCartItem.getShoppingCartItemInfo());
        }
        return shoppingCartInfo;
    }


    private void notifyObservers(Event eventType) {
        SaleDTO saleInfo = getSaleInfo();
        saleObservers.get(eventType).forEach(observer -> observer.updateSale(saleInfo));
    }
    /**
     * The specified observer will be notified when this sale has been updated
     * i.e. an event has occurred.
     *
     * @param eventType The event as {@Link Event}
     * @param obs The observer to notify.
     */
    public void addSaleObserver(Event eventType, SaleObserver obs) {
        saleObservers.get(eventType).add(obs);
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