package se.kth.iv1350.integration;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;
import se.kth.iv1350.util.ErrorFileLogHandler;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
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
    private static final String CSV_DELIMITER = System.getProperty("se.kth.iv1350.database.file.csv_delimiter");
    private final String FILE_PATH = System.getProperty("se.kth.iv1350.database.file.location");
    private final String FILE_SEPARATOR  = System.getProperty("file.separator");
    private final String FLAT_FILE_DB_NAME = System.getProperty("se.kth.iv1350.database.file.accounting_db");
    private File flatFileDb;
    private String recordHeader;
    private ErrorFileLogHandler logger;
    private ArrayList<Record> records;
    private Locale locale = new Locale("sv", "SE");
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).localizedBy(locale);
    private LocalTime timeOfUpdate;
    private Amount totalRevenue = new Amount(0);
    private Amount totalVATCosts = new Amount(0);
    private Amount totalDiscounts = new Amount(0);

    private AccountingSystem() throws IOException {
        this.logger = ErrorFileLogHandler.getInstance();
        flatFileDb = new File(FILE_PATH+FILE_SEPARATOR+FLAT_FILE_DB_NAME);
        records = new ArrayList<>();

        addRecordDataFromDb();
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
        Map<LocalDate, Record> accountingTable = new HashMap<>();
        try (FileReader reader = new FileReader(flatFileDb);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = "";
            recordHeader = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] splitArray = line.split(CSV_DELIMITER);
                accountingTable.put(
                        LocalDate.parse(splitArray[0], formatter),
                        new Record(
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


    private void addRecordDataFromDb() throws IOException {
        try (FileReader reader = new FileReader(flatFileDb);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = "";
            recordHeader = bufferedReader.readLine();
            if ((line = bufferedReader.readLine()) != null) {
                String[] splitArray = line.split(CSV_DELIMITER);
                timeOfUpdate = LocalTime.parse(splitArray[0]);
                totalRevenue = new Amount(Double.parseDouble(splitArray[1]));
                totalVATCosts = new Amount(Double.parseDouble(splitArray[2]));
                totalDiscounts = new Amount(Double.parseDouble(splitArray[3]));
                records.add(new Record(LocalTime.now(), totalRevenue, totalVATCosts, totalDiscounts));
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
        Amount totalPricePreDiscount = closedSale.calculateRunningTotal();
        Amount totalPricePaid = closedSale.getTotalPricePaid();
        Amount discount = totalPricePreDiscount.minus(totalPricePaid);
        Amount vat = closedSale.getTotalVATCosts();

        totalRevenue = totalRevenue.plus(totalPricePaid);
        totalVATCosts = totalVATCosts.plus(vat);
        totalDiscounts = totalDiscounts.plus(discount);

        Record record = new Record(LocalTime.now(), totalRevenue, totalVATCosts, totalDiscounts);
        records.add(record);
        updateDatabase();
    }

    /**
     * Update database by writing to the flat file database
     */
    private void updateDatabase() {
        try (FileWriter fileWriter = new FileWriter(flatFileDb.getPath().replace(".csv", "_" + LocalDate.now() + ".csv"));
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(recordHeader);
            bufferedWriter.newLine();
            for (Record record : records) {
                bufferedWriter.write(record.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (FileNotFoundException ex){
            logger.log(ex);
            throw new ItemRegistryException("Detailed message about database fail");
        } catch (IOException ex){
            logger.log(ex);
            throw new ItemRegistryException("Detailed message about database fail");
        }
    }
    private static class Record {
        private LocalTime timeOfUpdate;
        private Amount totalAmount;
        private Amount totalVATAmount;
        private Amount discounts;

        public Record(LocalTime timeOfUpdate, double totalAmount, double totalVATAmount, double discounts) {
            this.timeOfUpdate = timeOfUpdate;
            this.totalAmount = new Amount(totalAmount);
            this.totalVATAmount = new Amount(totalVATAmount);
            this.discounts = new Amount(discounts);
        }

        public Record(LocalTime timeOfUpdate, Amount totalAmount, Amount totalVATAmount, Amount discounts) {
            this.timeOfUpdate = timeOfUpdate;
            this.totalAmount = new Amount(totalAmount);
            this.totalVATAmount = new Amount(totalVATAmount);
            this.discounts = new Amount(discounts);
        }
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(timeOfUpdate);
            builder.append(AccountingSystem.CSV_DELIMITER);
            builder.append(totalAmount);
            builder.append(AccountingSystem.CSV_DELIMITER);
            builder.append(totalVATAmount);
            builder.append(AccountingSystem.CSV_DELIMITER);
            builder.append(discounts);
            return builder.toString();
        }

    }
}