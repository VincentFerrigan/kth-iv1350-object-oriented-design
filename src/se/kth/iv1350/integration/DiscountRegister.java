package src.se.kth.iv1350.integration;

import src.se.kth.iv1350.dto.DiscountDTO;

public class DiscountRegister {
    public DiscountRegister(){
        //TODO construct
    }

    public DiscountDTO discountRegister(int customerID){
        return new DiscountDTO(customerID);
    }
}
