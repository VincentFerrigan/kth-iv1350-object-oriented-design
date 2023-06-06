package se.kth.iv1350.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    void testBasicFlow() {
        instance.basicFlow();
        String result = outContent.toString(); // TODO ska testas utanför
        assertAll(
                "Verify basic flow",
                () -> assertTrue(result.contains(itemDisplay(5,1))),
                () -> assertTrue(result.contains(itemDisplay(1,1))),
                () -> assertTrue(result.contains(itemDisplay(3,1))),
                () -> assertTrue(result.contains(itemDisplay(9,1))),
                () -> assertTrue(result.contains("Display"), "No display output")
        );
    }

    @Test
    void testAlternativeFlow3AWithCheckedExceptions() {
        instance.alternativeFlow3AWithCheckedExceptions();
        String result = outContent.toString(); // TODO ska testas utanför
        assertAll(
                "Verify basic flow",
                () -> assertTrue(result.contains(itemDisplay(5,1))),
                () -> assertTrue(result.contains(itemDisplay(3,1))),
                () -> assertTrue(result.contains(itemDisplay(4,1))),
                () -> assertTrue(result.contains(itemDisplay(2,1))),
                () -> assertTrue(result.contains("Display"), "No display output")
        );
    }

    @Test
    void testAlternativeFlow3B() {
        instance.alternativeFlow3B();
        String result = outContent.toString(); // TODO ska testas utanför
        assertAll(
                "Verify basic flow",
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
    void testAlternativeFlow3C() {
        instance.alternativeFlow3C();
        String result = outContent.toString(); // TODO ska testas utanför
        assertAll(
                "Verify basic flow",
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
    void testAlternativeFlow9a() {
        instance.alternativeFlow9a();
        String result = outContent.toString(); // TODO ska testas utanför
        assertAll(
                "Verify basic flow",
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
    void testAlternativeFlowWithUnCheckedExceptions() {
        instance.alternativeFlowWithUnCheckedExceptions();
        String result = outContent.toString(); // TODO ska testas utanför
        assertAll(
                "Verify basic flow",
                () -> assertTrue(result.contains(itemDisplay(5,1))),
                () -> assertTrue(result.contains(itemDisplay(5,1+1))),
                () -> assertTrue(result.contains(itemDisplay(2,3))),
                () -> assertTrue(result.contains(runningTotalDisplay(1+1+3))),
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
}