package se.kth.iv1350.model;

import org.junit.jupiter.api.*;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.CustomerDTO;
import se.kth.iv1350.integration.ItemDTO;
import se.kth.iv1350.integration.pricing.CustomerType;

import static org.junit.jupiter.api.Assertions.*;

class CashPaymentTest {

    private CashPayment instance;
    private final double PAID = 500;
    private Amount paidAmount;
    private Sale sale;
    private ItemDTO itemInfo;
    private Amount itemPrice;

    @BeforeEach
    void setUp() {
        paidAmount = new Amount(PAID);
        instance = new CashPayment(paidAmount);
        try {
            sale = new Sale();
        } catch (OperationFailedException ex) {
            fail("Failed to setUp CashPaymentTest");
            ex.printStackTrace();
        }
        itemInfo = new ItemDTO(1, "Product name", "Product Description",
                new Amount(80), new VAT(1));
        itemPrice = new Amount(100);
        sale.addItem(itemInfo, 1);
    }

    @AfterEach
    void tearDown() {
        instance = null;
        paidAmount = null;
        sale = null;
        itemInfo = null;
        itemPrice = null;
    }

    @Test
    void testCalculateTotalCost() {
        instance.calculateTotalCost(sale);
        Amount expResult = itemPrice;
        Amount result = instance.getTotalCostPaid();
        assertEquals(expResult, result, "Wrong total cost");
    }

    @Test
    void testCalculateTotalCostWithCustomerBonusAndDiscount() {
        CustomerDTO customerInfo = new CustomerDTO(1, CustomerType.MEMBER, 0);
        sale.addCustomerToSale(customerInfo);
        instance.calculateTotalCost(sale);
        Customer customer = sale.getCustomer();
        Amount expPriceResult = itemPrice.minus(sale.getDiscount());
        Amount totalCostPaidResult = instance.getTotalCostPaid();

        int bonusPointResult = customer.getBonusPoints();
        int expBonusPointResult = expPriceResult.getAmount().intValue();
        assertEquals(expPriceResult, totalCostPaidResult, "Wrong total cost");
        assertEquals(expBonusPointResult, bonusPointResult, "Bonus points not registered");
    }
    @Test
    void getTotalCostPaid() {
        instance.calculateTotalCost(sale);
        Amount expResult = itemPrice;
        Amount result = instance.getTotalCostPaid();
        assertEquals(expResult, result, "Wrong paid amount");
    }
    @Test
    void getChange() {
        instance.calculateTotalCost(sale);
        Amount expResult = paidAmount.minus(itemPrice);
        Amount result = instance.getChange();
        assertEquals(expResult, result, "Wrong change");
    }
}