package src.se.kth.iv1350.model;

public class CashPayment {
    private final Amount paidAmt; // TODO ändra till final
    private Amount totalCost;

    /**
     * The cash that is paid for the entire sale.
     * @param paidAmt The total amount given from costumer
     */
    public CashPayment(Amount paidAmt){
        this.paidAmt = paidAmt;
    }

    //TODO make method after Sale class is done
    void calculateTotalCost(Sale paidSale){

    }

    public void setTotalCost(Amount totalCost) {
        this.totalCost = new Amount(totalCost);
    }

    //TODO look at getters
    public Amount getPaidAmt() {
        return paidAmt;
    }

    Amount getTotalCost() {
        return totalCost;
    }

    // TODO här är change negativt. Just nu är det det vi tror är rätt. Vi vet först efter accountinssystem är gjord.
    public Amount getChange() {
        Amount change = new Amount(this.totalCost);
        change.subtractAmount(this.paidAmt);
        return change;
    }
}
