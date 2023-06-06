package se.kth.iv1350.integration;

import org.junit.jupiter.api.*;
import se.kth.iv1350.POSTestSuperClass;
import se.kth.iv1350.integration.dto.CustomerDTO;
import se.kth.iv1350.integration.pricing.CustomerType;
import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.CashPayment;
import se.kth.iv1350.model.PricingFailedException;
import se.kth.iv1350.model.Sale;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaleLogTest extends POSTestSuperClass {
    private SaleLog instance;

    @BeforeEach
    void setUp() {
        instance = new SaleLog();
    }
    @AfterEach
    void tearDown() {
        instance = null;
    }
    @Nested
    @DisplayName("Log a couple of sales and find the them")
    class LogASaleAndFindItTest {
        private Sale sale;
        private CashPayment payment;
        private Amount paidAmt;
        @BeforeEach
        void setUp() {
            try {
                sale = new Sale();
            } catch (PricingFailedException ex) {
                // Not part of the test
                fail("No exception should be thrown, unable to set up the SaleLogTest. "
                        + "%s".formatted(ex.getMessage()));
            }
            try {
                sale.addItem(1, 1);
            } catch (ItemNotFoundInItemRegistryException | PricingFailedException | ItemRegistryException e) {
                fail("Exception should not have been thrown, " +
                        e.getMessage());
            }
            paidAmt = new Amount(100);
            payment = new CashPayment(paidAmt);
            sale.endSale();
        }
        @AfterEach
        void tearDown() {
            sale = null;
            paidAmt = null;
            payment = null;
        }
        @Test
        @DisplayName("log a sale without a customer registered")
        void testLogSale() {
//            sale.addCustomerToSale(new CustomerDTO(1, CustomerType.MEMBER,10));
            sale.pay(payment);
            instance.logSale(sale);
            List<Sale> sales = instance.getSales();
            assertAll("Check the list of logged sales",
            () -> assertFalse(sales.isEmpty(), "Sale log is empty"),
            () -> assertTrue(sales.contains(sale), "Sale log does not contain the logged sale"));
        }
        @Test
        @DisplayName("log a sale with a customer registered")
        void testLogAMembersSale() {
            sale.addCustomerToSale(new CustomerDTO(1, CustomerType.MEMBER,10));
            sale.pay(payment);
            instance.logSale(sale);
            List<Sale> sales = instance.getSales();
            assertAll("Check the list of logged sales",
                    () -> assertFalse(sales.isEmpty(), "Sale log is empty"),
                    () -> assertTrue(sales.contains(sale), "Sale log does not contain the logged sale"));
        }
        @Test
        @DisplayName("Find a customers sale by CustomerID")
        void findSalesByCustomerID() {
            sale.addCustomerToSale(new CustomerDTO(1, CustomerType.MEMBER,10));
            sale.pay(payment);
            instance.logSale(sale);
            List<Sale> salesWithCustomerID = instance.findSalesByCustomerID(1);
            List<Sale> salesWithWrongCustomerID = instance.findSalesByCustomerID(2);
            assertAll("Check the list of logged sales",
                    () -> assertFalse(salesWithCustomerID.isEmpty(), "Sale log is empty"),
                    () -> assertTrue(salesWithWrongCustomerID.isEmpty(), "Sale log is empty"),
                    () -> assertTrue(salesWithCustomerID.contains(sale), "Sale log does not contain the logged sale"),
                    () -> assertFalse(salesWithWrongCustomerID.contains(sale), "Sale log does not contain the logged sale")
            );
        }
    }

}