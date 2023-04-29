package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.model.CashRegister;

/**
     * The class is responsible for instantiating all registers (external systems/databases)
 */
public class RegisterCreator {
    private SaleLog saleLog;
    private InventorySystem inventorySystem;
    private CashRegister cashRegister;
    private DiscountRegister discountRegister;
    private AccountingSystem accountingSystem;

    public RegisterCreator() {
        this.saleLog = new SaleLog();
        this.cashRegister = new CashRegister(CashRegister.INITIAL_BALANCE);
        this.inventorySystem = new InventorySystem(InventorySystem.FLAT_FILE_DB);
        this.discountRegister = new DiscountRegister(DiscountRegister.FLAT_FILE_DB);
        this.accountingSystem = new AccountingSystem(AccountingSystem.FLAT_FILE_DB);
    }

    public SaleLog getSaleLog() {
        return saleLog;
    }

    public InventorySystem getInventorySystem() {
        return inventorySystem;
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
