package se.kth.iv1350.integration;


import java.io.IOException;

// TODO: Move file path and names to the registers. Make all registers into singletons. Should the template method design also be used? Interface Database -> FlatFileDatabase -> Singleton AccountingSystemFlatFileDB/DiscountRegistry/ItemRegistry?
// TODO: What to do about SaleLog? Does it have anything todo with TotalRevenueFileOutput? Does TotalRevenueFileOutput have anything to do with AccountSystem. Whats the difference between all three of them. Make it clear.
/**
 * The class is responsible for instantiating all registers (external systems/databases)
 */
public class RegisterCreator {
    private SaleLog saleLog;
    private ItemRegistry itemRegistry;
    private CustomerRegistry customerRegistry;
    private AccountingSystem accountingSystem;

    /**
     * Creates an instance of {@link RegisterCreator}.
     */
    public RegisterCreator() throws IOException {
        this.saleLog = new SaleLog();
        this.accountingSystem = AccountingSystemFlatFileDB.getInstance();
        this.customerRegistry = CustomerRegistryFlatFileDB.getInstance();
        this.itemRegistry = ItemRegistryFlatFileDB.getInstance();
    }

    /**
     * Get the saleLog as {@link SaleLog}
     * @return the saleLog
     */
    public SaleLog getSaleLog() {
        return saleLog;
    }

    /**
     * Get the item registry as {@link ItemRegistryFlatFileDB}
     * @return the itemRegistry
     */
    public ItemRegistry getInventorySystem() {
        return itemRegistry;
    }

    /**
     * Get the cash register as {@link CustomerRegistryFlatFileDB}
     * @return the discount register
     */
    public CustomerRegistry getCustomerRegistry() {
        return customerRegistry;
    }

    /**
     * Get the accounting system as {@link AccountingSystemFlatFileDB}
     * @return the accounting system
     */
    public AccountingSystem getAccountingSystem() {
        return accountingSystem;
    }
}
