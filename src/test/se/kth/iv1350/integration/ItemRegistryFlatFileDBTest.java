package se.kth.iv1350.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.POSTestSuperClass;
import se.kth.iv1350.integration.dto.ItemDTO;
import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.CashPayment;
import se.kth.iv1350.model.PricingFailedException;
import se.kth.iv1350.model.Sale;

import java.io.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemRegistryFlatFileDBTest extends POSTestSuperClass {
    ItemRegistryFlatFileDB instance;
    private final String FILE_PATH_KEY = "se.kth.iv1350.database.file.location";
    private final String FLAT_FILE_DB_NAME_KEY = "se.kth.iv1350.database.file.inventory_db";
    String fileName = "item-inventory-test.csv";
    private File flatFileDb;
    String recordHeader = "ITEM ID;NAME;DESCRIPTION;PRICE;VAT RATE GROUP CODE;IN STORE;SOLD";
    String itemRecord = "0;test name;test description;8;1;10;0";

    @BeforeEach
    void setUp() {
        instance = ItemRegistryFlatFileDB.getInstance();
        flatFileDb = new File(
                System.getProperty(FILE_PATH_KEY) +
                        System.getProperty("file.separator") +
                        fileName);

        try {
            flatFileDb.createNewFile();
        } catch (IOException ex) {
            fail("No exception should be thrown, unable to to create a new flat db file."
                    + "%s".formatted(ex.getMessage()));
        }
    }

    @AfterEach
    void tearDown() {
        instance = null;
//        flatFileDb.delete();
        flatFileDb = null;

    }

    @Test
    void testGetInstance() {
        ItemRegistryFlatFileDB newInstance = ItemRegistryFlatFileDB.getInstance();
        assertEquals(instance, newInstance);
    }

    @Test
    void testGetDataInfo() {
        ItemDTO itemInfo = null;
        try {
            itemInfo = instance.getDataInfo(0);
        } catch (ItemNotFoundInItemRegistryException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
        }
        assertEquals(0,itemInfo.getItemID(), "Wrong item info fetched");
    }

    @Disabled
    @Test
    void testUpdateRegistry() {
        System.setProperty(FLAT_FILE_DB_NAME_KEY, fileName);
        try (FileWriter fileWriter = new FileWriter(flatFileDb);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(recordHeader);
            bufferedWriter.newLine();
            bufferedWriter.write(itemRecord);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (FileNotFoundException ex){
            fail("No exception should be thrown, unable to set up the registry test. "
                    + "%s".formatted(ex.getMessage()));
        } catch (IOException ex){
            fail("No exception should be thrown, unable to set up the registry test. "
                    + "%s".formatted(ex.getMessage()));
        }
        String firstRecordStringPreUpdate = "";
        String firstRecordStringPostUpdate = "";
        String expFirstRecordStringPostUpdate = "";

        try (FileReader reader = new FileReader(flatFileDb);
            BufferedReader bufferedReader = new BufferedReader(reader)) {
            bufferedReader.readLine();
            firstRecordStringPreUpdate = bufferedReader.readLine();
        } catch (FileNotFoundException ex){
            fail("No exception should be thrown, unable to set up the registry test. "
                    + "%s".formatted(ex.getMessage()));
        } catch (IOException ex){
            fail("No exception should be thrown, unable to set up the registry test. "
                    + "%s".formatted(ex.getMessage()));
        }

        Sale sale = new Sale();
        try {
            sale = new Sale();
            sale.addItem(0, 1);
            sale.addItem(0, 1);
            sale.addItem(0, 1);
        } catch (ItemNotFoundInItemRegistryException | PricingFailedException | ItemRegistryException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
        }
        sale.endSale();
        sale.pay(new CashPayment(new Amount(100)));
        instance.updateRegistry(sale);
        RegistryHandler.getInstance().updateItemRegistry(sale);

        try (FileReader reader = new FileReader(flatFileDb);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            bufferedReader.readLine();
            firstRecordStringPostUpdate = bufferedReader.readLine();
        } catch (FileNotFoundException ex){
            fail("No exception should be thrown, unable to set up the update registry test. "
                    + "%s".formatted(ex.getMessage()));
        } catch (IOException ex){
            fail("No exception should be thrown, unable to set up the update registry test. "
                    + "%s".formatted(ex.getMessage()));
        }

//        assertFalse(firstRecordStringPostUpdate.equals(firstRecordStringPreUpdate));
        System.out.println(firstRecordStringPreUpdate);
        System.out.println(firstRecordStringPostUpdate);

    }
}