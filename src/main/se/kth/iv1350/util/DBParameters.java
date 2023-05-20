package se.kth.iv1350.util;

import se.kth.iv1350.integration.ItemRegistry;

import java.io.*;

public class DBParameters {
    private static final String FILE_SEPARATOR  = System.getProperty("file.separator");
    private static final String FILE_PATH = "src.main.se.kth.iv1350.data".replace(".", FILE_SEPARATOR);
    private static final String REGISTRY_FILE = "registry.csv";
    private final String CSV_DELIMITER = ";";
    private static volatile DBParameters instance;
    private String inventoryFlatFileDb;
    private String customerFlatFileDb;
    private String accountingFlatFileDb;
    private ErrorFileLogHandler logger;

    private DBParameters() throws IOException {
        this.logger = ErrorFileLogHandler.getInstance();
        String parameters[];
        parameters = readParamsFromFile();
        accountingFlatFileDb = parameters[0].trim();
        customerFlatFileDb = parameters[1].trim();
        inventoryFlatFileDb = parameters[2].trim();
    }
    public static DBParameters getInstance() throws IOException {
        DBParameters result = instance;
        if (result == null) {
            synchronized (ItemRegistry.class) {
                result = instance;
                if (result == null) {
                    instance = result = new DBParameters();
                }
            }
        }
        return result;
    }
    private String[] readParamsFromFile() throws IOException {
        String[] params;
        try (FileReader reader = new FileReader(FILE_PATH + FILE_SEPARATOR + REGISTRY_FILE);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = "";
            bufferedReader.readLine();
            line = bufferedReader.readLine();
            params = line.split(CSV_DELIMITER);
        } catch (NullPointerException ex) {
            logger.log(ex);
            throw ex;
        } catch (FileNotFoundException ex) {
            logger.log(ex);
            throw ex;
        } catch (IOException ex) {
            logger.log(ex);
            throw ex;
        }
        return params;
    }

    public String getCSV_DELIMITER() {
        return CSV_DELIMITER;
    }

    public File getInventoryFlatFileDb() {
        return new File(FILE_PATH + FILE_SEPARATOR + inventoryFlatFileDb);
    }

    public File getCustomerFlatFileDb() {
        return new File(FILE_PATH + FILE_SEPARATOR + customerFlatFileDb);
    }

    public File getAccountingFlatFileDb() {
        return new File(FILE_PATH + FILE_SEPARATOR + accountingFlatFileDb);
    }
}
