package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.POSTestSuperClass;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest extends POSTestSuperClass {
    private Receipt instance;
    private ByteArrayOutputStream outContent;
    private PrintStream originalSysOut;
    private CashPayment payment;
    private final Amount PAID_AMOUNT = new Amount(500);
    private Sale sale;
    private Amount itemPrice = new Amount(8);
    private Printer printer = new Printer();
    private final int ITEM_ID = 0;
    private final String ITEM_NAME = "test name";

    @BeforeEach
    void setUp() {
        originalSysOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        payment = new CashPayment(PAID_AMOUNT);
        try {
            sale = new Sale();
        } catch (OperationFailedException ex) {
            fail("Failed to setUp CashPaymentTest");
            ex.printStackTrace();
        }
        try {
            sale.addItem(ITEM_ID, 1);
        } catch (OperationFailedException | ItemRegistryException | ItemNotFoundInItemRegistryException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
            throw new RuntimeException(e);
        }
        sale.pay(payment);
    }

    @AfterEach
    void tearDown() {
        instance = null;
        outContent = null;
        System.setOut(originalSysOut);
        sale = null;
        payment = null;
    }

    @Test
    void testPrintReceipt() {
        instance = new Receipt(sale);
        printer.printReceipt(instance);
        assertTrue(outContent.toString().contains("Receipt"),
                "Receipt not printed");
    }

    @Test
    void testToString() {
        instance = new Receipt(sale);
        LocalDateTime saleTime = LocalDateTime.now();
        String result = instance.toString();
        assertTrue(result.contains(Integer.toString(saleTime.getYear())), "Wrong sale year.");
        assertTrue(result.contains(Integer.toString(saleTime.getMonthValue())),
                "Wrong rental month.");
        assertTrue(result.contains(Integer.toString(saleTime.getDayOfMonth())),
                "Wrong rental day.");
        assertTrue(result.contains(Integer.toString(saleTime.getHour())), "Wrong sale hour.");
        assertTrue(result.contains(Integer.toString(saleTime.getMinute())), "Wrong sale minute.");
        assertTrue(result.contains(sale.getTotalPricePaid().toString()), "Wrong total price");
        assertTrue(result.contains(ITEM_NAME), "Wrong item name");
        assertTrue(result.contains(payment.getPaidAmt().toString()), "Wrong paid amount name");
        assertTrue(result.contains(PAID_AMOUNT.minus(sale.getTotalPricePaid()).toString()), "Wrong change");
        assertTrue(result.contains("VAT"), "Receipt does not include the word \"VAT\"");
    }
}