package se.kth.iv1350.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.model.*;
import se.kth.iv1350.view.View;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PrinterTest {
    private Printer instance;
    private ByteArrayOutputStream outContent;
    private PrintStream originalSysOut;
    private Sale sale;

    @BeforeEach
    void setUp() {
        instance = new Printer();
        originalSysOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        try {
            sale = new Sale();
        } catch (PricingFailedException ex) {
            // Not part of the test
            fail("No exception should be thrown, unable to set up the PrinterTest. "
                    + "%s".formatted(ex.getMessage()));
        }
    }
    @AfterEach
    void tearDown() {
        instance = null;
        outContent = null;
        System.setOut(originalSysOut);
    }
    @Disabled
    @Test
    void printReceipt() {
        Amount paidAmount = new Amount(100);

        try {
            sale.addItem(1, 1);
        } catch (ItemNotFoundInItemRegistryException | PricingFailedException | ItemRegistryException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
        }
        sale.pay(new CashPayment(paidAmount));
        LocalDateTime saleTime = LocalDateTime.now();
//        Receipt receipt = new Receipt(sale); // CAN NOT! PACKAGE PRIVATE
//        instance.printReceipt(receipt);
        String result = outContent.toString();

        assertAll(
                "Something here",
                () -> assertTrue(result.contains("Receipt")),
                ()-> assertTrue(result.contains(Integer.toString(saleTime.getYear())), "Wrong sale year."),
                () -> assertTrue(result.contains(Integer.toString(saleTime.getMonthValue())), "Wrong sale month."),
                () -> assertTrue(result.contains(Integer.toString(saleTime.getDayOfMonth())),
                        "Wrong sale day."),
                () -> assertTrue(result.contains(Integer.toString(saleTime.getHour())), "Wrong sale hour."),
                () -> assertTrue(result.contains(Integer.toString(saleTime.getMinute())), "Wrong sale minute.")
        );

    }

}