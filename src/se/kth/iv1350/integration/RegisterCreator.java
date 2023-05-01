package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.model.CashRegister;

/**
     * The class is responsible for instantiating all registers (external systems/databases)
 */
public class RegisterCreator {
    private SaleLog saleLog;
    private ItemRegistry itemRegistry;
    private CashRegister cashRegister;
    private DiscountRegister discountRegister;
    private AccountingSystem accountingSystem;
    private static final String FILE_PATH = "src/se/kth/iv1350/integration/";
    private static final String AS_FLAT_FILE_DB = "accounting.csv";
    private static final String IS_FLAT_FILE_DB = "inventory_items.csv";
    private static final String DR_FLAT_FILE_DB = "discounts.csv";

    public RegisterCreator() {
        this.saleLog = new SaleLog();
        this.itemRegistry = new ItemRegistry(FILE_PATH, IS_FLAT_FILE_DB);
        this.discountRegister = new DiscountRegister(FILE_PATH, DR_FLAT_FILE_DB);
        this.accountingSystem = new AccountingSystem(FILE_PATH, AS_FLAT_FILE_DB);
    }

    public SaleLog getSaleLog() {
        return saleLog;
    }

    public ItemRegistry getInventorySystem() {
        return itemRegistry;
    }

    public CashRegister getCashRegister() {
        return cashRegister;
    }

    public DiscountRegister getDiscountRegister() {
        return discountRegister;
    }

    public AccountingSystem getAccountingSystem() {
        return accountingSystem;
    }
}
