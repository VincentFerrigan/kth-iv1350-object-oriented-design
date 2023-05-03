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

    @BeforeEach
    void setUp() {
        testItem = new Item(this.itemInfo, quantity);
    }

    @AfterEach
    void tearDown() {
        testItem = null;
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
        //TODO or not TODO
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