package se.kth.iv1350.integration;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;
import se.kth.iv1350.util.ErrorFileLogHandler;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

//TODO : UML:as
/**
 * A Singleton that creates an instance representing an external accounting system.
 * This Singleton is a placeholder for a future external accounting system.
 */
public class AccountingSystemFlatFileDB implements AccountingSystem {
//public class AccountingSystemFlatFileDB implements IRegistry<RecordDTO, LocalDateTime> {
    private static volatile AccountingSystemFlatFileDB instance;
    private static final String CSV_DELIMITER = System.getProperty("se.kth.iv1350.database.file.csv_delimiter");
    private final String FILE_PATH_KEY = "se.kth.iv1350.database.file.location";
    private final String FLAT_FILE_DB_NAME_KEY = "se.kth.iv1350.database.file.accounting_db";
    private File flatFileDb;
    private String recordHeader;
    private Map<LocalDateTime, Record> records = new HashMap<>();
    private Locale locale = new Locale("sv", "SE");
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).localizedBy(locale);
    private LocalTime timeOfUpdate;
    private Amount totalRevenue = new Amount(0);
    private Amount totalVATCosts = new Amount(0);
    private Amount totalDiscounts = new Amount(0);
    private ErrorFileLogHandler logger;

    private AccountingSystemFlatFileDB() throws IOException {
        this.logger = ErrorFileLogHandler.getInstance();
        flatFileDb = new File(
                System.getProperty(FILE_PATH_KEY) +
                        System.getProperty("file.separator") +
                        System.getProperty(FLAT_FILE_DB_NAME_KEY));

        addRecordDataFromDb();
    }

    /**
     * @return The only instance of this singleton.
     * @throws IOException
     */
    public static AccountingSystemFlatFileDB getInstance() throws IOException {
        AccountingSystemFlatFileDB result = instance;
        if (result == null) {
            synchronized (AccountingSystemFlatFileDB.class) {
                result = instance;
                if (result == null) {
                    instance = result = new AccountingSystemFlatFileDB();
                }
            }
        }
        return result;
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
                records.put(LocalDateTime.of(LocalDate.now(), timeOfUpdate), new Record(LocalTime.now(), totalRevenue, totalVATCosts, totalDiscounts));
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
     * @throws AccountingSystemException
     */
    @Override
    public void updateRegistry(Sale closedSale){
        timeOfUpdate = LocalTime.now();
        Amount totalPricePaid = closedSale.getTotalPricePaid();
        Amount discount = closedSale.getDiscount();
        Amount vat = closedSale.getTotalVATCosts();

        totalRevenue = totalRevenue.plus(totalPricePaid);
        totalVATCosts = totalVATCosts.plus(vat);
        totalDiscounts = totalDiscounts.plus(discount);
        Record record = (new Record(timeOfUpdate, totalRevenue, totalVATCosts, totalDiscounts));
        records.put(
                LocalDateTime.of(LocalDate.now(), timeOfUpdate),
                record);
        updateDatabase();
    }

    /**
     * Update database by writing to the flat file database
     * @throws AccountingSystemException
     */
    private void updateDatabase() throws AccountingSystemException {
        try (FileWriter fileWriter = new FileWriter(flatFileDb.getPath().replace(".csv", "_" + LocalDate.now() + ".csv"));
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(recordHeader);
            bufferedWriter.newLine();
            for (Record record : records.values()) {
                bufferedWriter.write(record.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (FileNotFoundException ex){
            logger.log(ex);
            throw new AccountingSystemException("Detailed message about database fail");
        } catch (IOException ex){
            logger.log(ex);
            throw new AccountingSystemException("Detailed message about database fail");
        }
    }
    private class Record {
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
            String csv_delimiter = AccountingSystemFlatFileDB.CSV_DELIMITER;

            StringBuilder builder = new StringBuilder();
            builder.append(timeOfUpdate.format(formatter));
            builder.append(csv_delimiter);
            builder.append(totalAmount);
            builder.append(csv_delimiter);
            builder.append(totalVATAmount);
            builder.append(csv_delimiter);
            builder.append(discounts);
            return builder.toString();
        }

    }
}