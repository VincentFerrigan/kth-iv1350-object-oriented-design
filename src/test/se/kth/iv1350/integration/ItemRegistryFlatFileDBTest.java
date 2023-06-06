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
    private File flatFileDb;

    @BeforeEach
    void setUp() {
        instance = ItemRegistryFlatFileDB.getInstance();
        flatFileDb = new File(
                System.getProperty(FILE_PATH_KEY) +
                        System.getProperty("file.separator") +
                        System.getProperty(FLAT_FILE_DB_NAME_KEY));
    }

    @AfterEach
    void tearDown() {
        instance = null;
        flatFileDb = null;
    }

    @Test
    void getInstance() {
        ItemRegistryFlatFileDB newInstance = ItemRegistryFlatFileDB.getInstance();
        assertEquals(instance, newInstance);
    }

    @Test
    void getDataInfo() {
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
    void updateRegistry() {
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
        } catch (PricingFailedException ex) {
            // Not part of the test
            fail("No exception should be thrown, unable to set up the SaleLogTest. "
                    + "%s".formatted(ex.getMessage()));
        }
        try {
            sale.addItem(0, 1);
        } catch (ItemNotFoundInItemRegistryException | PricingFailedException | ItemRegistryException e) {
            fail("Exception should not have been thrown, " +
                    e.getMessage());
        }
        sale.endSale();
        sale.pay(new CashPayment(new Amount(100)));
        instance.updateRegistry(sale);

        try (FileReader reader = new FileReader(flatFileDb.getPath().replace(".csv", "-" + LocalDate.now()));
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

            assertFalse(firstRecordStringPostUpdate.equals(firstRecordStringPreUpdate));
    }
}