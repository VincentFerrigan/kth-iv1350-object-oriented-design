package se.kth.iv1350.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ErrorMessageHandlerTest {
    private ErrorMessageHandler errorMessageHandler;
    private Locale locale = new Locale("sv", "SE");
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputArr = new ByteArrayOutputStream();
    private String output;
    private final String testError = "This is the exception msg.";

    @BeforeEach
    void setUp() {
        errorMessageHandler = new ErrorMessageHandler();
        System.setOut(new PrintStream(outputArr));
        output = outputArr.toString();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        output = null;
    }

    @Test
    void testPrintErrorMessage() {
        StringBuilder testMessage = new StringBuilder();
        testMessage.append(LocalDateTime.now().format(formatter));
        testMessage.append(", ERROR: ");
        testMessage.append(testError);
        errorMessageHandler.showErrorMessage(testError);
        assertEquals(testMessage.toString() +"\n", outputArr.toString());
    }
}