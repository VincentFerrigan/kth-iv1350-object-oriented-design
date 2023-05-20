package se.kth.iv1350.integration;

import se.kth.iv1350.integration.pricing.DiscountType;

public class CustomerDTO {
    private final int customerID;
    private final DiscountType discountType;
    private final int bonusPoints;
    public CustomerDTO(int customerID, DiscountType discountType, int bonusPoints) {
        this.customerID = customerID;
        this.discountType = discountType;
        this.bonusPoints = bonusPoints;
    }

    public int getCustomerID() {
        return customerID;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }
}
