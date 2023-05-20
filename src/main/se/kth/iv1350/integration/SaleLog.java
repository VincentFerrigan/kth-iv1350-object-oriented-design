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

    /**
     * Returns a list containing all sales made by a customer with the
     * specified identification number.
     * If there are no such sales, the returned list is empty.
     *
     * @param customerID The customer identification number of
     * the customer whose sales shall be retrieved.
     * @return A list with all sales made by the specified customer.
     */
    public List<Sale> findSalesByCustomerID(int customerID) {
        List<Sale> salesWithSpecifiedCustomerID = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getCustomer() != null && sale.getCustomer().getCustomerID() == customerID) {
                salesWithSpecifiedCustomerID.add(sale);
            }
        }
        return salesWithSpecifiedCustomerID;
    }
}