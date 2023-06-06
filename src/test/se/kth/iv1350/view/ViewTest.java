package se.kth.iv1350.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.POSTestSuperClass;
import se.kth.iv1350.controller.Controller;
import se.kth.iv1350.integration.Printer;
import se.kth.iv1350.model.Amount;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

// TODO Lägg typ upp en läsning av flat db filen och skapa en hashmap (id, array) som du kan använda dig av
class ViewTest extends POSTestSuperClass {
    private View instance;
    private ByteArrayOutputStream outContent;
    private PrintStream originalSysOut;
    private static final String TEST_NAME = "test name";
    private static final Amount TEST_UNIT_PRICE = new Amount(10);
    @BeforeEach
    void setUp() {
        try {
            instance = new View(new Controller(new Printer()) );
            originalSysOut = System.out;
            outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
        } catch (IOException ex) {
            // Not part of the test
            fail("No exception should be thrown, unable to set up the ViewTest. "
                    + "%s".formatted(ex.getMessage()));
        }
    }

    @AfterEach
    void tearDown() {
        instance = null;
        outContent = null;
        System.setOut(originalSysOut);
    }
    @Test
    @DisplayName("Basic flow")
    void testBasicFlow() {
        instance.basicFlow();
        String result = outContent.toString(); // TODO ska testas utanför
        int nbrOfItems = 4;
        double paidAmount = 200;
        assertAll(
                "Verify basic flow",
                () -> assertTrue(result.contains(itemDisplay(5,1))),
                () -> assertTrue(result.contains(itemDisplay(1,1))),
                () -> assertTrue(result.contains(itemDisplay(3,1))),
                () -> assertTrue(result.contains(itemDisplay(9,1))),
                () -> assertTrue(result.contains(runningTotalDisplay(1)), "Wrong running total or missing"),
                () -> assertTrue(result.contains(runningVATDisplay(1)), "Wrong running VAT displayed"),
                () -> assertTrue(result.contains(totalCostDisplay(nbrOfItems)), "Wrong total cost displayed"),
                () -> assertTrue(result.contains(vatDisplay(nbrOfItems)), "Wrong VAT displayed"),
                () -> assertTrue(result.contains(paidAmountDisplay(paidAmount)), "Wrong paid amount displayed"),
                () -> assertTrue(result.contains(changeDisplay(paidAmount, nbrOfItems)), "Wrong change displayed"),
                () -> assertTrue(result.contains("Display"), "No display output"),
                () -> assertTrue(result.contains("End of Sale"), "No end of sale output"),
                () -> assertTrue(result.contains("Receipt"), "No receipt output")
        );
    }

    @Test
    @DisplayName("Alternative flow 3A with checked exceptions")
    void testAlternativeFlow3AWithCheckedExceptions() {
        instance.alternativeFlow3AWithCheckedExceptions();
        String result = outContent.toString();
        assertAll(
                "Verify alternative flow 3A output",
                () -> assertTrue(result.contains("ERROR"), "Lack of error message"),
                () -> assertTrue(result.contains(itemDisplay(5,1))),
                () -> assertTrue(result.contains(itemDisplay(3,1))),
                () -> assertTrue(result.contains(itemDisplay(4,1))),
                () -> assertTrue(result.contains(itemDisplay(2,1))),
                () -> assertTrue(result.contains("Display"), "No display output"),
                () -> assertTrue(result.contains("End of Sale"), "No end of sale output"),
                () -> assertTrue(result.contains("Display"), "No display output")
        );
    }

    @Test
    @DisplayName("Alternative flow 3B")
    void testAlternativeFlow3B() {
        instance.alternativeFlow3B();
        String result = outContent.toString(); // TODO ska testas utanför
        assertAll(
                "Verify Alternative flow 3B output",
                () -> assertTrue(result.contains(itemDisplay(3,1))),
                () -> assertTrue(result.contains(itemDisplay(3,1+1))),
                () -> assertTrue(result.contains(itemDisplay(1,1))),
                () -> assertTrue(result.contains(itemDisplay(1,1+1))),
                () -> assertTrue(result.contains(itemDisplay(9,1))),
                () -> assertTrue(result.contains(itemDisplay(9,1+1))),
                () -> assertTrue(result.contains(itemDisplay(9,1+1+1))),
                () -> assertTrue(result.contains(itemDisplay(9,1+1+1+1))),
                () -> assertTrue(result.contains(itemDisplay(9,1+1+1+1+1))),
                () -> assertTrue(result.contains(runningTotalDisplay(9))),
                () -> assertTrue(result.contains("Display"), "No display output"),
                () -> assertTrue(result.contains("End of Sale"), "No end of sale output"),
                () -> assertTrue(result.contains("Receipt"), "No receipt output")
        );
    }

