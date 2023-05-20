package se.kth.iv1350.integration;

import se.kth.iv1350.integration.pricing.DiscountType;
import se.kth.iv1350.model.Customer;
import se.kth.iv1350.model.Sale;
import se.kth.iv1350.model.ShoppingCartItem;
import se.kth.iv1350.util.DBParameters;
import se.kth.iv1350.util.ErrorFileLogHandler;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Singleton that creates an instance representing an external discount database
 * This class is a placeholder for a future external discount database.
 */
public class CustomerRegistry {
    private static volatile CustomerRegistry instance;
    private static final String CSV_DELIMITER = ";" ;
    private static final int DATABASE_NOT_FOUND = 404;
    private File flatFileDb;
    private String recordHeader;
    private Map<Integer, CustomerData> customerTable = new HashMap<>();
    private ErrorFileLogHandler logger;

    private CustomerRegistry() throws IOException {
        this.logger = ErrorFileLogHandler.getInstance();
        DBParameters dBParams = DBParameters.getInstance();
        flatFileDb = dBParams.getCustomerFlatFileDb();

        addCustomerData();
    }

    /**
     * @return The only instance of this singleton.
     * @throws IOException
     */
    public static CustomerRegistry getInstance() throws IOException {
        CustomerRegistry result = instance;
        if (result == null) {
            synchronized (CustomerRegistry.class) {
                result = instance;
                if (result == null) {
                    instance = result = new CustomerRegistry();
                }
            }
        }
        return result;
    }

    /**
     * Adds customer to the hashmap from the flat file database.
     */
    private void addCustomerData() throws IOException {
        try (FileReader reader = new FileReader(flatFileDb);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = "";
            recordHeader = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null) {
                String[] splitArray = line.split(CSV_DELIMITER);
                CustomerData customerData = new CustomerData(
                        Integer.parseInt(splitArray[0]),
                        DiscountType.valueOf(splitArray[1]),
                        Integer.parseInt(splitArray[2]));
                this.customerTable.put(customerData.customerID, customerData);
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
     * Updates the customer database.
     * @param closedSale contains the sale details
     */
    public void updateCustomerDatabase(Sale closedSale){
        if (closedSale.getCustomer() !=null) {
            int bonusPoints = closedSale.getCustomer().getBonusPoints();
            int key = closedSale.getCustomer().getCustomerID();
            customerTable.get(key).bonusPoints += bonusPoints;
            updateDatabase();
        }
    }

    /**
     * Update database by writing to the flat file database
     */
    private void updateDatabase() {
        try (FileWriter fileWriter = new FileWriter(flatFileDb.getPath().replace(".csv", "_" + LocalDate.now() + ".csv"));
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(recordHeader);
            bufferedWriter.newLine();
            for (CustomerRegistry.CustomerData customerData : customerTable.values()) {
                bufferedWriter.write(customerData.toString());
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



    /**
     * Searches for customer in the customer database with specified ID.
     * @param customerID The customer identification
     * @return Customer information as a {@link CustomerDTO}.
     * @throws CustomerNotFoundInCustomerRegistryException when customer ID does not exist in customer registry.
     * @throws CustomerRegistryException when database call failed.
     */
    //TODO Are we supposed to throw ItemRegistryException as well with method?
    public CustomerDTO getCustomerInfo(int customerID) throws CustomerNotFoundInCustomerRegistryException {
        if (customerID == DATABASE_NOT_FOUND) {
            throw new CustomerRegistryException("Detailed message about database fail");
        } else if (customerTable.containsKey(customerID)) {
            CustomerData customerData = this.customerTable.get(customerID);
            return new CustomerDTO(
                    customerData.customerID, customerData.discountType, customerData.bonusPoints);
        } else {
            throw new CustomerNotFoundInCustomerRegistryException(customerID);
        }
    }

    private static class CustomerData {
        private int customerID;
        private DiscountType discountType;
        private int bonusPoints;

        public CustomerData(int customerID, DiscountType discountType, int bonusPoints)  {
            this.customerID = customerID;
            this.discountType = discountType;
            this.bonusPoints = bonusPoints;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(customerID);
            builder.append(CustomerRegistry.CSV_DELIMITER);
            builder.append(discountType);
            builder.append(CustomerRegistry.CSV_DELIMITER);
            builder.append(bonusPoints);
            return builder.toString();
        }
    }
}