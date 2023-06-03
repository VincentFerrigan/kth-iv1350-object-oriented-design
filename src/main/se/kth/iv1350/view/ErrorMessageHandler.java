package se.kth.iv1350.view;

import se.kth.iv1350.util.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * A Singleton that creates an instance responsible for
 * showing error messages to the user.
 * It's a dummy implementation that prints to
 * <code>System.out</code>
 * It is implemented as a singleton
 */
class ErrorMessageHandler implements Logger {
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
        errorMsgBuilder.append("%n".formatted());
        errorMsgBuilder.append(createTime());
        errorMsgBuilder.append(", ERROR: ");
        errorMsgBuilder.append("%n".formatted());
        errorMsgBuilder.append(errorMessage);
        errorMsgBuilder.append("%n".formatted());

        System.out.println(errorMsgBuilder);
    }

    private String createTime() {
        Locale locale = new Locale("sv", "SE");
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }
}
