package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.request.CartItemRequest;
import com.team4.hospital_system.dto.request.CheckoutDto;
import com.team4.hospital_system.dto.response.CartDto;
import com.team4.hospital_system.dto.response.OrderDto;
import com.team4.hospital_system.model.Order;
import com.team4.hospital_system.model.Pharmacy;
import com.team4.hospital_system.model.enums.OrderStatus;
import com.team4.hospital_system.repository.OrderRepository;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.repository.PharmacyRepository;
import com.team4.hospital_system.service.CartService;
import com.team4.hospital_system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
class CartContoller {
    private final CartService cartService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final PharmacyRepository pharmacyRepository;
    private final PatientRepository patientRepository;



    @GetMapping("/{patientId}/{pharmacyId}")
    public ResponseEntity<CartDto> getCart(
            @PathVariable long patientId,
            @PathVariable long pharmacyId) {
        CartDto cart = cartService.getCart(patientId, pharmacyId);
        return ResponseEntity.ok(cart);
    }



    @PostMapping("/{patientId}/add")
    public ResponseEntity<CartDto> addItem(
            @PathVariable long patientId,
            @RequestBody CartItemRequest request) {
        CartDto updatedCart = cartService.addItem(
                patientId,
                request.getMedicineId(),
                request.getQuantity(),
                request.getPharmacyId()
        );
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{patientId}/remove")
    public ResponseEntity<CartDto> removeItem(
            @PathVariable long patientId,
            @RequestBody CartItemRequest request) {
        CartDto updatedCart = cartService.removeItem(
                patientId,
                request.getMedicineId(),
                request.getPharmacyId()
        );
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{patientId}/clear")
    public ResponseEntity<CartDto> clearCart(@PathVariable long patientId, @RequestBody CartItemRequest request) {
        CartDto clearedCart = cartService.clear(
                patientId,
                request.getPharmacyId()
        );
        return ResponseEntity.ok(clearedCart);
    }

    // === Checkout endpoint ===
    @PostMapping("/{patientId}/checkout")
    public ResponseEntity<OrderDto> checkout(
            @PathVariable long patientId,
            @RequestBody CheckoutDto request) {
        OrderDto order = orderService.checkout(patientId, request);
        return ResponseEntity.ok(order);
    }

    // === Get patient orders ===
    @GetMapping("/{patientId}/orders")
    public ResponseEntity<List<OrderDto>> getPatientOrders(@PathVariable long patientId) {
        List<OrderDto> orders = orderService.listForPatient(patientId);
        return ResponseEntity.ok(orders);
    }

    // === Get specific order ===
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable long orderId) {
        OrderDto order = orderService.getById(orderId);
        return ResponseEntity.ok(order);
    }
}
