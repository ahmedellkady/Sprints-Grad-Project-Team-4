package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.response.CartDto;

public interface CartService {
    CartDto getCart(long patientId , long PharmacyId);
    CartDto addItem(long patientId, long medicineId, int quantity , long pharmacyId);
    CartDto removeItem(long patientId, long medicineId , long pharmacyId);
    CartDto clear(long patientId, long pharmacyId);
}
