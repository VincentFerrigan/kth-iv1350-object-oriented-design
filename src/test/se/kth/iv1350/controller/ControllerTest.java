package se.kth.iv1350.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.model.*;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.startup.Main;
import se.kth.iv1350.view.EndOfSaleView;
import se.kth.iv1350.view.RunningSaleView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/* TODO: Would it be possible to setup ones own inventory list?
   TODO: Check how to send filename to singletons.
   Temp. solution is to use ITEM_ID 0
 */
class ControllerTest {
    private static final int TEST_ITEM_ID = 0;
    private static final int TEST_QUANTITY = 5;
    private static final String TEST_NAME = "test name";
    private static final Amount TEST_UNIT_PRICE_INCL_VAT = new Amount(10);
    private static final Amount TEST_UNIT_PRICE_EX_VAT = new Amount(8);
    private static final int TEST_VAT_GROUP_CODE = 1;
    private static final Amount PAID_AMOUNT = new Amount(100);
    private static final String TEST_DESCRIPTION = "test description";
    private final ItemDTO TEST_ITEM_INFO = new ItemDTO(TEST_ITEM_ID,
            TEST_NAME, TEST_DESCRIPTION, TEST_UNIT_PRICE_EX_VAT, TEST_VAT_GROUP_CODE);
    private static final int CUSTOMER_ID = 880822;
    private Controller instance;
    private RegisterCreator registerCreator;
    private ByteArrayOutputStream outContent;
    private PrintStream originalSysOut;

    @BeforeEach
    void setUp() {
        try {
            System.setProperty("se.kth.iv1350.database.file.location","data/db");
            System.setProperty("se.kth.iv1350.database.file.accounting_db","accounting.csv");
            System.setProperty("se.kth.iv1350.database.file.customer_db","customers.csv");
            System.setProperty("se.kth.iv1350.database.file.inventory_db","inventory_items.csv");
            System.setProperty("se.kth.iv1350.log.file.revenue_log","=revenue-log.txt");
            System.setProperty("se.kth.iv1350.log.file.location","data/log");
            System.setProperty("se.kth.iv1350.log.file.error_log","error-log.txt");
            System.setProperty("se.kth.iv1350.database.file.csv_delimiter",";");
            System.setProperty("se.kth.iv1350.discount_strategy_classname","se.kth.iv1350.integration.pricing.MemberDiscount,se.kth.iv1350.integration.pricing.StudentDiscount,se.kth.iv1350.integration.pricing.Promotion");
            System.setProperty("se.kth.iv1350.vat_strategy.classname","se.kth.iv1350.integration.vat.SwedishVAT");
            originalSysOut = System.out;
            outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
            Printer printer = new Printer();
            registerCreator = new RegisterCreator();
            instance = new Controller(printer, registerCreator);
            instance.addSaleObserver(new RunningSaleView());
            instance.addSaleObserver(new EndOfSaleView());

        } catch (IOException ex)  {
            // Not part of the test
            fail("No exception should be thrown, unable to set up the ControllerTest. " +
                    "%s".formatted(ex.getMessage()));
        }
    }
    @AfterEach
    void tearDown() {
        outContent = null;
        System.setOut(originalSysOut);
        instance = null;
        registerCreator = null;
    }

    @Test
    void testAddSaleObserver() {
        try {
            instance.addSaleObserver(new RunningSaleView());
        } catch (NullPointerException ex) {
            fail("Unable to add observer to subscription. " +
                    "No instance of Map (for saleObservers) was created");
        }
    }
    @Test
    void testStartSale() {
        try {
            instance.startSale();
            instance.registerItem(TEST_ITEM_ID);
        } catch (NullPointerException ex) {
            fail("No instance of Sale was created in startSale");
        } catch (ItemNotFoundInItemRegistryException ex) {
            // Not part of the test
            fail("No exception should be thrown: item ID is valid." +
                    "%s".formatted(ex.getMessage()));
        } catch(OperationFailedException ex) {
            // Not part of the test
            fail("No exception should be thrown: %s".formatted(ex.getMessage()));
        }

        assertTrue(outContent.toString().contains("Display"),
                "The observers were passed on by startSale to the sales subscription list");
    }

