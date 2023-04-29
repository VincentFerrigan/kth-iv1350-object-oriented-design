package src.se.kth.iv1350.model;

public class CashRegister {
    Amount balance;            //TODO should be Amount?
    public static final double INITIAL_BALANCE = 10_000;

    public CashRegister(Amount initialAmount) {
        this.balance = initialAmount;
    }

    public CashRegister(double initialAmount) {
        this(new Amount(initialAmount));
    }

    public CashRegister() {
        this(0);
    }

    public void addPayment(CashPayment payment){
        balance = balance.plus(payment.getPaidAmt());
        balance = balance.minus(payment.getChange());
    }
}
