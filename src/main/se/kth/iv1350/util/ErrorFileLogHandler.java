package se.kth.iv1350.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * A Singleton that creates an instance responsible for
 * logging errors to a specific file.
 */
public class ErrorFileLogHandler implements Logger {
    private static volatile ErrorFileLogHandler instance;
    private static final String FILE_SEPARATOR  = System.getProperty("file.separator");
    private final String FILE_PATH = System.getProperty("se.kth.iv1350.log.file.location");
    private static final String LOG_FILE_NAME = System.getProperty("se.kth.iv1350.log.file.error_log");
    private static final String LINE_SEPARATOR  = System.getProperty("line.separator");
    private PrintWriter logFile;

    private ErrorFileLogHandler() throws IOException {
        logFile = new PrintWriter(new FileWriter(FILE_PATH + FILE_SEPARATOR + LOG_FILE_NAME, true), true);
    }

    /**
     * @return The only instance of this singleton.
     * @throws IOException
     */
    public static ErrorFileLogHandler getInstance() throws IOException{
        ErrorFileLogHandler result = instance;
        if (result == null) {
            synchronized (ErrorFileLogHandler.class) {
                result = instance;
                if (result == null) {
                    instance = result = new ErrorFileLogHandler();
                }
            }
        }
        return result;
    }

    /**
     * Writes a log entry describing a thrown exception.
     * @param ex The exception that shall be logged.
     */
    @Override
    public void log(Object ex) {
        Exception exception = (Exception) ex;
        StringBuilder logMsgBuilder = new StringBuilder();
        logMsgBuilder.append(createTime());
        logMsgBuilder.append(", Exception was thrown: ");
        logMsgBuilder.append(exception.getMessage());
        logFile.println(logMsgBuilder);
        exception.printStackTrace(logFile);
        logFile.println(LINE_SEPARATOR);
    }

    private String createTime() {
        Locale locale = new Locale("sv", "SE");
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }
}