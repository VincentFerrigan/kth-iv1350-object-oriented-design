package se.kth.iv1350.model;

/**
 * Represents one specific payment for one sale.
 * The sale is paid in cash.
 */
public class CashPayment {
    private final Amount paidAmt;
    private Amount totalCostPaid;

    /**
     * Creates a new instance of the cash that is
     * paid for the entire sale.
     * @param paidAmt The amount of cash that was given by the customer
     */
    public CashPayment(Amount paidAmt){
        this.paidAmt = paidAmt;
    }

    /**
     * Calculates the total cost of the specified {@link Sale}
     * @param paidSale The sale that the customer is paying.
     */
    void calculateTotalCost(Sale paidSale) {
        totalCostPaid = paidSale.getTotalPrice();
        if (paidSale.getCustomer() != null) {paidSale.getCustomer().addBonusPoints(totalCostPaid);}
    }

    /**
     * @return The amount of cash that was given by the customer.
     */
    public Amount getPaidAmt() {
        return paidAmt;
    }

    /**
     * @return The total cost of the sale that was paid.
     */
    Amount getTotalCostPaid() {
        return totalCostPaid == null? new Amount(0) : new Amount(totalCostPaid);
    }

    /**
     * @return The amount of change the customer shall receive.
     */
    Amount getChange() {
        // TODO här är change negativt om kund betalat för lite. Just nu är det det vi tror är rätt. Vi vet först efter accountinssystem är gjord.
        // TODO Ett negativt belopp kan innebära att vi skickar ett "felmeddelande" om att kund betalat för lite.
        return paidAmt.minus(totalCostPaid);
    }
}