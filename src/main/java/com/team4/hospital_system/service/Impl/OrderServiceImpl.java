package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.request.CheckoutDto;
import com.team4.hospital_system.dto.response.OrderDto;
import com.team4.hospital_system.dto.response.OrderItemDto;
import com.team4.hospital_system.exception.BadRequestException;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Order;
import com.team4.hospital_system.model.OrderItem;
import com.team4.hospital_system.model.Patient;
import com.team4.hospital_system.model.Pharmacy;
import com.team4.hospital_system.model.enums.OrderStatus;
import com.team4.hospital_system.repository.OrderRepository;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.repository.PharmacyRepository;
import com.team4.hospital_system.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PatientRepository patientRepository;
    private final PharmacyRepository pharmacyRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                           PatientRepository patientRepository,
                           PharmacyRepository pharmacyRepository) {
        this.orderRepository = orderRepository;
        this.patientRepository = patientRepository;
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    public OrderDto checkout(long patientId, CheckoutDto request) {
        // Find the patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));

        // Find the pharmacy
        Pharmacy pharmacy = pharmacyRepository.findById(request.getPharmacyId())
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found: " + request.getPharmacyId()));

        // Find the cart (order with CART status)
        Order cart = orderRepository.findByPatientIdAndPharmacyIdAndStatus(patientId, request.getPharmacyId(), OrderStatus.CART)
                .orElseThrow(() -> new BadRequestException("No cart found for patient: " + patientId + " and pharmacy: " + request.getPharmacyId()));

        // Validate cart has items
        if (cart.getOrderItems() == null || cart.getOrderItems().isEmpty()) {
            throw new BadRequestException("Cannot checkout empty cart");
        }

        // Validate stock availability
        validateStockAvailability(cart);

        // Update order status to PENDING
        cart.setStatus(OrderStatus.PENDING);
        
        // Add checkout information
        cart.setCreatedAt(LocalDateTime.now()); // Update creation time for actual order
        
        // Save the order
        Order savedOrder = orderRepository.save(cart);

        return toOrderDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getById(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        return toOrderDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> listForPatient(long patientId) {
        return orderRepository.findByPatientIdAndStatusNot(patientId, OrderStatus.CART)
                .stream()
                .map(this::toOrderDto)
                .collect(Collectors.toList());
    }

    private void validateStockAvailability(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            if (item.getQuantity() > item.getMedicine().getStockQty()) {
                throw new BadRequestException("Insufficient stock for medicine: " + item.getMedicine().getName() + 
                        ". Available: " + item.getMedicine().getStockQty() + ", Requested: " + item.getQuantity());
            }
        }
    }

    private OrderDto toOrderDto(Order order) {
        List<OrderItemDto> itemDtos = order.getOrderItems().stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList());

        return new OrderDto(
                order.getId(),
                order.getPatient().getId(),
                order.getPatient().getUser().getName(),
                order.getPharmacy().getId(),
                order.getPharmacy().getName(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                null, // shippingAddress - not implemented in current model
                null, // paymentMethod - not implemented in current model
                null, // notes - not implemented in current model
                itemDtos
        );
    }

    private OrderItemDto toOrderItemDto(OrderItem item) {
        return new OrderItemDto(
                item.getId(),
                item.getMedicine().getId(),
                item.getMedicine().getName(),
                item.getQuantity(),
                item.getPriceAtPurchase()
        );
    }
}
