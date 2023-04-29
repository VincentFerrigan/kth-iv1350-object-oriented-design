package src.se.kth.iv1350.model;

/**
 * Represents a cash register. There shall be one instance of this class for each register.
 */
public class CashRegister {
    Amount balance;            //TODO should be Amount?
    public static final double INITIAL_BALANCE = 10_000;

    /**
     * Creates a instance. Initial balance according to specified amount.
     * @param initialAmount
     */
    public CashRegister(Amount initialAmount) {
        this.balance = initialAmount;
    }

    /**
     * Creates a instance. Initial balance according to specified amount.
     * @param initialAmount
     */
    public CashRegister(double initialAmount) {
        this(new Amount(initialAmount));
    }

    /**
     * Creates a instance. Initial balance is the default value of 0;
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
    }
}
