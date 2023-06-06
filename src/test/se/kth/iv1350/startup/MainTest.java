package se.kth.iv1350.startup;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.POSTestSuperClass;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO Lägg typ upp en läsning av flat db filen och skapa en hashmap (id, array) som du kan använda dig av
class MainTest extends POSTestSuperClass {
    private Main instance;
    private ByteArrayOutputStream outContent;
    private PrintStream originalSysOut;

    @BeforeEach
    void setUp() {
        instance = new Main();
        originalSysOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        instance = null;
        outContent = null;
        System.setOut(originalSysOut);
    }

    @Test
    @DisplayName("Start up with samples")
    void testMain() {
        instance.main(new String[0]);
        String result = outContent.toString();
        assertAll(
                "Verify all displays",
                () -> assertTrue(result.contains("ERROR"), "Lack of error message"),
                () -> assertTrue(result.contains("Display"), "No display output"),
                () -> assertTrue(result.contains("End of Sale"), "No end of sale output"),
                () -> assertTrue(result.contains("Receipt"), "No receipt output")
            );
    }
}