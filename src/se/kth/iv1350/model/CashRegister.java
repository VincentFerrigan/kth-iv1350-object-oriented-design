package src.se.kth.iv1350.model;

public class CashRegister {
    Amount balance;            //TODO should be Amount?

    public CashRegister(Amount initialAmount) {
        this.balance = initialAmount;
    }

    public CashRegister(double initialAmount) {
        this(new Amount(initialAmount));
    }

    public CashRegister() {
        this(10000);
    }

    public void addPayment(CashPayment payment){
        balance = balance.plus(payment.getPaidAmt());
        balance = balance.minus(payment.getChange());
    }
}
