package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.model.Amount;
import src.se.kth.iv1350.model.Sale;

import java.io.*;
import java.time.LocalDate;

//TODO flat file dB.
public class AccountingSystem {
    private final String flatFileDb;
    private final String filePath;

    private Amount vat = new Amount(0);
    private Amount totalSale = new Amount(0);

    // TODO, based on InventorySystems constructor.
    AccountingSystem(String filePath, String fileName) {
        this.filePath = filePath;
        this.flatFileDb = fileName;
        String splitCsvBy = ";";
    }
    public void updateToAccountingSystem(Sale closedSale){
        vat = vat.plus(closedSale.getTotalVATAmount());
        totalSale = totalSale.plus(closedSale.getRunningTotal());
        updateDatabase();
    }

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
