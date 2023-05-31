package se.kth.iv1350.integration;

import se.kth.iv1350.model.Sale;

import java.util.List;

public interface AccountingSystem {
    void updateRegistry(Sale closedSale);
}