package se.kth.iv1350.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class VATTest {

    @Test
    void testVATDefault() {
        VAT vatDefault = new VAT();
        double vatRateResult = vatDefault.getVATRate();
        int vatRateGroupCodeResult = vatDefault.getVATRateGroupCode();

        double expVATRateResult = 0.25;
        int expVATRateGroupCodeResult = 1;

        assertEquals(expVATRateResult, vatRateResult, "Wrong VAT rate");
        assertEquals(expVATRateGroupCodeResult, vatRateGroupCodeResult, "Wrong VAT Group Code");
    }

    @Test
    void testVAT() {
        VAT vat0 = new VAT(0);
        VAT vat1 = new VAT(1);
        VAT vat2 = new VAT(2);
        VAT vat3 = new VAT(3);

        assertEquals(0, vat0.getVATRate(), "Wrong VAT rate");
        assertEquals(0.25, vat1.getVATRate(), "Wrong VAT rate");
        assertEquals(0.12, vat2.getVATRate(), "Wrong VAT rate");
        assertEquals(0.06, vat3.getVATRate(), "Wrong VAT rate");
    }

}