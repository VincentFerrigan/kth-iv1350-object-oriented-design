package src.se.kth.iv1350.dto;

//TODO s:
// TODO Ska vi ha en customerDTO och customerDb?
// TODO Move to integration layer.
// TODO ska ett discount attribute finnas med i både sale och saleDTO?
// TODO Ska den vara en tabell av rabatter, procentssats, belopp eller själva discountDTO?
// TODO Total cost - Total discount = total price? (Per vara eller hela försäljningen?)
public class DiscountDTO {
    private double discountRate;
    public DiscountDTO(double discountRate){
        this.discountRate = discountRate;
    }

    public DiscountDTO() {
        this.discountRate= 1;
    }

    public double getDiscountRate(){
        return discountRate;
    }
    public double getDiscountMultiplier() {
        return 1 - discountRate;
    }
}
