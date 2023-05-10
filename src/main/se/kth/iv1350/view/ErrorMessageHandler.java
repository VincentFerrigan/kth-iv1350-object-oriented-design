package se.kth.iv1350.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * This class creates and shows error messages to the user.
 */
public class ErrorMessageHandler {
    private Locale locale = new Locale("sv", "SE");
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);

    /**
     * Creates and displays the specified error message.
     * @param msg The error message to be displayed.
     */
    public void showErrorMessage(String msg) {
        System.out.println("%s, ERROR: %s".formatted(LocalDateTime.now().format(formatter), msg));
    }
}
