package src.se.kth.iv1350.dto;

//TODO s:
// TODO Ska vi ha en customerDTO och customerDb?
// TODO Move to integration layer.
// TODO ska ett discount attribute finnas med i både sale och saleDTO?
// TODO Ska den vara en tabell av rabatter, procentssats, belopp eller själva discountDTO?
// TODO Total cost - Total discount = total price? (Per vara eller hela försäljningen?)
public class DiscountDTO {
    private double discountMultiplier;
    public DiscountDTO(double discountMultiplier){
        this.discountMultiplier = discountMultiplier;
    }

    public DiscountDTO() {
        this.discountMultiplier = 1;
    }

    public double getDiscountMultiplier() {
        return discountMultiplier;
    }
}
