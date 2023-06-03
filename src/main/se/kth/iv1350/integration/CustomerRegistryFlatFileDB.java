package se.kth.iv1350.integration;

import se.kth.iv1350.integration.dto.CustomerDTO;
import se.kth.iv1350.integration.pricing.CustomerType;
import se.kth.iv1350.model.Sale;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * A Singleton that creates an instance representing an external discount database
 * This class is a placeholder for a future external discount database.
 */
public class CustomerRegistryFlatFileDB implements CustomerRegistry {
    private static volatile CustomerRegistryFlatFileDB instance;
    private static final String CSV_DELIMITER = System.getProperty("se.kth.iv1350.database.file.csv_delimiter");
    private final String FILE_PATH_KEY = "se.kth.iv1350.database.file.location";
    private final String FLAT_FILE_DB_NAME_KEY = "se.kth.iv1350.database.file.customer_db";
    private static final int DATABASE_NOT_FOUND = 404;
    private File flatFileDb;
    private String recordHeader;
    private Map<Integer, CustomerData> customerTable = new HashMap<>();

    private CustomerRegistryFlatFileDB() throws CustomerRegistryException {
        flatFileDb = new File(
                System.getProperty(FILE_PATH_KEY) +
                        System.getProperty("file.separator") +
                        System.getProperty(FLAT_FILE_DB_NAME_KEY));
        addCustomerData();
    }

    /**
     * @return The only instance of this singleton.
     * @throws CustomerRegistryException database I/O failure
     */
    public static CustomerRegistryFlatFileDB getInstance() throws CustomerRegistryException {
        CustomerRegistryFlatFileDB result = instance;
        if (result == null) {
            synchronized (CustomerRegistryFlatFileDB.class) {
                result = instance;
                if (result == null) {
                    instance = result = new CustomerRegistryFlatFileDB();
                }
            }
        }
        return result;
    }

    /**
     * Adds customer to the hashmap from the flat file database.
     * @throws CustomerRegistryException database I/O failure
     */
    private void addCustomerData() throws CustomerRegistryException {
        try (FileReader reader = new FileReader(flatFileDb);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = "";
            recordHeader = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null) {
                String[] splitArray = line.split(CSV_DELIMITER);
                CustomerData customerData = new CustomerData(
                        Integer.parseInt(splitArray[0]),
                        CustomerType.valueOf(splitArray[1]),
                        Integer.parseInt(splitArray[2]));
                this.customerTable.put(customerData.customerID, customerData);
            }
        } catch (FileNotFoundException ex){
            throw new CustomerRegistryException("Flat-file db %s not found in path %s"
                    .formatted(flatFileDb.getName(), flatFileDb.getPath()), ex);
        } catch (IOException ex){
            throw new CustomerRegistryException("Unable to read from flat file db %s in path %s"
                    .formatted(flatFileDb.getName(), flatFileDb.getPath()), ex);
        }
    }

    /**
     * Searches for customer in the customer database with specified ID.
     * @param dataID The customer identification
     * @return Customer information as a {@link CustomerDTO}.
     * @throws CustomerNotFoundInCustomerRegistryException when customer ID does not exist in customer registry.
     * @throws CustomerRegistryException when database call failed.
     */
    @Override
    public CustomerDTO getDataInfo(Object dataID ) throws CustomerNotFoundInCustomerRegistryException, CustomerRegistryException {
        Integer customerID = (Integer) dataID;
        if (customerID == DATABASE_NOT_FOUND) {
            throw new CustomerRegistryException("Detailed message about database fail");
        } else if (customerTable.containsKey(customerID)) {
            CustomerData customerData = this.customerTable.get(customerID);
            return new CustomerDTO(
                    customerData.customerID, customerData.customerType, customerData.bonusPoints);
        } else {
            throw new CustomerNotFoundInCustomerRegistryException(customerID);
        }
    }

    /**
     * Updates the Customer registry by adding the specified {@link Sale}.
     * @param closedSale contains the sale details
     * @throws CustomerRegistryException when database call failed
     */
    @Override
    public void updateRegistry(Sale closedSale) throws CustomerRegistryException{
        if (closedSale.getCustomer() !=null) {
            int bonusPoints = closedSale.getCustomer().getBonusPoints();
            int key = closedSale.getCustomer().getCustomerID();
            customerTable.get(key).bonusPoints += bonusPoints;
            updateDatabase();
        }
    }

    /**
     * Update database by writing to the flat file database
     * @throws CustomerRegistryException database I/O failure
     */
    private void updateDatabase() throws CustomerRegistryException{
        try (FileWriter fileWriter = new FileWriter(flatFileDb.getPath().replace(".csv", "_" + LocalDate.now() + ".csv"));
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(recordHeader);
            bufferedWriter.newLine();
            for (CustomerData customerData : customerTable.values()) {
                bufferedWriter.write(customerData.toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (FileNotFoundException ex){
            throw new CustomerRegistryException("Flat-file db %s not found in path %s"
                    .formatted(flatFileDb.getName(), flatFileDb.getPath()), ex);
        } catch (IOException ex){
            throw new CustomerRegistryException("Unable to write to flat file db %s in path %s"
                    .formatted(flatFileDb.getName(), flatFileDb.getPath()), ex);
        }
    }


    private static class CustomerData {
        private final int customerID;
        private CustomerType customerType;
        private int bonusPoints;

        public CustomerData(int customerID, CustomerType customerType, int bonusPoints)  {
            this.customerID = customerID;
            this.customerType = customerType;
            this.bonusPoints = bonusPoints;
        }

        @Override
        public String toString() {
            String csv_delimiter = CustomerRegistryFlatFileDB.CSV_DELIMITER;

            StringBuilder builder = new StringBuilder();
            builder.append(customerID);
            builder.append(csv_delimiter);
            builder.append(customerType);
            builder.append(csv_delimiter);
            builder.append(bonusPoints);
            return builder.toString();
        }
    }
}