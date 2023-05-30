package se.kth.iv1350.integration.dto;

import se.kth.iv1350.model.Amount;

import java.time.LocalTime;

/**
 * Contains information about one particular record (as a Data Transfer Object) in the Accounting System
 */
public class RecordDTO {

    private LocalTime timeOfUpdate;
    private Amount totalAmount;
    private Amount totalVATAmount;
    private Amount discounts;

    public RecordDTO(LocalTime timeOfUpdate, Amount totalAmount, Amount totalVATAmount, Amount discounts) {
        this.timeOfUpdate = timeOfUpdate;
        this.totalAmount = totalAmount;
        this.totalVATAmount = totalVATAmount;
        this.discounts = discounts;
    }

    public LocalTime getTimeOfUpdate() {
        return timeOfUpdate;
    }

    public Amount getTotalAmount() {
        return totalAmount;
    }

    public Amount getTotalVATAmount() {
        return totalVATAmount;
    }

    public Amount getDiscounts() {
        return discounts;
    }
}
