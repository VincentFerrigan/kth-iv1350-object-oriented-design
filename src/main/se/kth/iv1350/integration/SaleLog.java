package se.kth.iv1350.integration;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all sales performed in the POS.
 */
public class SaleLog {
    private List<Sale> sales = new ArrayList<>();
    SaleLog(){}

    /**
     * Saves the specified sale permanently
     * @param closedSale The sale that will be saved.
     */
    public void logSale(Sale closedSale) {
        sales.add(closedSale);
    }

    public List<Sale> findSalesByTotalAmount(Amount totalAmount) {
        List<Sale> salesWithSpecifiedTotalAmount = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getRunningTotal().equals(totalAmount)) {
                salesWithSpecifiedTotalAmount.add(sale);
            }
        }
        return salesWithSpecifiedTotalAmount;
    }
}
