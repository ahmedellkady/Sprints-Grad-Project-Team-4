package com.team4.hospital_system.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Long medicineId;
    private String medicineName;
    private int quantity;
    private BigDecimal price;
}
