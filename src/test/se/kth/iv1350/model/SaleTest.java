package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SaleTest {
    private Map<Integer, Item> testCart;
    private final int testItemID = 4;

    @BeforeEach
    void setUp() {
        testCart = new HashMap<>();
    }

    @AfterEach
    void tearDown() {
        testCart = null;
    }

    @Test
    void testAddItem() {
    }

    @Test
    void testAddItem1() {
    }

    @Test
    void endSale() {
    }

    @Test
    void applyDiscount() {
    }

    @Test
    void pay() {
    }

    @Test
    void printReceipt() {
    }

    @Test
    void displayOpenSale() {
    }

    @Test
    void displayCheckout() {
    }

    @Test
    void updateInventory() {
    }
}