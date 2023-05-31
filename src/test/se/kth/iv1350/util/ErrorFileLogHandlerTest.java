package se.kth.iv1350.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ErrorFileLogHandlerTest {
    ErrorFileLogHandler instance;
    @BeforeEach
    void setUp() {
        try {
            instance = ErrorFileLogHandler.getInstance();
        } catch (IOException ex) {
            // Not part of the test
            fail("No exception should be thrown, " +
                    "unable to set up the ControllerTest. " +
                    "%s".formatted(ex.getMessage()));
        }
    }

    @AfterEach
    void tearDown() {
        instance = null;
    }

    @Test
    void testLog() {
       //TODO lägg till att den ska jämföra error-log filen med förväntad, efter något slags fel.
    }
}