package src.se.kth.iv1350.model;

import java.util.Locale;

/**
 *
 */
public class Amount {
    String currency;
    double amount;

    public Amount(double amount){
        this("SEK", amount);
    }
    public Amount(String currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Amount(Amount another) {
        this.currency = another.getCurrency();
        this.amount = another.getAmount();
    }

    public String getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public void addAmount(Amount another) {
        if (this.currency.equals(another.getCurrency())) {
            this.amount += another.getAmount();
        }
    }
    public void subtractAmount(Amount another) {
        if (this.currency.equals(another.getCurrency())) {
            this.amount -= another.getAmount();
        }
    }
    public void multiptyAmount(double factor) {
        this.amount *= factor;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%,.2f %s",this.amount, this.currency);
    }
}
