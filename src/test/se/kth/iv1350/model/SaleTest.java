package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.integration.*;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaleTest {
    private Sale instance;
    private RegisterCreator registerCreator;

    @BeforeEach
    void setUp() {
        try {
            registerCreator = new RegisterCreator();
            instance = new Sale(registerCreator.getInventorySystem());
        } catch (IOException ex) {
            // Not part of the test
            System.out.println("Unable to set up SaleTest");
            ex.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        instance = null;
        registerCreator = null;
    }

    @Test
    void testAddItem() {
        int itemID = 1;
        int quantity = 2;

        try {
            instance.addItem(itemID, quantity);
        } catch (ItemNotFoundException | ItemRegistryException ex){
            // Not part of the test
        }

        int expResult = 2;
        SaleDTO saleInfo = instance.getSaleInfo();
        List<ShoppingCartItemDTO> listOfSaleItems = saleInfo.getSaleItemsInfo();
        int result = listOfSaleItems.get(0).getQuantity();
        assertEquals(expResult,result,"ShoppingCartItem quantity not equal");
    }

    @Disabled
    @Test
    void getRunningTotal() {
    }

    @Disabled
    @Test
    void getTotalVATAmount() {
    }

    @Disabled
    @Test
    void testIncreaseItem() {
        int itemID = 1;
        int quantity = 1;

        try {
            instance.addItem(itemID, quantity);
            instance.addItem(itemID, quantity);
        } catch (ItemNotFoundException | ItemRegistryException ex){
            // Not part of the test
        }

        int expResult = 2;
        SaleDTO saleInfo = instance.getSaleInfo();
        List<ShoppingCartItemDTO> listOfSaleItems = saleInfo.getSaleItemsInfo();
        int result = listOfSaleItems.get(0).getQuantity();
        assertEquals(expResult,result,"ShoppingCartItem quantity not increased");
    }

    @Disabled
    @Test
    void endSale() {
    }

    @Disabled
    @Test
    void applyDiscount() {
    }

    @Disabled
    @Test
    void pay() {
    }

    @Disabled
    @Test
    void printReceipt() {
    }

    @Disabled
    @Test
    void displayOpenSale() {
    }

    @Disabled
    @Test
    void displayCheckout() {
    }

    @Disabled
    @Test
    void updateInventory() {
    }
}