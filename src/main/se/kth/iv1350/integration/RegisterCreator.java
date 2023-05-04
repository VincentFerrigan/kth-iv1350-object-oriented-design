package se.kth.iv1350.integration;

import se.kth.iv1350.model.CashRegister;

/**
 * The class is responsible for instantiating all registers (external systems/databases)
 */
public class RegisterCreator {
    private SaleLog saleLog;
    private ItemRegistry itemRegistry;
    private DiscountRegister discountRegister;
    private AccountingSystem accountingSystem;
    private static final String FILE_PATH = "src/main/se/kth/iv1350/data/";
    private static final String AS_FLAT_FILE_DB = "accounting.csv";
    private static final String IS_FLAT_FILE_DB = "inventory_items.csv";
    private static final String DR_FLAT_FILE_DB = "discounts.csv";

    /**
     * Creates an instance of {@link RegisterCreator}.
     */
    public RegisterCreator() {
        this.saleLog = new SaleLog();
        this.itemRegistry = new ItemRegistry(FILE_PATH, IS_FLAT_FILE_DB);
        this.discountRegister = new DiscountRegister(FILE_PATH, DR_FLAT_FILE_DB);
        this.accountingSystem = new AccountingSystem(FILE_PATH, AS_FLAT_FILE_DB);
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
