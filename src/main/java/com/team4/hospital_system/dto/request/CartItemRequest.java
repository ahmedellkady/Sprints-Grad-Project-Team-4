package com.team4.hospital_system.dto.request;

public class CartItemRequest {
    private long medicineId;
    private int quantity;
    private Long pharmacyId;

    public long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(long medicineId) {
        this.medicineId = medicineId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getPharmacyId() {return pharmacyId;}

    public void setPharmacyId(Long pharmacyId) {this.pharmacyId = pharmacyId;}
}
