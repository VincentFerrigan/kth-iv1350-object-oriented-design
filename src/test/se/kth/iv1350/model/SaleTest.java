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
    private Sale sale;
    private ItemRegistry itemRegistry;
    private RegisterCreator registries;

    private final int VALID_ITEM_ID = 1;
    private final int INVALID_ITEM_ID = -2;
    private final int ERROR_ITEM_ID = 404;

    @BeforeEach
    void setUp() {
        try {
            registries = new RegisterCreator();
            itemRegistry = registries.getInventorySystem();
            sale = new Sale(itemRegistry);
        } catch (IOException ex) {
            System.out.println("Unable to set up SaleTest");
            ex.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        sale = null;
        itemRegistry = null;
        registries = null;
    }

    @Test
    void testAddItem() {
        int quantity = 2;
        try {
            sale.addItem(VALID_ITEM_ID, quantity);
        } catch (ItemNotFoundException ex) {
            fail("No exception of this type should be thrown: ItemID is valid.");
        } catch (ItemRegistryException ex) {
            fail("No connection exception should be thrown: connection is valid.");
        }
            int expResult = 2;
            SaleDTO saleInfo = sale.displayCheckout(new Display());
            List<SaleItemDTO> listOfSaleItems = saleInfo.getSaleItemsInfo();
            int result = listOfSaleItems.get(0).getQuantity();
            assertEquals(expResult, result, "Item quantity not equal");
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
        int quantity = 1;
        try {
            sale.addItem(VALID_ITEM_ID, quantity);
            sale.addItem(VALID_ITEM_ID, quantity);

            int expResult = 2;
            SaleDTO saleInfo = sale.displayCheckout(new Display());
            List<SaleItemDTO> listOfSaleItems = saleInfo.getSaleItemsInfo();
            int result = listOfSaleItems.get(0).getQuantity();
            assertEquals(expResult, result, "Item quantity not increased");
        } catch (ItemNotFoundException ex) {
            //This is not part of the test.
        }
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