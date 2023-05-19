package se.kth.iv1350.view;

import se.kth.iv1350.util.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

// TODO UML:a och skriva JavaDocs
/**
 * A Singleton that creates an instance responsible for
 * showing error messages to the user.
 * It's a dummy implementation that prints to
 * <code>System.out</code>
 * It is implemented as a singleton
 */
class ErrorMessageHandler implements Logger {
    private Locale locale = new Locale("sv", "SE");
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);
    private static final ErrorMessageHandler INSTANCE = new ErrorMessageHandler();
    private ErrorMessageHandler() {}

    /**
     * @return The only instance of this singleton.
     */
    public static ErrorMessageHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Displays the specified error message.
     * Prints specified string to <code>System.out</code>
     * @param message The error message that will be printed to
     * <code>System.out</code>.
     */
    @Override
    public void log(Object message) {
        String errorMessage = (String) message;
        StringBuilder errorMsgBuilder = new StringBuilder();
        errorMsgBuilder.append(createTime());
        errorMsgBuilder.append(", ERROR: ");
        errorMsgBuilder.append(errorMessage);

        System.out.println(errorMsgBuilder);
    }

    private String createTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }
}
