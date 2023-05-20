package se.kth.iv1350.model;

import se.kth.iv1350.integration.CustomerDTO;
import se.kth.iv1350.integration.pricing.DiscountType;

/**
 * Represents a customer of the car rental company.
 */
public class Customer {
    private final int customerID;
    private final DiscountType discountType;
    private int bonusPoints;

    /**
     * Creates a new instance.
     * @param customerID the customers identification number.
     * @param discountType the type of customer as {@link DiscountType}.
     * @param bonusPoints the amount of bonus points collected.
     */
    public Customer(int customerID, DiscountType discountType, int bonusPoints) {
        this.customerID = customerID;
        this.discountType = discountType;
        this.bonusPoints = bonusPoints;
    }

    // TODO ta bort när du implementerat dem andra konstruktuerarna
    public Customer(int customerID, DiscountType discountType) {
        this(customerID, discountType, 0);
    }

    public Customer(CustomerDTO customerInfo){
        this.customerID = customerInfo.getCustomerID();
        this.discountType = customerInfo.getDiscountType();
        this.bonusPoints = customerInfo.getBonusPoints();
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

    public void addBonusPoints(Amount paidAmount) {
        this.bonusPoints += paidAmount.getAmount().intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Customer)) return false;

        Customer that = (Customer) o;

        return Integer.compare(that.customerID, customerID) == 0;
    }
}