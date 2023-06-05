package se.kth.iv1350.integration;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.util.ErrorFileLogHandler;
import se.kth.iv1350.model.TotalRevenue;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * A Singleton that creates an instance responsible for
 * logging the total revenue to a specific file, each time it is updated.
 */
public class TotalRevenueFileOutput extends TotalRevenue {
    private static volatile TotalRevenueFileOutput instance;
    private static final String FILE_SEPARATOR  = System.getProperty("file.separator");
    private final String FILE_PATH = System.getProperty("se.kth.iv1350.log.file.location");
    private static final String LOG_FILE_NAME = System.getProperty("se.kth.iv1350.log.file.revenue_log");
    private PrintWriter revenueLogger;
    private final ErrorFileLogHandler logger;

    private TotalRevenueFileOutput() throws IOException {
        revenueLogger = new PrintWriter(new FileWriter(FILE_PATH + FILE_SEPARATOR + LOG_FILE_NAME, true), true);
        this.logger = ErrorFileLogHandler.getInstance();
    }
    /**
     * @return The only instance of this singleton.
     * @throws IOException
     */
    public static TotalRevenueFileOutput getInstance() throws IOException{
        TotalRevenueFileOutput result = instance;
        if (result == null) {
            synchronized (TotalRevenueFileOutput.class) {
                result = instance;
                if (result == null) {
                    instance = result = new TotalRevenueFileOutput();
                }
            }
        }
        return result;
    }

    /**
     * Creates a string builder with total revenue, that is to be written to a file.
     * @param totalRevenue the new total revenue
     */
    @Override
    protected void doShowTotalRevenue(Amount totalRevenue) {
       revenueLogger.println("%s %s: %s".formatted("Total revenue ", createTime(), totalRevenue));
    }

    /**
     * Handles errors that are thrown.
     * @param ex the exception thrown
     */
    @Override
    protected void handleErrors(Exception ex) {
        logger.log(ex);
    }

    private String createTime() {
        Locale locale = new Locale("sv", "SE");
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }
}