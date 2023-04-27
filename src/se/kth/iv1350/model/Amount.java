package src.se.kth.iv1350.model;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Represents an amount of money
 */
public class Amount {
    private final Currency currency;
    Locale locale;
    private final double amount;

    /**
     * Creates a new instance, representing the specified amount.
     * @param amount The amount represented by the newly created instance.
     */
    public Amount(double amount){
        this(new Locale("sv", "SE"), amount);
    }
    public Amount(Locale locale, double amount) {
        this.locale = locale;
        this.currency = Currency.getInstance(locale);
        this.amount = amount;
    }

    /**
     * Creates a new instance,
     * based on the specified {@link Amount}.
     * @param another The <code>Amount</code> to copy
     * @return A copy of the specified <code>Amount</code>
     */
    public Amount(Amount another) {
        this.locale = another.locale;
        this.currency = another.currency;
        this.amount = another.amount;
    }


    /**
     * Subtracts the specified {@link Amount} from
     * this object and returns an {@link Amount}
     * instance with the result.
     * The operation will overflow if the result
     * is smaller than <code>Integer.MIN_VALUE</code>.
     * @param other The <code>Amount</code> to subtract.
     * @return The result of the subtraction.
     */
    public Amount minus(Amount other) {
        return new Amount(amount - other.amount);
    }

    /**
     * Adds the specified {@link Amount} to
     * this object and returns an {@link Amount}
     * instance with the result.
     * The operation will overflow if the result
     * is larger than <code>Integer.MAX_VALUE</code>.
     * @param other The <code>Amount</code> to add.
     * @return The result of the addition.
     */
    public Amount plus(Amount other) {
        return new Amount(amount + other.amount);
    }

    // TODO testa då jag är osäker. Annars får man göra en statisk metod som summerar en lista istället.
    public Amount plus(List<Amount> amounts) {
        return new Amount(amounts.stream().reduce(this, Amount::plus));
    }

    /**
     * Multiplies this object with the specified
     * <code>double multiplier</code> and returns an
     * {@link Amount} instance with the result.
     * The operation will overflow if the result
     * is larger than <code>Integer.MAX_VALUE</code> or
     * is smaller than <code>Integer.MIN_VALUE</code>.
     * @param multiplier The factor that changes the amount.
     * @return The product of the multiplication.
     */
    public Amount multiply(double multiplier) {
        return new Amount(multiplier * amount);
    }

    @Override
    public String toString() {
        return String.format(locale, "%,.2f %s",this.amount, this.currency.getSymbol(locale));
    }
    // TODO behövs en override på equal?

    // TODO kommer dessa användas?
    public Currency getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }
}
