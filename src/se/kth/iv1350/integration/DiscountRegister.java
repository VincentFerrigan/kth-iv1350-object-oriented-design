package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.dto.DiscountDTO;

import java.util.HashMap;
import java.util.Map;

public class DiscountRegister {
    private Map<Integer, Discount> discountTable = new HashMap<>(); // TODO bör nog ändras till CustomerDTO där en DiscountDTO ingår
    public static final String FLAT_FILE_DB = "src/se/kth/iv1350/integration/discounts.csv";
    private enum Discount {
        STAFF,
        VIP}

    public DiscountRegister(){
        int testCustomer1 = 1;
        //TODO construct
    }

    // TODO, based on InventorySystems constructor.
    public DiscountRegister(String file) {
        String filePath = file;
        String splitCsvBy = ";";
    }

    public DiscountDTO getDiscount(int customerID){
        double discountMultiplier;

        switch (discountTable.get(customerID)){
            case STAFF:
                discountMultiplier = 0.10;
                break;
            default:
                discountMultiplier = 1;
                break;
        }
        return new DiscountDTO(discountMultiplier);
    }
}
