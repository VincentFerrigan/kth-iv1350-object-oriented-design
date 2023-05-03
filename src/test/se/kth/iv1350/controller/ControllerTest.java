package se.kth.iv1350.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.dto.DiscountDTO;
import se.kth.iv1350.dto.SaleDTO;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    private Controller controller;
    private Printer printer;
    private Display display;
    private RegisterCreator registerCreator;
    private final int ITEM_ID = 3;
    private final int QUANTITY = 5;

    private SaleDTO testRegID;
    private SaleDTO testRegIDQ;
    private Sale testSale;
    private SaleDTO currentSale;
    private DiscountDTO discountDTO;
    private final int CUSTOMER_ID = 880822;
    private ItemRegistry itemRegistry;
    private DiscountRegister discountRegister;
    private Amount paidAmount;
    private ByteArrayOutputStream outContent;
    private PrintStream originalSysOut;


    @BeforeEach
    void setUp() {
        registerCreator = new RegisterCreator();
        itemRegistry = registerCreator.getInventorySystem();
        testSale = new Sale(itemRegistry);
        discountRegister = registerCreator.getDiscountRegister();
        printer = new Printer();
        display = new Display();
        controller = new Controller(printer, display, registerCreator);
        testRegID = null;
        testRegIDQ = null;
        currentSale = null;
        originalSysOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        paidAmount = new Amount(1000);
    }

    @AfterEach
    void tearDown() {
        registerCreator = null;
        itemRegistry = null;
        testSale = null;
        discountRegister = null;
        printer = null;
        display = null;
        controller = null;
        testRegID = null;
        testRegIDQ = null;
        currentSale = null;
        paidAmount = null;
        outContent = null;
        System.setOut(originalSysOut);
    }

    @Test
    void testStartSale() {
        controller.startSale();
        try {
            controller.registerItem(ITEM_ID);
        } catch (NullPointerException e) {
            fail("No instance of Sale was created in startSale");
        }
    }

    @Test
    void testRegisterItem() {
        controller.startSale();

        testRegID = controller.registerItem(ITEM_ID);
        assertNotNull(testRegID, "Item registration did not work");
        assertEquals(1, testRegID.getSaleItemsInfo().get(0).getQuantity(),
                "Item did not have quantity 1 when added without quantity.");

        testRegIDQ = controller.registerItem(4, QUANTITY);
        assertNotNull(testRegIDQ, "Item registration did not work");
        assertEquals(QUANTITY, testRegIDQ.getSaleItemsInfo().get(0).getQuantity(),
                "Item did not have the right quantity when added with quantity.");
    }

    @Test
    void testEndSale() {
        controller.startSale();
        currentSale = controller.endSale();
        assertNotNull(currentSale, "End sale did not work");
    }

    @Test
    void testDiscountRequest() {
        discountDTO = discountRegister.getDiscount(CUSTOMER_ID);
        testSale.addItem(ITEM_ID);
        Amount beforeD = testSale.getRunningTotal();
        testSale.applyDiscount(discountDTO);
        Amount afterD = testSale.getRunningTotal();
        assertNotEquals(beforeD, afterD,
                "Discount not applied.");
    }

    @Test
    void testPay() {
        controller.startSale();
        controller.registerItem(ITEM_ID);
        SaleDTO paySaleInfo = controller.endSale();
        paidAmount = new Amount(100);
        LocalDateTime saleTime = LocalDateTime.now();
        controller.pay(paidAmount);
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
    }
}