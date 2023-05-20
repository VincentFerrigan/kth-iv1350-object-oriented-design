package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.util.Event;
import se.kth.iv1350.view.EndOfSaleView;
import se.kth.iv1350.view.RunningSaleView;
import se.kth.iv1350.view.TotalRevenueView;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SaleTest {
    private Sale instance;
    private static final int TEST_ITEM_ID = 0;
    private static final int TEST_QUANTITY = 5;
    private static final String TEST_NAME = "test name";
    private static final String TEST_DESCRIPTION = "test description";
    private static final Amount TEST_UNIT_PRICE = new Amount(10);
    private static final VAT TEST_VAT = new VAT(1);
    private final ItemDTO TEST_ITEM_INFO = new ItemDTO(TEST_ITEM_ID,
            TEST_NAME, TEST_DESCRIPTION, TEST_UNIT_PRICE, TEST_VAT);
    private static final Amount TEST_PAID_AMOUNT = new Amount(5);
    private RegisterCreator registerCreator;
    private ByteArrayOutputStream outContent;
    private PrintStream originalSysOut;
    @BeforeEach
    void setUp() {
        instance = new Sale();
        originalSysOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Map<Event, List<SaleObserver>> saleObservers;
        saleObservers = new HashMap<>();
        Arrays.stream(Event.values()).forEach(event -> saleObservers.put(event, new ArrayList<>()));
        saleObservers.get(Event.NEW_ITEM).add(new RunningSaleView());
        saleObservers.get(Event.CHECKED_OUT).add(new EndOfSaleView());
        saleObservers.get(Event.PAID).add(new TotalRevenueView());
        instance.addSaleObservers(saleObservers);
    }

    @AfterEach
    void tearDown() {
        instance = null;
        outContent = null;
        System.setOut(originalSysOut);
    }

    @Test
    void testAddItem() {
        try {
            instance.addItem(TEST_ITEM_ID, TEST_QUANTITY);
            fail("Exception should have been thrown, the item was the first to be added to the shopping cart");
        } catch (ItemNotFoundInShoppingCartException e) {
            assertEquals(TEST_ITEM_ID,e.getItemIDNotFound());
        }
        instance.addItem(TEST_ITEM_INFO, TEST_QUANTITY);
        assertNotEquals(instance.isComplete(), true);
        int expResult = TEST_QUANTITY;
        SaleDTO saleInfo = instance.getSaleInfo();
        List<ShoppingCartItemDTO> listOfSaleItems = saleInfo.getSaleItemsInfo();
        int result = listOfSaleItems.get(0).getQuantity();
        assertEquals(expResult, result,"ShoppingCartItem quantity not equal");
        assertTrue(outContent.toString().contains("Display"),
                "No display output");
        try {
            instance.addItem(TEST_ITEM_ID, TEST_QUANTITY);
        } catch (ItemNotFoundInShoppingCartException ex) {
            fail("No exception should be thrown: item ID is valid." +
                    "%s".formatted(ex.getMessage()));
        }
        expResult += TEST_QUANTITY;
        saleInfo = instance.getSaleInfo();
        listOfSaleItems = saleInfo.getSaleItemsInfo();
        result = listOfSaleItems.get(0).getQuantity();
        assertEquals(expResult, result,"ShoppingCartItem quantity not equal");
        assertTrue(outContent.toString().contains("Display"),
                "No display output");
    }
    @Test
    void testItemNotFoundInShoppingCartException() {
        assertThrows(ItemNotFoundInShoppingCartException.class,
                () -> instance.addItem(TEST_ITEM_ID, TEST_QUANTITY));
        try {
            instance.addItem(TEST_ITEM_ID, TEST_QUANTITY);
            fail("Exception should have been thrown, the item was the first to be added to the shopping cart");
        } catch (ItemNotFoundInShoppingCartException ex) {
            assertEquals(TEST_ITEM_ID,ex.getItemIDNotFound());
        }
    }
    @Disabled
    @Test
    void applyDiscount(){}

    @Test
    void testPay() {
        instance.pay(new CashPayment(TEST_PAID_AMOUNT));
        assertTrue(outContent.toString().contains("Revenue update"),
                "Observer not notified");
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