package se.kth.iv1350.integration;

import se.kth.iv1350.integration.dto.CustomerDTO;
import se.kth.iv1350.integration.dto.ItemDTO;
import se.kth.iv1350.integration.dto.RecordDTO;
import se.kth.iv1350.model.Sale;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;


// Singleton facade.
// TODO JavaDocs
// Testa först med att köra factory. Frågan sen om du ska köra abstract to nedan gäller enbart flat file databases.
/**
 * A service container as a Facade implemented as Singleton.
// * It provides a simpler interface with functionality limited to necessity  for the client.
 * It provides a unified and simplified interface.
 * This service container is responsible for instantiating all registers (external systems/databases) and provide
 * a unified and simplified interface.
 */
public class RegistryHandler { // Rename RegistryFacade?
    private static volatile RegistryHandler instance;
    private AccountingSystem accountingSystem;
    private CustomerRegistry customerRegistry;
    private ItemRegistry itemRegistry;
    private SaleLog saleLog;

    /**
     * Creates an instance of {@link RegistryHandler}.
     *
     */
    private RegistryHandler() {
        try {
            IRegistryFactory  registryFactory = FlatFileDatabaseFactory.getInstance();
            accountingSystem = registryFactory.getDefaultAccountingSystem();
            customerRegistry = registryFactory.getDefaultCustomerRegister();
            itemRegistry = registryFactory.getDefaultItemRegister();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException ex) {
            //TODO osäker om try catch ska vara här
        throw new RegistryHandlerException("Unable to instantiate registries", ex);
        }
        saleLog = new SaleLog();
    }

    /**
     * @return The only instance of this singleton.
     * @throws IOException
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

    public void updateRegistries(Sale closedSale) {
        updateAccountingSystem(closedSale);
        updateCustomerRegistry(closedSale);
        updateItemRegistry(closedSale);
        logSale(closedSale);
    }

    public void logSale(Sale closedSale) {saleLog.logSale(closedSale);}
    public void updateAccountingSystem(Sale closedSale) {
        accountingSystem.updateRegistry(closedSale);
    }
    public void updateCustomerRegistry(Sale closedSale) {
        customerRegistry.updateRegistry(closedSale);
    }
    public void updateItemRegistry(Sale closedSale) {
        itemRegistry.updateRegistry(closedSale);
    }

    /**
     *
     * @param customerID
     * @return
     * @throws CustomerNotFoundInCustomerRegistryException
     * @throws CustomerRegistryException
     */
    public CustomerDTO getCustomerInfo(int customerID) throws CustomerNotFoundInCustomerRegistryException {
        return customerRegistry.getDataInfo(customerID);
    }

    /**
     *
     * @param itemID
     * @return
     * @throws ItemNotFoundInItemRegistryException
     * @throws ItemRegistryException
     */
    public ItemDTO getItemInfo(int itemID) throws ItemNotFoundInItemRegistryException {
        return itemRegistry.getDataInfo(itemID);
    }

    /**
     *
     * @param timeOfSale
     * @return
     * @throws AccountRecordNotFoundInAccountingSystemException
     * @throws AccountingSystemException
     */
    public RecordDTO getRecordInfo(LocalDateTime timeOfSale) throws AccountRecordNotFoundInAccountingSystemException {
        return accountingSystem.getDataInfo(timeOfSale);
    }
}
