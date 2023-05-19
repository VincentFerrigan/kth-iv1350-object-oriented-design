package se.kth.iv1350.integration;

import se.kth.iv1350.util.ErrorFileLogHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A Singleton that creates an instance representing an external discount database
 * This class is a placeholder for a future external discount database.
 */
public class DiscountRegister {
    private static volatile DiscountRegister instance;
    private static final String FILE_SEPARATOR  = System.getProperty("file.separator");
    private static final String FILE_PATH = "src/main/se/kth/iv1350/data/";
    private static final String FLAT_FILE_DB = "discounts.csv";
    private final String CSV_DELIMITER = ";" ;
    private String recordHeader;
    private Map<Integer, Discount> discountTable = new HashMap<>(); // TODO bör nog ändras till CustomerDTO där en DiscountDTO ingår
    private enum Discount {
        STAFF,
        MEMBER}
    private ErrorFileLogHandler logger;

    private DiscountRegister() throws IOException {
        logger = ErrorFileLogHandler.getInstance();
        addDiscount();
    }

    /**
     * @return The only instance of this singleton.
     * @throws IOException
     */
    public static DiscountRegister getInstance() throws IOException {
        DiscountRegister result = instance;
        if (result == null) {
            synchronized (DiscountRegister.class) {
                result = instance;
                if (result == null) {
                    instance = result = new DiscountRegister();
                }
            }
        }
        return result;
    }

    /**
     * Adds discounts to the hashmap from the flat file database.
     */
    private void addDiscount() throws IOException {
        try (FileReader reader = new FileReader(FILE_PATH + FILE_SEPARATOR + FLAT_FILE_DB);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = "";
            recordHeader = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null) {
                String[] splitArray = line.split(CSV_DELIMITER);
                this.discountTable.put(Integer.parseInt(splitArray[0]), Discount.valueOf(splitArray[1]));
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
     * Get the discount as a {@link DiscountDTO}
     * @param customerID The customer identification
     * @return the discount as a {@link DiscountDTO}
     */
    public DiscountDTO getDiscountInfo(int customerID){
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