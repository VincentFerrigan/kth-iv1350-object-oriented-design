package se.kth.iv1350.integration;

import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.model.Sale;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;


// Singleton facade.
// TODO JavaDocs
// Testa först med att köra factory. Frågan sen om du ska köra abstract to nedan gäller enbart flat file databases.
public class RegistryHandler { // Rename RegistryFacade?
    private static volatile RegistryHandler instance;
    private ItemRegister itemRegister;
//    private CustomerRegister customerRegister;
    private AccountingSystem accountingSystem;

//    private IRegistry itemRegister;
    private IRegistry customerRegister;
//    private IRegistry accountingSystem;
    /**
     * Creates an instance of {@link RegistryHandler}.
     */
    private RegistryHandler() throws IOException, OperationFailedException {
        try {
            RegistryFactory registryFactory = RegistryFactory.getInstance();
//            accountingSystem = registryFactory.getDefaultAccountingRegister();
            customerRegister = registryFactory.getDefaultCustomerRegister();
//            itemRegister = registryFactory.getDefaultItemRegister();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException ex) {
        throw new OperationFailedException("Unable to instantiate registries", ex);
    }
        this.accountingSystem = AccountingSystem.getInstance();
//        this.customerRegister = CustomerRegister.getInstance();
        this.itemRegister = ItemRegister.getInstance();
    }

    /**
     * @return The only instance of this singleton.
     * @throws IOException
     */
    public static RegistryHandler getInstance() throws IOException, OperationFailedException {
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
        updateCustomerRegister(closedSale);
        updateItemRegister(closedSale);
    }
    public void updateAccountingSystem(Sale closedSale) {
        accountingSystem.updateRegister(closedSale);
    }

    public void updateCustomerRegister(Sale closedSale) {
        customerRegister.updateRegister(closedSale);
    }
    public void updateItemRegister(Sale closedSale) {
        itemRegister.updateRegister(closedSale);
    }


    /**
     *
     * @param customerID
     * @return
     * @throws CustomerNotFoundInCustomerRegistryException
     * @throws CustomerRegistryException
     */
    public CustomerDTO getCustomerInfo(int customerID) throws Exception {
        return (CustomerDTO) customerRegister.getDataInfo(customerID);
    }

    /**
     *
     * @param itemID
     * @return
     * @throws ItemNotFoundInItemRegistryException
     * @throws ItemRegistryException
     */
    public ItemDTO getItemInfo(int itemID) throws ItemNotFoundInItemRegistryException {
        return itemRegister.getDataInfo(itemID);
    }

    /**
     *
     * @param timeOfSale
     * @return
     * @throws RecordNotFoundInRegisterException
     * @throws AccountingSystemException
     */
    public RecordDTO getRecordInfo(LocalDateTime timeOfSale) throws RecordNotFoundInRegisterException {
        return accountingSystem.getDataInfo(timeOfSale);

    }
}
