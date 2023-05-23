package se.kth.iv1350.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cash register. There shall be one instance of this class for each register.
 */
public class CashRegister {
    public static final double INITIAL_BALANCE = 10_000;
    private List<CashRegisterObserver> cashRegisterObservers;
    private Amount balance;            //TODO should be Amount?
    private Amount revenue;

    /**
     * Creates an instance. Initial balance according to specified amount.
     * @param initialAmount
     */
    public CashRegister(Amount initialAmount) {
        cashRegisterObservers = new ArrayList<>();
        this.balance = initialAmount;
        this.revenue = new Amount(0);
    }

    /**
     * Creates a instance. Initial balance according to specified amount.
     * @param initialAmount
     */
    public CashRegister(double initialAmount) {
        this(new Amount(initialAmount));
    }

    /**
     * Creates an instance. Initial balance is the default value of 0;
     */
    public CashRegister() {
        this(0);
    }

    /**
     * Updates the balance of the cash register.
     * @param payment The payment handed over from the customer.
     */
    public void addPayment(CashPayment payment){
        balance = balance.plus(payment.getPaidAmt());
        balance = balance.minus(payment.getChange());
        revenue = revenue.plus(payment.getTotalCostPaid());
        notifyObservers();
    }
    private void notifyObservers() {
        cashRegisterObservers.forEach(observer -> observer.updateRevenue(revenue));
    }

    /**
     * The specified observer will be notified when this revenue has changed
     * @param observer The observer to notify.
     */
    public void addCashRegisterObserver(CashRegisterObserver observer) {
        cashRegisterObservers.add(observer);
    }
    /**
     * All the specified observers will be notified when the revenue has been updated
     * @param observers The observers to notify.
     */
    public void addAllCashRegisterObservers(List<CashRegisterObserver> observers) {
        cashRegisterObservers.addAll(observers);
    }
}
