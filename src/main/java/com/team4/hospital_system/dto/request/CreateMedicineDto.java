package com.team4.hospital_system.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public class CreateMedicineDto {
    @NotBlank
    @Length(max = 100)
    private String name;

    @Length(max = 255)
    private String description;

    @NotNull
    @Future
    private LocalDate expirationDate;

    @NotNull
    @PastOrPresent
    private LocalDate manufacturingDate;

    @NotNull
    @PositiveOrZero
    private Integer price;

    @NotNull
    @PositiveOrZero
    private Integer stockQty;

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
}
