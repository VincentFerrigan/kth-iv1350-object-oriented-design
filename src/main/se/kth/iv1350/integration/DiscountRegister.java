package se.kth.iv1350.integration;

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

    /**
     * Creates a new instance of a discount registry.
     * @param filePath the file path to the flat file database
     * @param fileName the file name of the flat file database.
     */
    DiscountRegister(String filePath, String fileName) {
        this.filePath = filePath;
        this.flatFileDb = fileName;
        addDiscount();
    }

    /**
     * Adds discounts to the hashmap from the flat file database.
     */
    private void addDiscount() {
        FileReader reader;
        try {
            reader = new FileReader(this.filePath + this.flatFileDb);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            recordHeader = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null) {
                String[] splitArray = line.split(CSV_DELIMITER);
                this.discountTable.put(Integer.parseInt(splitArray[0]), Discount.valueOf(splitArray[1]));
            }
        } catch (FileNotFoundException e){
            System.out.println("The file was not found");
            e.printStackTrace(); //Skriver ut vart felet var någonstans.

        } catch (IOException e){
            System.out.println("IOE exception");
            e.printStackTrace();
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