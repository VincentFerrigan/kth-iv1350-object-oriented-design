package se.kth.iv1350.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.ItemNotFoundInItemRegistryException;
import se.kth.iv1350.integration.ItemRegistryFlatFileDB;
import se.kth.iv1350.integration.ItemRegistryException;
import se.kth.iv1350.startup.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CashRegisterTest {

    private CashRegister instance;
    private final Amount INITIAL_BALANCE = new Amount(1000);
    private CashPayment payment;
    private final Amount PAID_AMOUNT = new Amount(500);
    private Sale sale;
    private Amount itemPrice = new Amount(10);
    private final int ITEM_ID = 0;

    /**
     * Properties set up base on:
     * <a href=https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html>The Java™ Tutorials - System Properties</a>.
     * If you're having trouble loading the resource file <code>config.properties></code>,
     * first check that <code>src/test/resources</code>
     * is correctly configured as a resources directory in your IDE.
     */
    @BeforeAll
    static void setup() {
        Properties properties = new Properties(System.getProperties());
        try {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(inputStream);
            System.setProperties(properties);
        } catch (IOException ex) {
            System.out.println("Unable to set up configuration");
            ex.printStackTrace();
        }
    }
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