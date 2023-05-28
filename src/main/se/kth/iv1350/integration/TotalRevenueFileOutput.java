package se.kth.iv1350.integration;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.util.ErrorFileLogHandler;
import se.kth.iv1350.view.TotalRevenue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

// TODO UML:a
// TODO use the System.getProperty?
public class TotalRevenueFileOutput extends TotalRevenue {
    private static final String FILE_SEPARATOR  = System.getProperty("file.separator");
    private static final String FILE_PATH = "data";
    private static final String FILE_NAME = "revenue-log.txt";
    private Locale locale = new Locale("sv", "SE");
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);
    private PrintWriter revenueLogger;
    private ErrorFileLogHandler logger;

    public TotalRevenueFileOutput() throws IOException {
        revenueLogger = new PrintWriter(new FileWriter(FILE_PATH + FILE_SEPARATOR + FILE_NAME, true), true);
        this.logger = ErrorFileLogHandler.getInstance();
    }
    @Override
    protected void doShowTotalRevenue(Amount totalRevenue) {
       revenueLogger.println("%s %s: %s".formatted("Total revenue ", createTime(), totalRevenue));
    }

    @Override
    protected void handleErrors(Exception e) {
        logger.log(e);
    }

    private String createTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }
}
