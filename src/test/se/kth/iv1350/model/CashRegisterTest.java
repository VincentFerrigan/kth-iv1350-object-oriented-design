package se.kth.iv1350.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.POSTestSuperClass;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.ItemNotFoundInItemRegistryException;
import se.kth.iv1350.integration.ItemRegistryException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CashRegisterTest extends POSTestSuperClass {

    private CashRegister instance;
    private final Amount INITIAL_BALANCE = new Amount(1000);
    private CashPayment payment;
    private final Amount PAID_AMOUNT = new Amount(500);
    private Sale sale;
    private Amount itemPrice = new Amount(10);
    private final int ITEM_ID = 0;

    @BeforeEach
    void setUp() throws IOException {
        instance = new CashRegister(INITIAL_BALANCE);
        payment = new CashPayment(PAID_AMOUNT);
        try {
            sale = new Sale();
        } catch (OperationFailedException ex) {
            fail("Failed to setUp CashPaymentTest");
            ex.printStackTrace();
        }
        try {
            sale.addItem(ITEM_ID, 1);
        } catch (ItemRegistryException | ItemNotFoundInItemRegistryException | OperationFailedException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
            throw new RuntimeException(e);
        }
        sale.pay(payment);
    }

    @Test
    void addPayment() {
        instance.addPayment(payment);
        Amount balanceResult = instance.getBalance();
        Amount revenueResult = instance.getRevenue();
        Amount expRevenueResult = payment.getTotalCostPaid();
        Amount expBalanceResult = payment.getTotalCostPaid().plus(INITIAL_BALANCE);

        assertEquals(expBalanceResult, balanceResult, "Wrong balance based on payment");
        assertEquals(itemPrice.plus(INITIAL_BALANCE), balanceResult, "Wrong balance based on total item price");

        assertEquals(expRevenueResult, revenueResult, "Wrong revenue based on total cost paid");
        assertEquals(itemPrice, revenueResult, "Wrong revenue based on total item price");
    }
}