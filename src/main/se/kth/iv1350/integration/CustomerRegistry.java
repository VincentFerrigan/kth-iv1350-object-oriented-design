package se.kth.iv1350.integration;

import se.kth.iv1350.integration.dto.CustomerDTO;
import se.kth.iv1350.model.Sale;

public interface CustomerRegistry<T> {

    /**
     * Searches for customer in the customer database with specified ID.
     * @param dataID The customer identification
     * @return Customer information as a {@link CustomerDTO}.
     * @throws CustomerNotFoundInCustomerRegistryException when customer ID does not exist in customer registry.
     * @throws CustomerRegistryException when database call failed.
     */
    CustomerDTO getDataInfo(T dataID) throws CustomerNotFoundInCustomerRegistryException, CustomerRegistryException;
    /**
     * Updates the customer database.
     * @param closedSale contains the sale details
     * @throws CustomerRegistryException
     */
    void updateRegistry(Sale closedSale) throws CustomerRegistryException;
}