    @Test
    @DisplayName("Alternative flow 3C")
    void testAlternativeFlow3C() {
        instance.alternativeFlow3C();
        String result = outContent.toString(); // TODO ska testas utanför
        assertAll(
                "Verify Alternative flow 3C output",
                () -> assertTrue(result.contains(itemDisplay(2,2))),
                () -> assertTrue(result.contains(itemDisplay(8,2))),
                () -> assertTrue(result.contains(itemDisplay(7,4))),
                () -> assertTrue(result.contains(itemDisplay(10,4))),
                () -> assertTrue(result.contains(runningTotalDisplay(2+2+4+4))),
                () -> assertTrue(result.contains("Display"), "No display output"),
                () -> assertTrue(result.contains("End of Sale"), "No end of sale output"),
                () -> assertTrue(result.contains("Receipt"), "No receipt output")
        );
    }

    @Test
    @DisplayName("Alternative flow 9A")
    void testAlternativeFlow9a() {
        instance.alternativeFlow9a();
        String result = outContent.toString();
        assertAll(
                "Verify Alternative flow 9A output",
                () -> assertTrue(result.contains(itemDisplay(8,2))),
                () -> assertTrue(result.contains(itemDisplay(8,4+2))),
                () -> assertTrue(result.contains(itemDisplay(10,4))),
                () -> assertTrue(result.contains(runningTotalDisplay(4+2+4))),
                () -> assertTrue(result.contains("Display"), "No display output"),
                () -> assertTrue(result.contains("End of Sale"), "No end of sale output"),
                () -> assertTrue(result.contains("Receipt"), "No receipt output")
        );
    }

    @Test
    @DisplayName("Alternative flow - Unchecked exception, data failure")
    void testAlternativeFlowWithUnCheckedExceptions() {
        instance.alternativeFlowWithUnCheckedExceptions();
        String result = outContent.toString(); // TODO ska testas utanför
        int nbrOfItems = 1+1+3;
        double paidAmount = 500;
        assertAll(
                "Verify Alternative flow output",
                () -> assertTrue(result.contains("ERROR"), "Lack of error message"),
                () -> assertTrue(result.contains(itemDisplay(5,1)), "Wrong first item display"),
                () -> assertTrue(result.contains(itemDisplay(5,1+1)), "Wrong second item display"),
                () -> assertTrue(result.contains(itemDisplay(2,3)),"Wrong third item display"),
                () -> assertTrue(result.contains(runningTotalDisplay(1)), "Wrong running total or missing"),
                () -> assertTrue(result.contains(runningVATDisplay(1)), "Wrong running VAT displayed"),
                () -> assertTrue(result.contains(runningTotalDisplay(2)), "Wrong running total or missing"),
                () -> assertTrue(result.contains(runningVATDisplay(2)), "Wrong running VAT displayed"),
                () -> assertTrue(result.contains(runningTotalDisplay(nbrOfItems)), "Wrong running total or missing"),
                () -> assertTrue(result.contains(runningVATDisplay(nbrOfItems)), "Wrong running VAT displayed"),
                () -> assertTrue(result.contains(totalCostDisplay(nbrOfItems)), "Wrong total cost displayed"),
                () -> assertTrue(result.contains(vatDisplay(nbrOfItems)), "Wrong VAT displayed"),
                () -> assertTrue(result.contains(paidAmountDisplay(paidAmount)), "Wrong paid amount displayed"),
                () -> assertTrue(result.contains(changeDisplay(paidAmount, nbrOfItems)), "Wrong change displayed"),
                () -> assertTrue(result.contains("Display"), "No display output"),
                () -> assertTrue(result.contains("End of Sale"), "No end of sale output"),
                () -> assertTrue(result.contains("Receipt"), "No receipt output")
        );
    }
    private String itemDisplay(int id, int quantity) {
        StringBuilder builder = new StringBuilder();
        builder.append("%-40s%s%n"
                .formatted(TEST_NAME+id, TEST_UNIT_PRICE.multiply(quantity)));
        builder.append("(%d * %s)\n".formatted(quantity, TEST_UNIT_PRICE));
        return builder.toString();
    }
    private String runningTotalDisplay(int quantity) {
        return "%-40s%s"
                .formatted("Running total:", TEST_UNIT_PRICE.multiply(quantity));
    }
    private String runningVATDisplay(int quantity) {
        return "%-40s%s"
                .formatted("Including VAT:", TEST_UNIT_PRICE.multiply(quantity).multiply(1/1.25*0.25));
    }
    private String totalCostDisplay(int quantity) {
        return "%-40s%s"
                .formatted("Total Cost:", TEST_UNIT_PRICE.multiply(quantity));
    }
    private String vatDisplay(int quantity) {
        return "%-40s%s"
                .formatted("Total VAT:", TEST_UNIT_PRICE.multiply(quantity).multiply(1/1.25*0.25));
    }
    private String paidAmountDisplay(double pricePaid) {
        return "%-40s%s"
                .formatted("Paid Amount:", new Amount(pricePaid));
    }
    private String changeDisplay(double pricePaid, int quantity ) {
        return "%-40s%s"
                .formatted("Change:", new Amount(pricePaid).minus(TEST_UNIT_PRICE.multiply(quantity)));
    }
}