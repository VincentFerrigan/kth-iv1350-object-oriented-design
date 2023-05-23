package se.kth.iv1350.model;

import se.kth.iv1350.integration.CustomerDTO;
import se.kth.iv1350.integration.pricing.CustomerType;

/**
 * Represents a customer of the car rental company.
 */
public class Customer {
    private final int customerID;
    private final CustomerType customerType;
    private int bonusPoints;
    private Amount discount;
    private String discountInformation;

    /**
     * Creates a new instance.
     * @param customerID the customers identification number.
     * @param customerType the type of customer as {@link CustomerType}.
     * @param bonusPoints the amount of bonus points collected.
     */
    public Customer(int customerID, CustomerType customerType, int bonusPoints) {
        this.customerID = customerID;
        this.customerType = customerType;
        this.bonusPoints = bonusPoints;
    }

    /**
     * Creates a new instance.
     * @param customerInfo customer information as {@link CustomerDTO}.
     */
    public Customer(CustomerDTO customerInfo){
        this.customerID = customerInfo.getCustomerID();
        this.customerType = customerInfo.getDiscountType();
        this.bonusPoints = customerInfo.getBonusPoints();
    }

    public int getCustomerID() {
        return customerID;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public void addBonusPoints(Amount paidAmount) {
        this.bonusPoints += paidAmount.getAmount().intValue();
    }

    public void decreaseBonusPoints(int usedBonusPoints) { this.bonusPoints -= usedBonusPoints; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Customer)) return false;

        Customer that = (Customer) o;

        return Integer.compare(that.customerID, customerID) == 0;
    }
}