    @Test
    void testRegisterItem() {
        int singleQuantity = 1;
        try {
            instance.startSale();
            instance.registerItem(TEST_ITEM_ID);
            String result = outContent.toString();
            assertTrue(result.contains("Display"),
                    "No display output");
            StringBuilder expOutSingleItem = new StringBuilder();
            expOutSingleItem.append("%-40s%s%n".formatted(TEST_NAME, TEST_UNIT_PRICE_INCL_VAT.multiply(singleQuantity)));
            expOutSingleItem.append("(%d * %s)\n".formatted(singleQuantity, TEST_UNIT_PRICE_INCL_VAT));
            assertTrue(result.contains(expOutSingleItem.toString()),
                    "ShoppingCartItem registration did not work");

            instance.registerItem(TEST_ITEM_ID, TEST_QUANTITY);
            result = outContent.toString();
            StringBuilder expOutMultipleItems = new StringBuilder();
            expOutMultipleItems.append("%-40s%s%n".formatted(TEST_NAME, TEST_UNIT_PRICE_INCL_VAT.multiply(TEST_QUANTITY + singleQuantity)));
            expOutMultipleItems.append("(%d * %s)\n".formatted(TEST_QUANTITY + singleQuantity, TEST_UNIT_PRICE_INCL_VAT));
            assertTrue(result.contains(expOutMultipleItems.toString()),
                            "ShoppingCartItem did not have the right quantity when added with quantity.");
        } catch (ItemNotFoundInItemRegistryException ex) {
            fail("No exception should be thrown: item ID should be valid. + " +
                    "Error message: %s".formatted(ex.getMessage()));
        } catch(OperationFailedException ex) {
            // Not part of the test
            fail("No exception should be thrown: %s" +
            "Error message: %s".formatted(ex.getMessage()));
        }
    }

    @Test
    void testItemThatDoesNotExist() {
        int incorrectItemID = -1;
        try {
            instance.startSale();
            assertThrows(ItemNotFoundInItemRegistryException.class,() -> instance.registerItem(incorrectItemID));
            instance.registerItem(incorrectItemID);
        } catch(OperationFailedException ex) {
            fail("Wrong exception called" +
                    "Error message: %s".formatted(ex.getMessage()));
        } catch (ItemNotFoundInItemRegistryException ex) {
            assertTrue(ex.getMessage()
                    .contains("Unable to find item with ID \"%d\" in the inventory system.".formatted(incorrectItemID)));
        }
    }
    @Test
    void testNoConnectionToInventorySystem() {
        try {
            instance.startSale();
            assertThrows(OperationFailedException.class,() -> instance.registerItem(404));
            instance.registerItem(404);
        } catch (ItemNotFoundInItemRegistryException ex) {
            fail("Wrong exception called" +
                    "Error message: %s".formatted(ex.getMessage()));
        } catch(OperationFailedException ex) {
            assertTrue(ex.getMessage().contains("No connection to inventory system. Try again."));
        }
    }

    @Test
    void testEndSale() {
        try {
            instance.startSale();
            instance.registerItem(TEST_ITEM_ID);
        } catch (ItemNotFoundInItemRegistryException | OperationFailedException ex) {
            // Not part of the test
        }
        instance.endSale();
        String result = outContent.toString();
        assertTrue(result.contains("End of Sale"), "End sale did not work");
    }

    @Test
    void testPay() {
        try {
            instance.startSale();
            instance.registerItem(TEST_ITEM_ID);
            instance.endSale();
            LocalDateTime saleTime = LocalDateTime.now();
            instance.pay(PAID_AMOUNT);
            StringBuilder expOut = new StringBuilder();
            expOut.append("%-40s%s%n".formatted("Total Cost:", TEST_UNIT_PRICE_INCL_VAT.multiply(1)));
            expOut.append("%-40s%s%n".formatted("Total VAT:", TEST_UNIT_PRICE_EX_VAT.multiply(1).multiply(0.25)));
            expOut.append("\n");
            expOut.append("%-40s%s%n".formatted("Paid Amount:", PAID_AMOUNT));
            expOut.append("%-40s%s%n".formatted("Change:", PAID_AMOUNT.minus(TEST_UNIT_PRICE_INCL_VAT.multiply(1))));
            String result = outContent.toString();
            assertTrue(result.contains(expOut), "Wrong output of change and paid amount.");
            assertTrue(result.contains(PAID_AMOUNT.toString()), "Output does not contain paid amount.");
            assertTrue(result.contains(Integer.toString(saleTime.getYear())), "Wrong year on receipt.");
            assertTrue(result.contains(Integer.toString(saleTime.getHour())), "Wrong hour on receipt.");
            assertTrue(result.contains(Integer.toString(saleTime.getMinute())), "Wrong minute on receipt.");
            assertTrue(result.contains(PAID_AMOUNT.minus(TEST_UNIT_PRICE_INCL_VAT.multiply(1)).toString()), "Wrong change on receipt.");
        } catch (ItemNotFoundInItemRegistryException | OperationFailedException ex) {
            // Not part of the test
        }
    }
}