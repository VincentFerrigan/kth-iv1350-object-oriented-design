package src.se.kth.iv1350.model;

public class CashPayment {
    private Amount paidAmt;
    private Amount totalCost;
    private Amount change;          //TODO this attr is not in UML

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

    //TODO look at getters
    public Amount getPaidAmt() {
        return paidAmt;
    }

    Amount getTotalCost() {
        return totalCost;
    }

    Amount getChange() {
        return change;
    }
}
