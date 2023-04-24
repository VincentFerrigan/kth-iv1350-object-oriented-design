package src.se.kth.iv1350.model;

/**
 *
 */
public class Amount {
    String currency;
    int     amount;

    public Amount(int amount){
        this("SEK", amount);
    }
    public Amount(String currency, int amount) {
        this.currency = currency;
        this.amount = amount;
    }
}
