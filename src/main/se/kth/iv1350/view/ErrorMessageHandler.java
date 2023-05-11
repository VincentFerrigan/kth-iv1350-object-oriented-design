package se.kth.iv1350.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * This class is responsible for showing error messages to the user.
 */
class ErrorMessageHandler {
    private Locale locale = new Locale("sv", "SE");
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);

    /**
     * Displays the specified error message.
     * @param msg The error message.
     */
    void showErrorMessage(String msg) {
        StringBuilder errorMsgBuilder = new StringBuilder();
        errorMsgBuilder.append(createTime());
        errorMsgBuilder.append(", ERROR: ");
        errorMsgBuilder.append(msg);

        System.out.println(errorMsgBuilder);
    }

    private String createTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }
}
