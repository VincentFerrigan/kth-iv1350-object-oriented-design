package se.kth.iv1350.integration;

/**
 * Thrown when customer does not exist in customer registry.
 */
public class CustomerNotFoundInCustomerRegistryException extends Exception{
    private int customerIDNotFound;

    /**
     * Creates a new instance including a message and the ID
     * that could not be found.
     * @param customerID the customer identification number
     */
    public CustomerNotFoundInCustomerRegistryException(int customerID) {
        super("Unable to find customer with ID \"%d\" in the customer registry.".formatted(customerID));
        this.customerIDNotFound = customerID;
    }

    public int getCustomerIDNotFound() {
        return customerIDNotFound;
    }
}