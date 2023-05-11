package se.kth.iv1350.integration;

import se.kth.iv1350.util.LogHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an external discount database
 * This class is a placeholder for a future external discount database.
 */
public class DiscountRegister {
    private final String CSV_DELIMITER = ";" ;
    private String recordHeader;
    private final String flatFileDb;
    private final String filePath;
    private Map<Integer, Discount> discountTable = new HashMap<>(); // TODO bör nog ändras till CustomerDTO där en DiscountDTO ingår
    private enum Discount {
        STAFF,
        MEMBER}
    private LogHandler logger;

    /**
     * Creates a new instance of a discount registry.
     * @param filePath the file path to the flat file database
     * @param fileName the file name of the flat file database.
     */
    DiscountRegister(String filePath, String fileName) throws IOException {
        this.filePath = filePath;
        this.flatFileDb = fileName;
        this.logger = new LogHandler();
        addDiscount();
    }

    /**
     * Adds discounts to the hashmap from the flat file database.
     */
    private void addDiscount() throws IOException {
        try (FileReader reader = new FileReader(this.filePath + this.flatFileDb);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = "";
            recordHeader = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null) {
                String[] splitArray = line.split(CSV_DELIMITER);
                this.discountTable.put(Integer.parseInt(splitArray[0]), Discount.valueOf(splitArray[1]));
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
     * Get the discount as a {@link DiscountDTO}
     * @param customerID The customer identification
     * @return the discount as a {@link DiscountDTO}
     */
    public DiscountDTO getDiscount(int customerID){
        double discountRate;

        switch (discountTable.get(customerID)){
            case STAFF:
                discountRate = 0.10;
                break;
            case MEMBER:
                discountRate = 0.05;
                break;
            default:
                discountRate = 0;
                break;
        }
        return new DiscountDTO(discountRate);
    }
}