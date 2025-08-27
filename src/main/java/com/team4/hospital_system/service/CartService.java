package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.response.CartDto;

public interface CartService {
    CartDto getCart(long patientId);
    CartDto addItem(long patientId, long medicineId, int quantity);
    CartDto removeItem(long patientId, long medicineId);
    void clear(long patientId);
}
