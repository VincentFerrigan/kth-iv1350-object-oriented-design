package se.kth.iv1350.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.integration.dto.CustomerDTO;
import se.kth.iv1350.integration.pricing.CustomerType;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    Customer instance;
    CustomerDTO customerInfo;
    int INITAIL_BONUS_POINTS = 100;

    @BeforeEach
    void setUp() {
        customerInfo = new CustomerDTO(1, CustomerType.MEMBER, INITAIL_BONUS_POINTS);
        instance = new Customer(customerInfo);
    }

    @Test
    void addBonusPoints() {
        int initialPoints = instance.getBonusPoints();
        Amount paidAmt = new Amount(50);
        instance.addBonusPoints(paidAmt);
        int expResult = initialPoints + paidAmt.getAmount().intValue();
        int result = instance.getBonusPoints();
        assertEquals(expResult, result, "Wrong bonus points");
    }

    @Test
    void decreaseBonusPoints() {
        int initialPoints = instance.getBonusPoints();
        int usedPoints = 50;
        instance.decreaseBonusPoints(usedPoints);
        int expResult = initialPoints - usedPoints;
        int result = instance.getBonusPoints();
        assertEquals(expResult, result, "Wrong bonus points");
    }
}