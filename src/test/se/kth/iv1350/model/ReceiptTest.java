package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.ItemDTO;
import se.kth.iv1350.integration.Printer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {
    private Receipt instance;
    private ByteArrayOutputStream outContent;
    private PrintStream originalSysOut;
    private CashPayment payment;
    private final Amount PAID_AMOUNT = new Amount(500);
    private Sale sale;
    private ItemDTO itemInfo;
    private Amount itemPrice;
    private Printer printer = new Printer();

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
        itemInfo = new ItemDTO(1, "Product name", "Product Description",
                new Amount(100), new VAT(1));
        itemPrice = itemInfo.getUnitPrice();
        try {
            sale.addItem(itemInfo, 1);
        } catch (OperationFailedException e) {
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
        itemInfo = null;
        itemPrice = null;
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
        assertTrue(result.contains(itemInfo.getName()), "Wrong item name");
        assertTrue(result.contains(payment.getPaidAmt().toString()), "Wrong paid amount name");
        assertTrue(result.contains(PAID_AMOUNT.minus(sale.getTotalPricePaid()).toString()), "Wrong change");
        assertTrue(result.contains("VAT"), "Receipt does not include the word \"VAT\"");
    }
}