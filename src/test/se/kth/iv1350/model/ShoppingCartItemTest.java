package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.integration.ItemDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShoppingCartItemTest {
    private ShoppingCartItem instance;
    private final ItemDTO ITEM_INFO = new ItemDTO(3, "Jordgubbe", "", new Amount(15), new VAT(1));
    private final int QUANTITY = 5;

    @BeforeEach
    void setUp() {
        instance = new ShoppingCartItem(this.ITEM_INFO, QUANTITY);
    }

    @AfterEach
    void tearDown() {
        instance = null;
    }
    @Disabled
    @Test
    void testAddItem() {
    }

    @Test
    void testSetQuantity() {
        int newQuantity = 7;
        instance.setQuantity(newQuantity);
        assertEquals(newQuantity, instance.getQuantity(),
                "ShoppingCartItem did not have correct quantity when set.");
    }

    @Test
    void testAddToQuantity() {
        int addedQuantity = 4;
        instance.addToQuantity(addedQuantity);
        assertEquals(QUANTITY +addedQuantity, instance.getQuantity(),
                "ShoppingCartItem did not have correct quantity when quantity was added to an existing ShoppingCartItem.");
    }

    @Disabled
    @Test
    void testEquals() {
        ShoppingCartItem a = new ShoppingCartItem(ITEM_INFO);
        ShoppingCartItem b = new ShoppingCartItem(ITEM_INFO,2);
        boolean expResult = true;
        boolean result =  a.equals(b);
        assertEquals(expResult,result,"Objects not equal");
    }

    @Disabled
    @Test
    void testIncrement() {
        int incrementedAmount = QUANTITY + 1;
        instance.increment();
        assertEquals(incrementedAmount, instance.getQuantity(),
                "ShoppingCartItem did not have correct quantity when incremented.");
    }

    @Disabled
    @Test
    void testDecrement() {
        int decrementedAmount = QUANTITY - 1;
        instance.increment();
        assertEquals(decrementedAmount, instance.getQuantity(),
                "ShoppingCartItem did not have correct quantity when decremented.");
    }
}