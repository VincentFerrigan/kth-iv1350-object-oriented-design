package se.kth.iv1350.integration;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.Sale;

import java.io.*;
import java.time.LocalDate;

/**
 * Represents the external accounting system.
 * This class is a placeholder for a future external accounting system.
 */
public class AccountingSystem {
    private final String flatFileDb;
    private final String filePath;

    private Amount vat = new Amount(0);
    private Amount totalSale = new Amount(0);

    /**
     * Creates a new instance of an accounting system.
     * @param filePath the file path to the flat file database
     * @param fileName the file name of the flat file database.
     */
    AccountingSystem(String filePath, String fileName) {
        this.filePath = filePath;
        this.flatFileDb = fileName;
        String splitCsvBy = ";";
    }

    /**
     * Updates the accounting system by adding the specified {@link Sale}.
     * @param closedSale The sale to be added to the accounting system.
     */
    public void updateToAccountingSystem(Sale closedSale){
        vat = vat.plus(closedSale.getTotalVATAmount());
        totalSale = totalSale.plus(closedSale.getRunningTotal());
        updateDatabase();
    }

    /**
     * Accounting by creating (and writing to) a flat file database
     */
    private void updateDatabase() {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(this.filePath + "accounting" + LocalDate.now() + ".csv");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(String.valueOf(totalSale));
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(vat));
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (FileNotFoundException e){
            System.out.println("The file was not found");
            e.printStackTrace(); //Skriver ut vart felet var någonstans.

        } catch (IOException e){
            System.out.println("IOE exception");
            e.printStackTrace();
        }
    }
}
