package se.kth.iv1350.integration;

/**
 * Contains information about one particular discount
 */
public class DiscountDTO {
    private double discountRate;

    /**
     * Creates a new instance representing a particular discount.
     * @param discountRate The discount rate for the entire sale
     */
    public DiscountDTO(double discountRate){
        this.discountRate = discountRate;
    }

    /**
     * Creates a new instance representing an empty discount.
     */
    public DiscountDTO() {
        this.discountRate = 0;
    }

    /**
     * Get the discount rate
     * @return the discount rate as a floating point number.
     */
    public double getDiscountRate(){
        return discountRate;
    }

    /**
     * Get the discount multiplier i.e. 1 subtracted by the discount rate
     * (as a floating point number).
     * It is used when applying the discount on a particular sale.
     * @return 1 subtracted by the discount rate.
     */
    public double getDiscountMultiplier() {
        return 1 - discountRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof DiscountDTO)) return false;

        DiscountDTO that = (DiscountDTO) o;

        return Double.compare(that.discountRate, discountRate) == 0;
    }
}
