package se.kth.iv1350.integration;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;
import se.kth.iv1350.util.LogHandler;

import java.io.*;
import java.time.LocalDate;

/**
 * Represents the external accounting system.
 * This class is a placeholder for a future external accounting system.
 */
public class AccountingSystem {
    private final String CSV_DELIMITER = ";";
    private String recordHeader;
    private final String flatFileDb;
    private final String filePath;

    private Amount vat = new Amount(0);
    private Amount totalSale = new Amount(0);
    private Amount discounts = new Amount(0);
    private LogHandler logger;

    /**
     * Creates a new instance of an accounting system.
     * @param filePath the file path to the flat file database
     * @param fileName the file name of the flat file database.
     */
    AccountingSystem(String filePath, String fileName) throws IOException {
        this.filePath = filePath;
        this.flatFileDb = fileName;
        this.logger = new LogHandler();
        addRecord();
    }

    private void addRecord() throws IOException {
        try (FileReader reader = new FileReader(this.filePath + this.flatFileDb);
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
            logger.logException(ex);
            throw ex;
        } catch (IOException ex){
            // TODO ska addItemData loggas här?
            logger.logException(ex);
            throw ex;
        }
    }

    /**
     * Updates the accounting system by adding the specified {@link Sale}.
     * @param closedSale The sale to be added to the accounting system.
     */
    public void updateToAccountingSystem(Sale closedSale){
        if (closedSale.getDiscountAmount() != null) {
            discounts = discounts.plus(closedSale.getDiscountAmount());
        }
        vat = vat.plus(closedSale.getTotalVAT());
        totalSale = totalSale.plus(closedSale.getTotalAmount());
        updateDatabase();
    }

    /**
     * Accounting by creating (and writing to) a flat file database
     */
    private void updateDatabase() {
        try (FileWriter fileWriter = new FileWriter(this.filePath + "accounting_" + LocalDate.now() + ".csv");
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
            logger.logException(ex);
            throw new AccountSystemException("Detailed message about database fail");
        } catch (IOException ex){
            logger.logException(ex);
            throw new AccountSystemException("Detailed message about database fail");
        }
    }
}