package se.kth.iv1350.dto;

//TODO s:
// TODO Ska vi ha en customerDTO och customerDb?
// TODO Move to integration layer.
// TODO ska ett discount attribute finnas med i både sale och saleDTO?
// TODO Ska den vara en tabell av rabatter, procentssats, belopp eller själva discountDTO?
// TODO Total cost - Total discount = total price? (Per vara eller hela försäljningen?)

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
}
