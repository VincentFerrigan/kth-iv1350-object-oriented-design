package se.kth.iv1350.integration;

import se.kth.iv1350.model.Sale;

public interface AccountingSystem {
    void updateRegistry(Sale closedSale);
}