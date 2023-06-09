package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.POSTestSuperClass;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.dto.ItemDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ShoppingCartItemTest extends POSTestSuperClass {
    private ShoppingCartItem instance;
    private final int TEST_ITEM_ID = 0;
    private final int TEST_QUANTITY = 5;
    private final String TEST_NAME = "test name";
    private final String TEST_DESCRIPTION = "test description";
    private final Amount TEST_UNIT_PRICE = new Amount(10);
    private final Amount TEST_UNIT_PRICE_EX_VAT = TEST_UNIT_PRICE.multiply(1/1.25);
    private final int TEST_VAT_GROUP_CODE = 1;
    private final ItemDTO TEST_ITEM_INFO = new ItemDTO(TEST_ITEM_ID,
            TEST_NAME, TEST_DESCRIPTION, TEST_UNIT_PRICE_EX_VAT, TEST_VAT_GROUP_CODE);

    @BeforeEach
    void setUp() {
        try {
            instance = new ShoppingCartItem(this.TEST_ITEM_INFO, TEST_QUANTITY);
        } catch (PricingFailedException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
        }
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
        Amount expResult = TEST_UNIT_PRICE_EX_VAT.multiply(TEST_QUANTITY).multiply(vatRate);
        Amount result = instance.calculateTotalSubVATCost();
        assertEquals(expResult, result, "Wrong vat");
    }

    @Test
    void testEquals() {
        ShoppingCartItem a = null;
        ShoppingCartItem b = null;
        try {
            a = new ShoppingCartItem(TEST_ITEM_INFO, 2);
            b = new ShoppingCartItem(TEST_ITEM_INFO,2);
        } catch (PricingFailedException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
            throw new RuntimeException(e);
        }
        boolean expResult = true;
        boolean result =  a.equals(b);
        assertEquals(expResult,result,"Objects not equal");
    }

}