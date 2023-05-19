package se.kth.iv1350.integration;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;
import se.kth.iv1350.util.ErrorFileLogHandler;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//TODO : UML:as
/**
 * A Singleton that creates an instance representing an external accounting system.
 * This Singleton is a placeholder for a future external accounting system.
 */
public class AccountingSystem {
    private static volatile AccountingSystem instance;
    private static final String FILE_SEPARATOR  = System.getProperty("file.separator");
    private static final String FILE_PATH = "src/main/se/kth/iv1350/data".replace(",", FILE_SEPARATOR);
    private static final String FLAT_FILE_DB = "accounting.csv";
    private final String CSV_DELIMITER = ";";
    private String recordHeader;
    private ErrorFileLogHandler logger;

    // Osäker på nedanstående attribute. Beror på vilken lösning jag väljer.
    private Locale locale = new Locale("sv", "SE");
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);
    private Amount vat = new Amount(0);
    private Amount totalSale = new Amount(0);
    // Kommer att bytas ut vid implementation av discount strategy
    private Amount discounts = new Amount(0);

    private AccountingSystem() throws IOException {
        this.logger = ErrorFileLogHandler.getInstance();
        addRecord();
    }

    /**
     * @return The only instance of this singleton.
     * @throws IOException
     */
    public static AccountingSystem getInstance() throws IOException {
        AccountingSystem result = instance;
        if (result == null) {
            synchronized (AccountingSystem.class) {
                result = instance;
                if (result == null) {
                    instance = result = new AccountingSystem();
                }
            }
        }
        return result;
    }

    // TODO: EVENTUELL. Beror på vilken lösning jag väljer.
    public Map getData(String FILE_TO_LOAD) throws IOException {
        Map<LocalDate, Account> accountingTable = new HashMap<>();
        try (FileReader reader = new FileReader(FILE_PATH + FILE_SEPARATOR + FLAT_FILE_DB);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = "";
            recordHeader = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] splitArray = line.split(CSV_DELIMITER);
                accountingTable.put(
                        LocalDate.parse(splitArray[0], formatter),
                        new Account(
                                LocalTime.parse(splitArray[1], formatter),
                                Double.parseDouble(splitArray[2]),
                                Double.parseDouble(splitArray[3]),
                                Double.parseDouble(splitArray[4])));
            }
        } catch (FileNotFoundException ex){
            // TODO Kan man kasta bara ex? Kommer den då skickas som en IOException?
            logger.log(ex);
            throw ex;
        } catch (IOException ex){
            // TODO ska addItemData loggas här?
            logger.log(ex);
            throw ex;
        }
        return accountingTable;
    }

    private static class Account {
        private LocalTime timeOfUpdate;
        private Amount totalAmount;
        private Amount totalVATAmount;
        private Amount discounts;

        public Account(LocalTime timeOfUpdate, double totalAmount, double totalVATAmount, double discounts) {
            this.timeOfUpdate = timeOfUpdate;
            this.totalAmount = new Amount(totalAmount);
            this.totalVATAmount = new Amount(totalVATAmount);
            this.discounts = new Amount(discounts);
        }
    }

    private void addRecord() throws IOException {
        try (FileReader reader = new FileReader(FILE_PATH + FILE_SEPARATOR + FLAT_FILE_DB);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = "";
            recordHeader = bufferedReader.readLine();
            if ((line = bufferedReader.readLine()) != null) {
                String[] splitArray = line.split(CSV_DELIMITER);
                totalSale = new Amount(Double.parseDouble(splitArray[0]));
                vat = new Amount(Double.parseDouble(splitArray[1]));
                discounts = new Amount(Double.parseDouble(splitArray[2]));
            }
        } catch (FileNotFoundException ex){
            // TODO Kan man kasta bara ex? Kommer den då skickas som en IOException?
            logger.log(ex);
            throw ex;
        } catch (IOException ex){
            // TODO ska addItemData loggas här?
            logger.log(ex);
            throw ex;
        }
    }

    /**
     * Updates the accounting system by adding the specified {@link Sale}.
     * @param closedSale The sale to be added to the accounting system.
     */
    public void updateToAccountingSystem(Sale closedSale){
//        if (!closedSale.getDiscountAmount().equals(new Amount(0))) {
//            discounts = discounts.plus(closedSale.getDiscountAmount());
//        }
        vat = vat.plus(closedSale.getTotalVATCosts());
        totalSale = totalSale.plus(closedSale.getTotalPrice());
        updateDatabase();
    }

    /**
     * Accounting by creating (and writing to) a flat file database
     */
    private void updateDatabase() {
        try (FileWriter fileWriter = new FileWriter(FILE_PATH + FILE_SEPARATOR + "accounting_" + LocalDate.now() + ".csv");
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(recordHeader);
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(totalSale));
            bufferedWriter.write(CSV_DELIMITER);
            bufferedWriter.write(String.valueOf(vat));
            bufferedWriter.write(CSV_DELIMITER);
            bufferedWriter.write(String.valueOf(discounts));
            bufferedWriter.flush();
        } catch (FileNotFoundException ex){
            logger.log(ex);
            throw new AccountSystemException("Detailed message about database fail");
        } catch (IOException ex){
            logger.log(ex);
            throw new AccountSystemException("Detailed message about database fail");
        }
    }
}