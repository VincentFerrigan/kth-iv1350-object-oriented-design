package se.kth.iv1350.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.ItemDTO;

import static org.junit.jupiter.api.Assertions.*;

class CashRegisterTest {

    private CashRegister instance;
    private final Amount INITIAL_BALANCE = new Amount(1000);
    private CashPayment payment;
    private final Amount PAID_AMOUNT = new Amount(500);
    private Sale sale;
    private ItemDTO itemInfo;
    private Amount itemPrice;

    @BeforeEach
    void setUp() {
        instance = new CashRegister(INITIAL_BALANCE);
        payment = new CashPayment(PAID_AMOUNT);
        try {
            sale = new Sale();
        } catch (OperationFailedException ex) {
            fail("Failed to setUp CashPaymentTest");
            ex.printStackTrace();
        }
        itemInfo = new ItemDTO(1, "Product name", "Product Description",
                new Amount(100), new VAT(1));
        itemPrice = itemInfo.getUnitPrice();
        sale.addItem(itemInfo, 1);
        sale.pay(payment);
    }

    @Test
    void addPayment() {
        instance.addPayment(payment);
        Amount balanceResult = instance.getBalance();
        Amount revenueResult = instance.getRevenue();
        Amount exRevenueResult = payment.getTotalCostPaid();
        Amount exBalanceResult = payment.getTotalCostPaid().plus(INITIAL_BALANCE);

        assertEquals(exBalanceResult, balanceResult, "Wrong balance based on payment");
        assertEquals(itemPrice.plus(INITIAL_BALANCE), balanceResult, "Wrong balance based on total item price");

        assertEquals(exRevenueResult, revenueResult, "Wrong revenue based on total cost paid");
        assertEquals(itemPrice, revenueResult, "Wrong revenue based on total item price");
    }
}