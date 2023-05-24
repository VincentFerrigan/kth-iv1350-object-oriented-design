package se.kth.iv1350.model;

import org.junit.jupiter.api.*;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.integration.CustomerDTO;
import se.kth.iv1350.integration.ItemDTO;
import se.kth.iv1350.integration.pricing.CustomerType;
import se.kth.iv1350.integration.pricing.DiscountFactory;
import se.kth.iv1350.integration.pricing.DiscountStrategy;

import java.lang.reflect.InvocationTargetException;

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
                new Amount(100), new VAT(1));
        itemPrice = itemInfo.getUnitPrice();
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
        Amount exResult = itemInfo.getUnitPrice();
        Amount result = instance.getTotalCostPaid();
        assertEquals(exResult, result, "Wrong total cost");
    }

    @Test
    void testCalculateTotalCostWithCustomerBonusAndDiscount() {
        CustomerDTO customerInfo = new CustomerDTO(1, CustomerType.MEMBER, 0);
        sale.addCustomerToSale(customerInfo);
        instance.calculateTotalCost(sale);
        Customer customer = sale.getCustomer();
        Amount exPriceResult = itemInfo.getUnitPrice().minus(sale.getDiscount());
        Amount totalCostPaidResult = instance.getTotalCostPaid();

        int bonusPointResult = customer.getBonusPoints();
        int exBonusPointResult = exPriceResult.getAmount().intValue();
        assertEquals(exPriceResult, totalCostPaidResult, "Wrong total cost");
        assertEquals(exBonusPointResult, bonusPointResult, "Bonus points not registered");
    }
    @Test
    void getTotalCostPaid() {
        instance.calculateTotalCost(sale);
        Amount exResult = itemPrice;
        Amount result = instance.getTotalCostPaid();
        assertEquals(exResult, result, "Wrong paid amount");
    }
    @Test
    void getChange() {
        instance.calculateTotalCost(sale);
        Amount exResult = paidAmount.minus(itemPrice);
        Amount result = instance.getChange();
        assertEquals(exResult, result, "Wrong change");
    }
}