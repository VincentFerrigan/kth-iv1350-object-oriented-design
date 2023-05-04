package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.dto.DiscountDTO;
import se.kth.iv1350.dto.SaleDTO;
import se.kth.iv1350.dto.SaleItemDTO;
import se.kth.iv1350.integration.DiscountRegister;
import se.kth.iv1350.integration.ItemRegistry;
import se.kth.iv1350.integration.RegisterCreator;
import se.kth.iv1350.integration.Display;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SaleTest {
    private Sale sale;
    private ItemRegistry itemRegistry;
    private RegisterCreator registries;

    @BeforeEach
    void setUp() {
        //addItem
        registries = new RegisterCreator();
        itemRegistry = registries.getInventorySystem();
        sale = new Sale(itemRegistry);

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

        sale.addItem(itemID, quantity);

        int expResult = 2;
        SaleDTO saleInfo = sale.displayCheckout(new Display());
        List<SaleItemDTO> listOfSaleItems = saleInfo.getSaleItemsInfo();
        int result = listOfSaleItems.get(0).getQuantity();
        assertEquals(expResult,result,"Item quantity not equal");
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

        sale.addItem(itemID, quantity);
        sale.addItem(itemID, quantity);

        int expResult = 2;
        SaleDTO saleInfo = sale.displayCheckout(new Display());
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