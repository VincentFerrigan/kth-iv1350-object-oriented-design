package se.kth.iv1350.model;

import org.junit.jupiter.api.*;
import se.kth.iv1350.POSTestSuperClass;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.integration.dto.CustomerDTO;
import se.kth.iv1350.integration.dto.ItemDTO;
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
class SaleTest extends POSTestSuperClass {
    private Sale instance;
    private static final int TEST_ITEM_ID = 0;
    private static final int TEST_QUANTITY = 5;
    private static final String TEST_NAME = "test name";
    private static final String TEST_DESCRIPTION = "test description";
    private static final Amount TEST_UNIT_PRICE = new Amount(10);
    private static final Amount TEST_PAID_AMOUNT = new Amount(100);
    private ByteArrayOutputStream outContent;
    private PrintStream originalSysOut;

    /* Om du nu skulle skapa din alldeles egna test-inv-txt
    private final String TEST_PRICE = "8";
    private static final Amount TEST_UNIT_PRICE_EX_VAT = TEST_UNIT_PRICE.multiply(1/1.25);
    private static final int TEST_VAT_GROUP_CODE = 1;
    */

    @BeforeEach
    void setUp() throws OperationFailedException {
        instance = new Sale();
        // Ska observers/displayView testas här eller för sig själva?
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

    @Nested
    @DisplayName("Adding items to sale")
    class SaleAddItemsTest {
        private int test_quantity = 5;
        private int expResult;
        private StringBuilder expOutputResult;
        private int result;
        private String outputResult;

        @Test
        @DisplayName("First item")
        void testAddFirstItem() { // TODO: Kommer den med i coverage report?
            try {
                instance.addItem(TEST_ITEM_ID, TEST_QUANTITY);
            } catch (ItemNotFoundInItemRegistryException | OperationFailedException | ItemRegistryException e) {
                fail("Exception should not have been thrown, " +
                        e.getMessage());
            }

            expResult = test_quantity;

            String outputResult = outContent.toString(); // TODO ska testas utanför
            List<ShoppingCartItem> listOfSaleItems = new ArrayList<>(instance.getCollectionOfItems());
            result = listOfSaleItems.get(0).getQuantity();

            assertAll(
                    "Verify alternative flow 3-4C",
                    () -> assertEquals(instance.isComplete(), false), // TODO: Ska denna testas utanför?
                    () -> assertEquals(expResult, result, "ShoppingCartItem quantity not equal"),
                    () -> assertTrue(outputResult.contains("Display"), "No display output") // TODO: Ska testas utanför
            );
        }
        @Test
        @DisplayName("Second item")
        void testAddSecondItem() {
            try {
                instance.addItem(TEST_ITEM_ID, test_quantity);
            } catch (ItemNotFoundInItemRegistryException | OperationFailedException | ItemRegistryException e) {
                fail("Exception should not have been thrown, " +
                        e.getMessage());
                throw new RuntimeException(e);// TODO behövs denna?
            }

            expResult += test_quantity;
            expOutputResult = new StringBuilder();
            expOutputResult.append("%-40s%s%n".formatted(TEST_NAME, TEST_UNIT_PRICE.multiply(expResult)));
            expOutputResult.append("(%d * %s)\n".formatted(expResult, TEST_UNIT_PRICE));

            String outputResultOfAddedQuantity = outContent.toString();
            List<ShoppingCartItem> listOfSaleItems = new ArrayList<>(instance.getCollectionOfItems());
            result = listOfSaleItems.get(0).getQuantity();

            assertAll(
                    "Verify alternative flow 3-4B",
                    () -> assertEquals(expResult, result, "ShoppingCartItem quantity not equal"),
                    () -> assertTrue(outputResultOfAddedQuantity.contains("Display"), "No display output"),
                    () -> assertTrue(outContent.toString().contains(expOutputResult.toString()),
                    "ShoppingCartItem did not have the right quantity when added with quantity.")
            );
        }
        @Test
        @DisplayName("Calculate running total")
        void testCalculateRunningTotal() {
            Amount expPriceResult = TEST_UNIT_PRICE.multiply(expResult);
            Amount priceResult = instance.calculateRunningTotal();
            assertEquals(expPriceResult, priceResult, "Wrong running total");
        }
    }

    @Test
    @DisplayName("Add Customer to Sale")
    void testAddCustomerToSale() {
        CustomerDTO customerInfo = new CustomerDTO(1, CustomerType.MEMBER, 0);
        Customer expResult = new Customer(customerInfo);
        instance.addCustomerToSale(customerInfo);
        Customer result = instance.getCustomer();
        assertEquals(expResult, result, "Customer not equal");
    }

    @Test
    @DisplayName("Calculate running total")
    void testCalculateRunningTotal() {
        try {
            instance.addItem(TEST_ITEM_ID,TEST_QUANTITY);
        } catch (ItemRegistryException | ItemNotFoundInItemRegistryException | OperationFailedException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
        }

        Amount expResult = TEST_UNIT_PRICE.multiply(TEST_QUANTITY);
        Amount result = instance.calculateRunningTotal();
        assertEquals(expResult, result, "Wrong running total");
    }
    @Test
    @DisplayName("Calculate total price")
    void testCalculateTotalPrice() {

        try {
            instance.addItem(TEST_ITEM_ID,TEST_QUANTITY);
        } catch (ItemRegistryException | ItemNotFoundInItemRegistryException | OperationFailedException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
        }

        instance.addCustomerToSale(new CustomerDTO(1, CustomerType.STUDENT, 0));
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