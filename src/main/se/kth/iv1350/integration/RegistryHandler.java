package se.kth.iv1350.integration;

import se.kth.iv1350.integration.dto.CustomerDTO;
import se.kth.iv1350.integration.dto.ItemDTO;
import se.kth.iv1350.model.Sale;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * A Singleton that creates an instance representing a service container (implemented as a Facade).
 * This service container is responsible for instantiating all registers (external systems/databases) and provides
 * a unified and simplified interface for the client classes.
 */
public class RegistryHandler { // Rename RegistryFacade?
    private static volatile RegistryHandler instance;
    private AccountingSystem accountingSystem;
    private CustomerRegistry customerRegistry;
    private ItemRegistry itemRegistry;
    private SaleLog saleLog;

    /**
     * Creates an instance of {@link RegistryHandler}.
     * @throws RegistryHandlerException
     */
    private RegistryHandler() {
        try {
            IRegistryFactory  registryFactory = FlatFileDatabaseFactory.getInstance();
            accountingSystem = registryFactory.getDefaultAccountingSystem();
            customerRegistry = registryFactory.getDefaultCustomerRegister();
            itemRegistry = registryFactory.getDefaultItemRegister();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException ex) {
        throw new RegistryHandlerException("Unable to instantiate registries", ex);
        }
        saleLog = new SaleLog();
    }

    /**
     * @return The only instance of this singleton.
     * @throws RegistryHandlerException when database call failed.
     */
    public static RegistryHandler getInstance() throws RegistryHandlerException {
        RegistryHandler result = instance;
        if (result == null) {
            synchronized (RegistryHandler.class) {
                result = instance;
                if (result == null) {
                    instance = result = new RegistryHandler();
                }
            }
        }
        return result;
    }

    /**
     * @throws AccountingSystemException
     * @throws CustomerRegistryException
     * @throws ItemRegistryException
     * @param closedSale
     */
    public void updateRegistries(Sale closedSale) {
        updateAccountingSystem(closedSale);
        updateCustomerRegistry(closedSale);
        updateItemRegistry(closedSale);
        logSale(closedSale);
    }

    /**
     * Updates the accounting system.
     * @param closedSale contains the sale details
     * @throws AccountingSystemException when database call failed.
     */
    public void updateAccountingSystem(Sale closedSale) {
        accountingSystem.updateRegistry(closedSale);
    }

    /**
     * Updates the customer database.
     * @param closedSale contains the sale details
     * @throws CustomerRegistryException
     */
    public void updateCustomerRegistry(Sale closedSale) {
        customerRegistry.updateRegistry(closedSale);
    }

    /**
     * Updates the inventory system.
     * @param closedSale contains the sale details
     * @throws ItemRegistryException when database call failed.
     */
    public void updateItemRegistry(Sale closedSale) {
        itemRegistry.updateRegistry(closedSale);
    }
    /**
     * Saves the specified sale permanently
     * @param closedSale The sale that will be saved.
     */
    public void logSale(Sale closedSale) {saleLog.logSale(closedSale);}


    /**
     * Searches for customer in the customer database with specified ID.
     * @param customerID The customer identification
     * @return Customer information as a {@link CustomerDTO}.
     * @throws CustomerNotFoundInCustomerRegistryException when customer ID does not exist in customer registry.
     * @throws CustomerRegistryException when database call failed.
     */
    public CustomerDTO getCustomerInfo(int customerID) throws CustomerNotFoundInCustomerRegistryException {
        return customerRegistry.getDataInfo(customerID);
    }

    /**
     * Searches for item in the inventory system with specified ID.
     * @param  itemID The items unique article number a.k.a item identifier.
     * @return Item information as a {@link ItemDTO}.
     * @throws ItemNotFoundInItemRegistryException when item ID does not exist in inventory.
     * @throws ItemRegistryException when database call failed.
     */
    public ItemDTO getItemInfo(int itemID) throws ItemNotFoundInItemRegistryException {
        return itemRegistry.getDataInfo(itemID);
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
        return saleLog.findSalesByCustomerID(customerID);
    }
}
