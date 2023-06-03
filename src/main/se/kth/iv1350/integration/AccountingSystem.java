package se.kth.iv1350.integration;

import se.kth.iv1350.model.Sale;

public interface AccountingSystem {

    /**
     * Updates the accounting system.
     * @param closedSale contains the sale details
     * @throws AccountingSystemException when database call failed.
     */
    void updateRegistry(Sale closedSale);
}