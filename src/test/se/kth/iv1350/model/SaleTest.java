package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.integration.ItemRegistry;
import se.kth.iv1350.integration.RegisterCreator;
import se.kth.iv1350.integration.Display;
import se.kth.iv1350.util.LogHandler;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaleTest {
    private Sale sale;
    private ItemRegistry itemRegistry;
    private RegisterCreator registries;

    @BeforeEach
    void setUp() {
        //addItem
        try {
            registries = new RegisterCreator();
            itemRegistry = registries.getInventorySystem();
            sale = new Sale(itemRegistry);
        } catch (IOException ex) {
            System.out.println("Unable to set up SaleTest");
            ex.printStackTrace();
        }

        //applyDiscount
//          RegisterCreator disReg = new RegisterCreator();
//          DiscountRegister discountRegister = disReg.getDiscountRegister();
//          DiscountDTO discount = new DiscountDTO();
    }

    @AfterEach
    void tearDown() {
        sale = null;
        itemRegistry = null;
        registries = null;
    }

    @Test
    void testAddItem() {
        int itemID = 1;
        int quantity = 2;

        try {
            sale.addItem(itemID, quantity);
        } catch (ItemNotFoundException | ItemRegistryException ex){
            // Not part of the test
        }

        int expResult = 2;
        SaleDTO saleInfo = sale.updateRunningSaleInfo();
        List<SaleItemDTO> listOfSaleItems = saleInfo.getSaleItemsInfo();
        int result = listOfSaleItems.get(0).getQuantity();
        assertEquals(expResult,result,"Item quantity not equal");
    }

    @Test
    void testAddItemInvalidID() {
        int quantity = 2;
        try {
            sale.addItem(INVALID_ITEM_ID, quantity);
        } catch (ItemNotFoundException ex) {
            return;
        }
        fail("No exception was thrown when operating on an invalid item ID.");
    }

    @Test
    void testAddItemConnectionError() {
        int quantity = 2;
        try {
            sale.addItem(ERROR_ITEM_ID, quantity);
        } catch (ItemNotFoundException ex){
            //This exception is ok here.
        } catch (ItemRegistryException ex) {
            return;
        }
        fail("No exception was thrown when signaling no connection to server.");
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
            sale.addItem(itemID, quantity);
            sale.addItem(itemID, quantity);
        } catch (ItemNotFoundException | ItemRegistryException ex){
            // Not part of the test
        }

        int expResult = 2;
        SaleDTO saleInfo = sale.updateRunningSaleInfo();
        List<SaleItemDTO> listOfSaleItems = saleInfo.getSaleItemsInfo();
        int result = listOfSaleItems.get(0).getQuantity();
        assertEquals(expResult,result,"Item quantity not increased");
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