package com.team4.hospital_system.repository;

import com.team4.hospital_system.model.Order;
import com.team4.hospital_system.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByPatientIdAndStatus(Long patientId, OrderStatus status);
    Optional<Order> findByPatientIdAndPharmacyIdAndStatus(Long patientId, Long pharmacyId, OrderStatus status);


}
