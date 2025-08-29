package com.team4.hospital_system.dto.response;

import java.time.LocalDate;

public class MedicineDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate expirationDate;
    private LocalDate manufacturingDate;
    private Integer price;
    private Integer stockQty;
    private Long pharmacyId;

    public MedicineDto() {}

    public MedicineDto(Long id, String name, String description, LocalDate expirationDate,
                       LocalDate manufacturingDate, Integer price, Integer stockQty, Long pharmacyId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.expirationDate = expirationDate;
        this.manufacturingDate = manufacturingDate;
        this.price = price;
        this.stockQty = stockQty;
        this.pharmacyId = pharmacyId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }
    public LocalDate getManufacturingDate() { return manufacturingDate; }
    public void setManufacturingDate(LocalDate manufacturingDate) { this.manufacturingDate = manufacturingDate; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }
    public Long getPharmacyId() { return pharmacyId; }
    public void setPharmacyId(Long pharmacyId) { this.pharmacyId = pharmacyId; }
}
