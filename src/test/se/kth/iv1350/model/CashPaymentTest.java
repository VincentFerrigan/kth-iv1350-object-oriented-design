package se.kth.iv1350.model;

import org.junit.jupiter.api.*;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.integration.dto.CustomerDTO;
import se.kth.iv1350.integration.dto.ItemDTO;
import se.kth.iv1350.integration.pricing.CustomerType;
import se.kth.iv1350.startup.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CashPaymentTest {

    private CashPayment instance;
    private final double PAID = 500;
    private Amount paidAmount;
    private Sale sale;
    private ItemDTO itemInfo;
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
        paidAmount = new Amount(PAID);
        instance = new CashPayment(paidAmount);
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
        }
    }

    @AfterEach
    void tearDown() {
        instance = null;
        paidAmount = null;
        sale = null;
        itemInfo = null;
        itemPrice = null;
    }

    @Test
    void testCalculateTotalCost() {
        instance.calculateTotalCost(sale);
        Amount expResult = itemPrice;
        Amount result = instance.getTotalCostPaid();
        assertEquals(expResult, result, "Wrong total cost");
    }

    @Test
    void testCalculateTotalCostWithCustomerBonusAndDiscount() {
        CustomerDTO customerInfo = new CustomerDTO(1, CustomerType.MEMBER, 0);
        sale.addCustomerToSale(customerInfo);
        instance.calculateTotalCost(sale);
        Customer customer = sale.getCustomer();
        Amount expPriceResult = itemPrice.minus(sale.getDiscount());
        Amount totalCostPaidResult = instance.getTotalCostPaid();

        int bonusPointResult = customer.getBonusPoints();
        int expBonusPointResult = expPriceResult.getAmount().intValue();
        assertEquals(expPriceResult, totalCostPaidResult, "Wrong total cost");
        assertEquals(expBonusPointResult, bonusPointResult, "Bonus points not registered");
    }
    @Test
    void getTotalCostPaid() {
        instance.calculateTotalCost(sale);
        Amount expResult = itemPrice;
        Amount result = instance.getTotalCostPaid();
        assertEquals(expResult, result, "Wrong paid amount");
    }
    @Test
    void getChange() {
        instance.calculateTotalCost(sale);
        Amount expResult = paidAmount.minus(itemPrice);
        Amount result = instance.getChange();
        assertEquals(expResult, result, "Wrong change");
    }
}