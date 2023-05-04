package se.kth.iv1350.model;

// TODO Is this considered to be a DTO? (SHOW STOPPER)
// TODO @Override toString()
// TODO Should the rates get collected from a database (NICE TO HAVE)
// TODO Should it be country based? Where the default for now is Sweden?
// https://www.verksamt.se/web/international/running/vat
// https://skatteverket.se/servicelankar/otherlanguages/inenglishengelska/businessesandemployers/startingandrunningaswedishbusiness/declaringtaxesbusinesses/vat/vatratesongoodsandservices.4.676f4884175c97df419255d.html

/** Contains information about VAT.
 *  The 25% VAT rate applies to most of the goods and services you are likely to sell in Sweden.
 *
 *  VAT is normally 12% on foodstuffs. This refers to all processed or unprocessed substances or goods intended
 *  for human consumption. Except for spirits, wine and beer with an alcohol content above 3.5 % by volume,
 *  tobacco and tobacco goods etc.
 *
 *  A VAT rate of 6% normally applies to newspapers, magazines, books,
 *  passenger transport (taxis, buses, flights and trains) in Sweden and concerts.
 */
public class VAT {
//    private Locale locale; // If the POS wants to go global. (<3/e)
    private int vatRateGroupCode;
    private double vatRate;

    /**
     * Creates a new instance representing a VAT rate based on VAT rate groups.
     * @param vatRateGroupCode The code for the VAT rate group, such as 0, 1, 2 or 3. Where currently
     *                          0 is VAT exempt
     *                          1 is 25 %,
     *                          2 is 12 % and
     *                          3 is 6 %
     */
    public VAT(int vatRateGroupCode) {
        this.vatRateGroupCode = vatRateGroupCode;
        switch (vatRateGroupCode) {
            case 0:
                this.vatRate = 0;
                break;
            case 1:
                this.vatRate = 0.25;
                break;
            case 2:
                this.vatRate = 0.12;
                break;
            case 3:
                this.vatRate = 0.6;
                break;
            default:
                this.vatRate = 0.25;
                break;
        }
    }

    /**
     * Creates a new instance representing a VAT rate based on VAT rate groups.
     * Default VAT rate group is 1. Currently, 25 %.
     */
    public VAT() {
        this(1);
    }

    /**
     * Get the code for the VAT rate group.
     * @return The code for the VAT rate group.
     */
    public int getVATRateGroupCode() {
        return vatRateGroupCode;
    }

    /**
     * Get VAT rate
     * @return The VAT rate.
     */
    public double getVATRate() {
        return vatRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof VAT)) return false;

        VAT vat = (VAT) o;

        if (vatRateGroupCode != vat.vatRateGroupCode) return false;
        return Double.compare(vat.vatRate, vatRate) == 0;
    }
}
