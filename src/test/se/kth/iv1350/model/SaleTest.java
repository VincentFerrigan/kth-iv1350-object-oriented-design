package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.integration.pricing.CustomerType;
import se.kth.iv1350.view.EndOfSaleView;
import se.kth.iv1350.view.RunningSaleView;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/* TODO: To do and keep the following for the report.
 * The current pay method makes the controller bloated and tend towards low cohesion.
 * Solution: Move payment creation to sale. Sale has to anyway "keep" payment details.
 * Open issue: What to do with the cash register?
 *
 */
class SaleTest {
    private Sale instance;
    private static final int TEST_ITEM_ID = 0;
    private static final int TEST_QUANTITY = 5;
    private static final String TEST_NAME = "test name";
    private static final String TEST_DESCRIPTION = "test description";
    private static final Amount TEST_UNIT_PRICE = new Amount(10);
    private static final Amount TEST_UNIT_PRICE_EX_VAT = TEST_UNIT_PRICE.multiply(1/1.25);
    private static final VAT TEST_VAT = new VAT(1);
    private final ItemDTO TEST_ITEM_INFO = new ItemDTO(TEST_ITEM_ID,
            TEST_NAME, TEST_DESCRIPTION, TEST_UNIT_PRICE_EX_VAT, TEST_VAT);
    private static final Amount TEST_PAID_AMOUNT = new Amount(100);
    private ByteArrayOutputStream outContent;
    private PrintStream originalSysOut;

    @BeforeEach
    void setUp() throws OperationFailedException {
        instance = new Sale();
        originalSysOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        List<SaleObserver> saleObservers;
        saleObservers = new ArrayList<>();
        saleObservers.add(new RunningSaleView());
        saleObservers.add(new EndOfSaleView());
        instance.addAllSaleObservers(saleObservers);
//        // TODO Nedan korrekt? Funkar enbart om cash reg skulle köras från sale. Vilket den inte gör§v
//        CashRegister cashRegister = new CashRegister();
//        cashRegister.addCashRegisterObserver(new TotalRevenueView());
    }

    @AfterEach
    void tearDown() {
        instance = null;
        outContent = null;
        System.setOut(originalSysOut);
    }

    @Test
    void testAddItem() {
        // First addItem with item id
        try {
            instance.addItem(TEST_ITEM_ID, TEST_QUANTITY);
            fail("Exception should have been thrown, the item was the first to be added to the shopping cart");
        } catch (ItemNotFoundInShoppingCartException e) {
            assertEquals(TEST_ITEM_ID, e.getItemIDNotFound());
        }

        // First addItem with item dto
        try {
            instance.addItem(TEST_ITEM_INFO, TEST_QUANTITY);
        } catch (OperationFailedException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
            throw new RuntimeException(e);// TODO behövs denna?
        }
        assertEquals(instance.isComplete(), false);

        int expResult = TEST_QUANTITY;
        String outputResult = outContent.toString();
        List<ShoppingCartItem> listOfSaleItems = new ArrayList<>(instance.getCollectionOfItems());
        int result = listOfSaleItems.get(0).getQuantity();

        assertEquals(expResult, result, "ShoppingCartItem quantity not equal");
        assertTrue(outputResult.contains("Display"), "No display output");

        // second addItem with item id
        try {
            instance.addItem(TEST_ITEM_ID, TEST_QUANTITY);
        } catch (ItemNotFoundInShoppingCartException ex) {
            fail("No exception should be thrown: item ID is valid." +
                    "%s".formatted(ex.getMessage()));
        }
        int expResultOfAddedQuantity = expResult + TEST_QUANTITY;
        StringBuilder expOutMultipleItems = new StringBuilder();
        expOutMultipleItems.append("%-40s%s%n".formatted(TEST_NAME, TEST_UNIT_PRICE.multiply(expResult)));
        expOutMultipleItems.append("(%d * %s)\n".formatted(expResult, TEST_UNIT_PRICE));

        String outputResultOfAddedQuantity = outContent.toString();
        result = listOfSaleItems.get(0).getQuantity();

        assertEquals(expResultOfAddedQuantity, result, "ShoppingCartItem quantity not equal");
        assertTrue(outputResultOfAddedQuantity.contains("Display"), "No display output");
        assertTrue(outContent.toString().contains(expOutMultipleItems.toString()),
                "ShoppingCartItem did not have the right quantity when added with quantity.");
    }

    @Test
    void testItemNotFoundInShoppingCartException() {
        assertThrows(ItemNotFoundInShoppingCartException.class,
                () -> instance.addItem(TEST_ITEM_ID, TEST_QUANTITY));
        try {
            instance.addItem(TEST_ITEM_ID, TEST_QUANTITY);
            fail("Exception should have been thrown, the item was the first to be added to the shopping cart");
        } catch (ItemNotFoundInShoppingCartException ex) {
            assertEquals(TEST_ITEM_ID, ex.getItemIDNotFound());
        }
    }

    @Disabled
    @Test
    void testAddCustomerToSale() {
        CustomerDTO customerInfo = new CustomerDTO(1, CustomerType.MEMBER, 0);
        Customer expResult = new Customer(customerInfo);
        instance.addCustomerToSale(customerInfo);
        Customer result = instance.getCustomer();
        assertEquals(expResult, result, "Customer not equal");
    }

    @Test
    void testCalculateRunningTotal() {
        try {
            instance.addItem(TEST_ITEM_INFO,TEST_QUANTITY);
        } catch (OperationFailedException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
            throw new RuntimeException(e);
        }
        Amount expResult = TEST_UNIT_PRICE.multiply(TEST_QUANTITY);
        Amount result = instance.calculateRunningTotal();
        assertEquals(expResult, result, "Wrong running total");
    }
    @Test
    void testCalculateTotalPrice() {

        try {
            instance.addItem(TEST_ITEM_INFO, TEST_QUANTITY);
        } catch (OperationFailedException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
            throw new RuntimeException(e);
        }
        instance.addCustomerToSale(new CustomerDTO(1, CustomerType.STAFF, 0));
        Amount discount = instance.getDiscount();
        Amount expResult = TEST_UNIT_PRICE.multiply(TEST_QUANTITY).minus(discount);
        Amount result = instance.calculateRunningTotal();
        assertEquals(expResult, result, "Wrong total price");
    }

    @Test
    void testPay() {
        instance.pay(new CashPayment(TEST_PAID_AMOUNT));
//        assertTrue(outContent.toString().contains("Revenue update"),
//                "Observer not notified");
        assertEquals(TEST_PAID_AMOUNT, instance.getPayment().getPaidAmt(),
                "Paid amount not correctly registered");
    }
    @Test
    void testPrintReceipt() {
        instance.pay(new CashPayment(TEST_PAID_AMOUNT));
        instance.printReceipt(new Printer());
        assertTrue(outContent.toString().contains("Receipt"),
                "Receipt not printed");
    }

    @Test
    void testEndSale() {
        instance.endSale();
        assertEquals(true, instance.isComplete());
        String result = outContent.toString();
        assertTrue(result.contains("End of Sale"), "End sale did not work");
    }
}