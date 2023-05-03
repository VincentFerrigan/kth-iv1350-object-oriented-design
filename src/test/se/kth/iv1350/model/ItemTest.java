package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.dto.ItemDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemTest {
    private Item testItem;
    private ItemDTO itemInfo;
    private final int quantity = 5;
    Object a;
    Object b;
    Object c;
    Object d;
    @BeforeEach
    void setUp() {
        testItem = new Item(this.itemInfo, quantity);
        a = 2;
        b = 2;
        c = 3;
        d = null;

    }

    @AfterEach
    void tearDown() {
        testItem = null;
        a = null;
        b = null;
        c = null;
    }

    @Test
    void testAddItem() {
        //TODO or not TODO
    }

    @Test
    void testSetQuantity() {
        int newQuantity = 7;
        testItem.setQuantity(newQuantity);
        assertEquals(newQuantity, testItem.getQuantity(),
                "Item did not have correct quantity when set.");
    }

    @Test
    void testAddToQuantity() {
        int addedQuantity = 4;
        testItem.addToQuantity(addedQuantity);
        assertEquals(quantity+addedQuantity, testItem.getQuantity(),
                "Item did not have correct quantity when quantity was added to an existing Item.");
    }



    @Disabled
    @Test
    void testEquals() {
        boolean expResult = true;
        boolean result =  a.equals(b);
        assertEquals(expResult,result,"Objects not equal");
    }
    @Disabled
    @Test
    void testNotEqual() {
        boolean expResult = false;
        boolean result =  a.equals(c);
        assertEquals(expResult,result,"Objects are equal");
    }
    @Disabled
    @Test
    void testEqualsNull() {
        boolean expResult = false;
        boolean result = a.equals(d);
        assertEquals(expResult,result,"insance equals null");
    }
    @Disabled
    @Test
    void testEqualsJavaLangObject() {
        Object other = new Object();
        boolean expResult = false;
        boolean result = other.equals(testItem);
        assertEquals(expResult,result,"insance equal java lang Object");
    }

    @Disabled
    @Test
    void testNotEqualNoArgConstr() {
        Object other = new Object();
        boolean expResult = false;
        boolean result = other.equals(d);
        assertEquals(expResult,result,"insance with different states is equal");
    }


    @Disabled
    @Test
    void testIncrement() {
        int incrementedAmount = quantity + 1;
        testItem.increment();
        assertEquals(incrementedAmount, testItem.getQuantity(),
                "Item did not have correct quantity when incremented.");
    }

    @Disabled
    @Test
    void testDecrement() {
        //TODO or not TODO
    }
}