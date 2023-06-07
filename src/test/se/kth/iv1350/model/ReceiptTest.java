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
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

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
    private Locale locale = new Locale("sv", "SE");
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);

    @BeforeEach
    void setUp() {
        originalSysOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        payment = new CashPayment(PAID_AMOUNT);
        try {
            sale = new Sale();
        } catch (PricingFailedException ex) {
            fail("Failed to setUp CashPaymentTest");
            ex.printStackTrace();
        }
        try {
            sale.addItem(ITEM_ID, 1);
        } catch (PricingFailedException | ItemRegistryException | ItemNotFoundInItemRegistryException e) {
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
        Amount itemPriceIncVAT = itemPrice.plus(itemPrice.multiply(0.25));

        assertAll("Verify data in receipt",
                ()-> assertTrue(result.contains(Integer.toString(saleTime.getYear())), "Wrong sale year."),
                () -> assertTrue(result.contains(saleTime.format(formatter)), "Wrong sale date stamp."),
                () -> assertTrue(result.contains(Integer.toString(saleTime.getDayOfMonth())),
                "Wrong sale day."),
                () -> assertTrue(result.contains(Integer.toString(saleTime.getHour())), "Wrong sale hour."),
                () -> assertTrue(result.contains(Integer.toString(saleTime.getMinute())), "Wrong sale minute."),
                () -> assertTrue(result.contains(sale.getTotalPricePaid().toString()), "Wrong total price"),
                () -> assertTrue(result.contains(ITEM_NAME), "Wrong item name"),
                () -> assertTrue(result.contains(payment.getPaidAmt().toString()), "Wrong paid amount name"),
                () -> assertTrue(result.contains(PAID_AMOUNT.minus(sale.getTotalPricePaid()).toString()), "Wrong change"),
                () -> assertTrue(result.contains("VAT"), "Receipt does not include the word \"VAT\""),
                () -> assertTrue(result.contains(itemDisplay(ITEM_NAME,itemPriceIncVAT,1)),"Wrong item display"),
                () -> assertTrue(result.contains(totalCostDisplay(itemPriceIncVAT, 1)), "Wrong total cost displayed"),
                () -> assertTrue(result.contains(vatDisplay(itemPriceIncVAT,1)), "Wrong VAT displayed"),
                () -> assertTrue(result.contains(paidAmountDisplay(PAID_AMOUNT)), "Wrong paid amount displayed"),
                () -> assertTrue(result.contains(changeDisplay(PAID_AMOUNT, itemPriceIncVAT, 1)), "Wrong change displayed")
        );
    }
    private String itemDisplay(String name, Amount unitPrice, int quantity) {
        StringBuilder builder = new StringBuilder();
        builder.append("%-40s%s%n"
                .formatted(name, unitPrice.multiply(quantity)));
        builder.append("(%d * %s)\n".formatted(quantity, unitPrice));
        return builder.toString();
    }
    private String totalCostDisplay(Amount unitPrice, int quantity) {
        return "%-40s%s"
                .formatted("Total Cost:", unitPrice.multiply(quantity));
    }
    private String vatDisplay(Amount unitPrice, int quantity) {
        return "%-40s%s"
                .formatted("Total VAT:", unitPrice.multiply(quantity).multiply(1/1.25*0.25));
    }
    private String paidAmountDisplay(Amount pricePaid) {
        return "%-40s%s"
                .formatted("Paid Amount:", pricePaid);
    }
    private String changeDisplay(Amount pricePaid, Amount unitPrice, int quantity ) {
        return "%-40s%s"
                .formatted("Change:", pricePaid.minus(unitPrice.multiply(quantity)));
    }
}