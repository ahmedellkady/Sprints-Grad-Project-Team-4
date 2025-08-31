package com.team4.hospital_system.dto.request;

import jakarta.validation.constraints.NotNull;

public class CheckoutDto {
    @NotNull
    private Long pharmacyId;
    
    private String shippingAddress;
    private String paymentMethod;
    private String notes;

    public CheckoutDto() {}

    public CheckoutDto(Long pharmacyId, String shippingAddress, String paymentMethod, String notes) {
        this.pharmacyId = pharmacyId;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
    }

    public Long getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(Long pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
