package se.kth.iv1350.integration;

import se.kth.iv1350.model.Sale;

import java.io.IOException;
import java.time.LocalDateTime;

// TODO JavaDocs
public class RegistryHandler {
    private static volatile RegistryHandler instance;
    private SaleLog saleLog; // Should this be here?
    private ItemRegister itemRegister;
    private CustomerRegister customerRegister;
    private AccountingSystem accountingSystem;

    /**
     * Creates an instance of {@link RegisterCreator}.
     */
    private RegistryHandler() throws IOException {
        this.saleLog = new SaleLog();
        this.accountingSystem = AccountingSystem.getInstance();
        this.customerRegister = CustomerRegister.getInstance();
        this.itemRegister = ItemRegister.getInstance();
    }

    /**
     * @return The only instance of this singleton.
     * @throws IOException
     */
    public static RegistryHandler getInstance() throws IOException {
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
    public CustomerDTO getCustomerInfo(int customerID) throws CustomerNotFoundInCustomerRegistryException {
        return customerRegister.getDataInfo(customerID);
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
