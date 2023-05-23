package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmountTest {
    private final double HALF = 0.5;
    private final double DOUBLE = 2;
    private final double ONE_HUNDRED = 100;
    private final double TWO_HUNDRED = 200;
    private Amount cashOneHundred;
    private Amount cashTwoHundred;
    private Amount amountWithNoArgument;

    @BeforeEach
    void setUp() {
        cashOneHundred = new Amount(ONE_HUNDRED);
        cashTwoHundred = new Amount(TWO_HUNDRED);
        amountWithNoArgument = new Amount();
    }

    @AfterEach
    void tearDown() {
        cashOneHundred = null;
        cashTwoHundred = null;
        amountWithNoArgument = null;
    }

    @Test
    void testMinus() {
        Amount cashResult = cashTwoHundred.minus(cashOneHundred);
        double floatResult = cashResult.getAmount();

        double expFloatResult = TWO_HUNDRED - ONE_HUNDRED;
        Amount expCashResult = new Amount(expFloatResult);

        assertEquals(expFloatResult, floatResult, "Wrong differance");
        assertEquals(expCashResult, cashResult, "Amount not equal");
    }

    @Test
    void plus() {
    }

    @Test
    void testPlus() {
        Amount cashResult = cashTwoHundred.plus(cashOneHundred);
        double floatResult = cashResult.getAmount();

        double expFloatResult = TWO_HUNDRED + ONE_HUNDRED;
        Amount expCashResult = new Amount(expFloatResult);

        assertEquals(expFloatResult, floatResult, "Wrong sum of amount");
        assertEquals(expCashResult, cashResult, "Amount not equal");
    }

    @Test
    void testMultiply() {
        Amount cashResult = cashOneHundred.multiply(DOUBLE);
        double floatResult = cashResult.getAmount();

        double expFloatResult = ONE_HUNDRED * DOUBLE;
        Amount expCashResult = new Amount(cashTwoHundred);

        assertEquals(expFloatResult, floatResult, "Amount not doubled");
        assertEquals(expCashResult, cashResult, "Amount not equal");

        cashResult = cashTwoHundred.multiply(HALF);
        floatResult = cashResult.getAmount();
        expFloatResult = TWO_HUNDRED * HALF;
        expCashResult = new Amount(cashOneHundred);

        assertEquals(expFloatResult, floatResult, "Amount not doubled");
        assertEquals(expCashResult, cashResult, "Amount not equal");
    }

    @Test
    void testEquals() {
        Amount other = new Amount(ONE_HUNDRED);
        boolean expResult = true;
        boolean result = cashOneHundred.equals(other);
        assertEquals(expResult, result, "Amount instances with same states are not equal.");
    }

    @Test
    public void testEqualAmountWithNoArgumentConstructor() {
        int amountOfOther = 0;
        Amount other = new Amount(amountOfOther);
        boolean expResult = true;
        boolean result = amountWithNoArgument.equals(other);
        assertEquals(expResult, result, "Amount instances with same states are not equal.");
    }

    @Test
    void compareTo() {
        assertEquals(cashTwoHundred.compareTo(cashOneHundred) >0 , TWO_HUNDRED > ONE_HUNDRED);
    }

    @Test
    void testToString() {
        String result = cashOneHundred.toString();
        String expResult = "%,.2f kr".formatted(ONE_HUNDRED);
        assertEquals(expResult, result, "Wrong string returned by toString");
    }
}