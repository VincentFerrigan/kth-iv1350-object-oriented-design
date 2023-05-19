package se.kth.iv1350.integration;


import java.io.IOException;

// TODO: Move file path and names to the registers. Make all registers into singletons. Should the template method design also be used? Interface Database -> FlatFileDatabase -> Singleton AccountingSystem/DiscountRegistry/ItemRegistry?
// TODO: What to do about SaleLog? Does it have anything todo with TotalRevenueFileOutput? Does TotalRevenueFileOutput have anything to do with AccountSystem. Whats the difference between all three of them. Make it clear.
/**
 * The class is responsible for instantiating all registers (external systems/databases)
 */
public class RegisterCreator {
    private SaleLog saleLog;
    private ItemRegistry itemRegistry;
    private DiscountRegister discountRegister;
    private AccountingSystem accountingSystem;

    /**
     * Creates an instance of {@link RegisterCreator}.
     */
    public RegisterCreator() throws IOException {
        this.saleLog = new SaleLog();
        this.itemRegistry = ItemRegistry.getInstance();
        this.discountRegister = DiscountRegister.getInstance();
        this.accountingSystem = AccountingSystem.getInstance();
    }

    /**
     * Get the saleLog as {@link SaleLog}
     * @return the saleLog
     */
    public SaleLog getSaleLog() {
        return saleLog;
    }

    /**
     * Get the item registry as {@link ItemRegistry}
     * @return the itemRegistry
     */
    public ItemRegistry getInventorySystem() {
        return itemRegistry;
    }

    /**
     * Get the cash register as {@link DiscountRegister}
     * @return the discount register
     */
    public DiscountRegister getDiscountRegister() {
        return discountRegister;
    }

    /**
     * Get the accounting system as {@link AccountingSystem}
     * @return the accounting system
     */
    public AccountingSystem getAccountingSystem() {
        return accountingSystem;
    }
}
