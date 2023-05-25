package se.kth.iv1350.integration;

import se.kth.iv1350.integration.pricing.CustomerType;

public class CustomerDTO {
    private final int customerID;
    private final CustomerType customerType;
    private final int bonusPoints;
    public CustomerDTO(int customerID, CustomerType customerType, int bonusPoints) {
        this.customerID = customerID;
        this.customerType = customerType;
        this.bonusPoints = bonusPoints;
    }

    public int getCustomerID() {
        return customerID;
    }

    public CustomerType getDiscountType() {
        return customerType;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }
}
