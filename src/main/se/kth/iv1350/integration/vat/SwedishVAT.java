package se.kth.iv1350.integration.vat;

import se.kth.iv1350.model.Amount;
import se.kth.iv1350.model.ShoppingCartItem;

/**
 * A <code>VATStrategy</code> that calculates Swedish VAT casts for a specific item
 */
public class SwedishVAT implements VATStrategy {

    @Override
    public Amount calculateVATForItem(ShoppingCartItem item) {
        int vatGroupCode = item.getVATGroupCode();
        Amount unitPrice = item.getUnitPrice();
        return unitPrice.multiply(getVATRate(vatGroupCode));
    }

    private double getVATRate(int vatGroupCode) {
        double vatRate;
        switch (vatGroupCode) {
            case 0:
                vatRate = 0;
                break;
            case 1:
                vatRate = 0.25;
                break;
            case 2:
                vatRate = 0.12;
                break;
            case 3:
                vatRate = 0.06;
                break;
            default:
                vatRate = 0.25;
                break;
        }
        return vatRate;
    }
}