package com.team4.hospital_system.service;

import com.team4.hospital_system.dto.request.CheckoutDto;
import com.team4.hospital_system.dto.response.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto checkout(long patientId, CheckoutDto request);
    OrderDto getById(long orderId);
    List<OrderDto> listForPatient(long patientId);
}
