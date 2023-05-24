package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.integration.ItemDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingCartItemTest {
    private ShoppingCartItem instance;
    private static final int TEST_ITEM_ID = 0;
    private final int TEST_QUANTITY = 5;
    private static final String TEST_NAME = "test name";
    private static final String TEST_DESCRIPTION = "test description";
    private static final Amount TEST_UNIT_PRICE = new Amount(10);
    private static final VAT TEST_VAT = new VAT(1);
    private final ItemDTO TEST_ITEM_INFO = new ItemDTO(TEST_ITEM_ID,
            TEST_NAME, TEST_DESCRIPTION, TEST_UNIT_PRICE, TEST_VAT);

    @BeforeEach
    void setUp() {
        instance = new ShoppingCartItem(this.TEST_ITEM_INFO, TEST_QUANTITY);
    }

    @AfterEach
    void tearDown() {
        instance = null;
    }

    @Test
    void testAddToQuantity() {
        instance.addToQuantity(TEST_QUANTITY);
        assertEquals(TEST_QUANTITY + TEST_QUANTITY, instance.getQuantity(),
                "ShoppingCartItem did not have correct quantity when quantity was added to an existing ShoppingCartItem.");
    }

    @Test
    void testCalculateVATCost() {
        double vatRate = 0.25;
        Amount expResult = TEST_UNIT_PRICE.multiply(TEST_QUANTITY).multiply(vatRate);
        Amount result = instance.calculateTotalSubVATCost();
        assertEquals(expResult, result, "Wrong vat");
    }

    @Test
    void testEquals() {
        ShoppingCartItem a = new ShoppingCartItem(TEST_ITEM_INFO, 2);
        ShoppingCartItem b = new ShoppingCartItem(TEST_ITEM_INFO,2);
        boolean expResult = true;
        boolean result =  a.equals(b);
        assertEquals(expResult,result,"Objects not equal");
    }

}