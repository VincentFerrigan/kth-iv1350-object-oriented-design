package se.kth.iv1350.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class AmountTest {
    private final double HALF = 0.5;
    private final double DOUBLE = 2;
    private final double ONE_HUNDRED = 100;
    private final double TWO_HUNDRED = 200;
    private Amount instanceOneHundred;
    private Amount instanceTwoHundred;
    private Amount instanceAmountWithoutArguments;
    private Amount instanceOneHundredInUSDollars;

    @BeforeEach
    void setUp() {
        instanceOneHundred = new Amount(ONE_HUNDRED);
        instanceTwoHundred = new Amount(TWO_HUNDRED);
        instanceAmountWithoutArguments = new Amount();
        instanceOneHundredInUSDollars = new Amount(ONE_HUNDRED, Locale.US);
    }

    @AfterEach
    void tearDown() {
        instanceOneHundred = null;
        instanceTwoHundred = null;
        instanceAmountWithoutArguments = null;
        instanceOneHundredInUSDollars = null;
    }

    @Test
    void testMinus() {
        Amount cashResult = instanceTwoHundred.minus(instanceOneHundred);
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
        Amount cashResult = instanceTwoHundred.plus(instanceOneHundred);
        double floatResult = cashResult.getAmount();

        double expFloatResult = TWO_HUNDRED + ONE_HUNDRED;
        Amount expCashResult = new Amount(expFloatResult);

        assertEquals(expFloatResult, floatResult, "Wrong sum of amount");
        assertEquals(expCashResult, cashResult, "Amount not equal");
    }

    @Test
    void testMultiply() {
        Amount cashResult = instanceOneHundred.multiply(DOUBLE);
        double floatResult = cashResult.getAmount();

        double expFloatResult = ONE_HUNDRED * DOUBLE;
        Amount expCashResult = new Amount(instanceTwoHundred);

        assertEquals(expFloatResult, floatResult, "Amount not doubled");
        assertEquals(expCashResult, cashResult, "Amount not equal");

        cashResult = instanceTwoHundred.multiply(HALF);
        floatResult = cashResult.getAmount();
        expFloatResult = TWO_HUNDRED * HALF;
        expCashResult = new Amount(instanceOneHundred);

        assertEquals(expFloatResult, floatResult, "Amount not doubled");
        assertEquals(expCashResult, cashResult, "Amount not equal");
    }

    @Test
    void testEquals() {
        Amount other = new Amount(ONE_HUNDRED);
        boolean expResult = true;
        boolean result = instanceOneHundred.equals(other);
        assertEquals(expResult, result, "Amount instances with same states are not equal.");
    }

    @Test
    public void testEqualAmountWithNoArgumentConstructor() {
        int amountOfOther = 0;
        Amount other = new Amount(amountOfOther);
        boolean expResult = true;
        boolean result = instanceAmountWithoutArguments.equals(other);
        assertEquals(expResult, result, "Amount instances with same states are not equal.");
    }

    @Test
    void compareTo() {
        assertEquals(
                instanceTwoHundred.compareTo(instanceOneHundred) > 0,
                TWO_HUNDRED > ONE_HUNDRED,
                  "%,.2f kr not greater than %,.2f kr".formatted(TWO_HUNDRED, ONE_HUNDRED));
        assertEquals(
                instanceOneHundred.compareTo(instanceTwoHundred) < 0,
                ONE_HUNDRED < TWO_HUNDRED,
                "%,.2f kr not less than %,.2f kr".formatted(ONE_HUNDRED, TWO_HUNDRED));
        assertEquals(
                instanceOneHundred.compareTo(instanceOneHundred) == 0,
                ONE_HUNDRED == ONE_HUNDRED,
                "%,.2f kr not equal to %,.2f kr".formatted(ONE_HUNDRED, ONE_HUNDRED));

    }

    @Test
    void testToString() {
        assertEquals(
                "%,.2f kr".formatted(ONE_HUNDRED),
                instanceOneHundred.toString(),
                "Wrong string returned by toString");
        assertEquals(
                "%,.2f $".formatted(ONE_HUNDRED),
                instanceOneHundredInUSDollars.toString(),
                "Wrong string returned by toString");

    }
}