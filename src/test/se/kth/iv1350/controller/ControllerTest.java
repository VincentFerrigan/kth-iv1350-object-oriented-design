package se.kth.iv1350.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.model.*;
import se.kth.iv1350.integration.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    private static final int ITEM_ID = 3;
    private static final int QUANTITY = 5;
    private static final int CUSTOMER_ID = 880822;
    private Controller instance;
    private RegisterCreator registerCreator;
    private ByteArrayOutputStream outContent;
    private PrintStream originalSysOut;

    @BeforeEach
    void setUp() {
        try {
            originalSysOut = System.out;
            outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
            Printer printer = new Printer();
            registerCreator = new RegisterCreator();
            instance = new Controller(printer, registerCreator);

        } catch (IOException ex)  {
            // Not part of the test
            System.out.println("Unable to set up the ControllerTest");
            ex.printStackTrace();
        }
    }
    @AfterEach
    void tearDown() {
        outContent = null;
        System.setOut(originalSysOut);
        instance = null;
        registerCreator = null;
    }

    @Test
    void testStartSale() {
        instance.startSale();
        try {
            instance.registerItem(ITEM_ID);
        } catch (NullPointerException ex) {
            fail("No instance of Sale was created in startSale");
        } catch (ItemNotFoundException | OperationFailedException ex) {
            // Not part of the test
        }
    }

    @Test
    void testRegisterItem() {
        instance.startSale();

        try {
            SaleDTO testRegID = instance.registerItem(ITEM_ID);
            assertNotNull(testRegID, "Item registration did not work");
            assertEquals(1, testRegID.getSaleItemsInfo().get(0).getQuantity(),
                    "Item did not have quantity 1 when added without quantity.");

            SaleDTO testRegIDQ = instance.registerItem(4, QUANTITY);
            assertNotNull(testRegIDQ, "Item registration did not work");
            assertEquals(QUANTITY, testRegIDQ.getSaleItemsInfo().get(0).getQuantity(),
                    "Item did not have the right quantity when added with quantity.");
        } catch (ItemNotFoundException | OperationFailedException ex) {
            // Not part of the test
        }
    }

    @Test
    void testRegisterItemNEW() {
        instance.startSale();
        int expectedSingularQuantity = 1;

        try {
            SaleItemDTO expResult = new SaleItemDTO(new ItemDTO(0, "", "", new Amount(1.0), new VAT(1)), expectedSingularQuantity, new Amount(1.0 * expectedSingularQuantity));
            SaleItemDTO result = instance.registerItem(0).getSaleItemsInfo().get(0);
            assertEquals(expResult, result, "Wrong quantity, expected %d".formatted(expectedSingularQuantity));
        } catch (ItemNotFoundException | OperationFailedException ex) {
            // Not part of the test
        }
    }

    @Test
    void testEndSale() {
        instance.startSale();
        SaleDTO saleInfo = instance.endSale();
        assertNotNull(saleInfo, "End sale did not work");
    }

    @Test
    void testDiscountRequest() {
    }

    @Test
    void testPay() {
        instance.startSale();
        try {
            instance.registerItem(ITEM_ID);
            SaleDTO paySaleInfo = instance.endSale();
            Amount paidAmount = new Amount(100);
            LocalDateTime saleTime = LocalDateTime.now();
            instance.pay(paidAmount);
            StringBuilder expOut = new StringBuilder();
            expOut.append("%-40s%s%n".formatted("Total Cost:", paySaleInfo.getTotalPrice()));
            expOut.append("%-40s%s%n".formatted("Total VAT:", paySaleInfo.getTotalVATAmount()));
            expOut.append("\n");
            expOut.append("%-40s%s%n".formatted("Paid Amount:", paidAmount));
            expOut.append("%-40s%s%n".formatted("Change:", paidAmount.minus(paySaleInfo.getTotalPrice())));
            String result = outContent.toString();
            assertTrue(result.contains(expOut), "Wrong output of change and paid amount.");
            assertTrue(result.contains("100,00 kr"), "Output does not contain paid amount.");
            assertTrue(result.contains(Integer.toString(saleTime.getYear())), "Wrong year on receipt.");
            assertTrue(result.contains(Integer.toString(saleTime.getHour())), "Wrong hour on receipt.");
            assertTrue(result.contains(Integer.toString(saleTime.getMinute())), "Wrong minute on receipt.");
            assertTrue(result.contains(paidAmount.minus(paySaleInfo.getTotalPrice()).toString()), "Wrong change on receipt.");
        } catch (ItemNotFoundException | OperationFailedException ex) {
            // Not part of the test
        }
    }
}