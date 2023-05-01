package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.dto.DiscountDTO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DiscountRegister {
    private final String flatFileDb;
    private final String filePath;
    private Map<Integer, Discount> discountTable = new HashMap<>(); // TODO bör nog ändras till CustomerDTO där en DiscountDTO ingår
    private enum Discount {
        STAFF,
        MEMBER}

    // TODO, based on InventorySystems constructor.
    DiscountRegister(String filePath, String file) {
        this.filePath = filePath;
        this.flatFileDb = file;
        addDiscount();
    }

    private void addDiscount() {
       String splitCsvBy = ";" ;
        FileReader reader;
        try {
            reader = new FileReader(this.filePath + this.flatFileDb);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            line = bufferedReader.readLine();
            while((line = bufferedReader.readLine()) != null) {
                String[] splitArray = line.split(splitCsvBy);
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

    public DiscountDTO getDiscount(int customerID){
        double discountMultiplier;

        switch (discountTable.get(customerID)){
            case STAFF:
                discountMultiplier = 1 - 0.10;
                break;
            case MEMBER:
                discountMultiplier = 1 - 0.05;
                break;
            default:
                discountMultiplier = 1;
                break;
        }
        return new DiscountDTO(discountMultiplier);
    }
}
