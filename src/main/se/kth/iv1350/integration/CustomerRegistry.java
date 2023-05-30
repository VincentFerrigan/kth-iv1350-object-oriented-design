package se.kth.iv1350.integration;

import se.kth.iv1350.integration.dto.CustomerDTO;
import se.kth.iv1350.model.Sale;

public interface CustomerRegistry<T> {
    CustomerDTO getDataInfo(T dataID) throws CustomerNotFoundInCustomerRegistryException;
    void updateRegistry(Sale closedSale);
}