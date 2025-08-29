package com.team4.hospital_system.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.PositiveOrZero;

public class MedicineSearchDto {
    @Length(max = 100)
    private String name;

    @PositiveOrZero
    private Integer minPrice;

    @PositiveOrZero
    private Integer maxPrice;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getMinPrice() { return minPrice; }
    public void setMinPrice(Integer minPrice) { this.minPrice = minPrice; }
    public Integer getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Integer maxPrice) { this.maxPrice = maxPrice; }
}
