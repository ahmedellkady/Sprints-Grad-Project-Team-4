package com.team4.hospital_system;

import com.team4.hospital_system.dto.request.CheckoutDto;
import com.team4.hospital_system.dto.response.OrderDto;
import com.team4.hospital_system.dto.response.OrderItemDto;
import com.team4.hospital_system.exception.BadRequestException;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.*;
import com.team4.hospital_system.model.enums.OrderStatus;
import com.team4.hospital_system.repository.OrderRepository;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.repository.PharmacyRepository;
import com.team4.hospital_system.service.Impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PharmacyRepository pharmacyRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Patient patient;
    private Pharmacy pharmacy;
    private User patientUser;
    private User pharmacyUser;
    private Medicine medicine;
    private Order cart;
    private OrderItem orderItem;
    private CheckoutDto checkoutDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test data
        patientUser = new User();
        patientUser.setId(1L);
        patientUser.setName("John Doe");
        
        pharmacyUser = new User();
        pharmacyUser.setId(2L);
        pharmacyUser.setName("City Pharmacy");
        
        patient = new Patient();
        patient.setId(1L);
        patient.setUser(patientUser);
        
        pharmacy = new Pharmacy();
        pharmacy.setId(1L);
        pharmacy.setUser(pharmacyUser);
        pharmacy.setName("City Pharmacy");
        
        medicine = new Medicine();
        medicine.setId(1L);
        medicine.setName("Aspirin");
        medicine.setPrice(10);
        medicine.setStockQty(100);
        medicine.setPharmacy(pharmacy);
        
        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setMedicine(medicine);
        orderItem.setQuantity(5);
        orderItem.setPriceAtPurchase(BigDecimal.valueOf(10));
        
        cart = new Order();
        cart.setId(1L);
        cart.setPatient(patient);
        cart.setPharmacy(pharmacy);
        cart.setStatus(OrderStatus.CART);
        cart.setTotalAmount(BigDecimal.valueOf(50));
        cart.setCreatedAt(LocalDateTime.now());
        cart.setOrderItems(new HashSet<>(Arrays.asList(orderItem)));
        
        checkoutDto = new CheckoutDto(1L, "123 Main St", "Credit Card", "Please deliver in the morning");
    }

    @Test
    void testCheckout_Success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(orderRepository.findByPatientIdAndPharmacyIdAndStatus(1L, 1L, OrderStatus.CART))
                .thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(cart);

        OrderDto result = orderService.checkout(1L, checkoutDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getPatientId());
        assertEquals("John Doe", result.getPatientName());
        assertEquals(1L, result.getPharmacyId());
        assertEquals("City Pharmacy", result.getPharmacyName());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(BigDecimal.valueOf(50), result.getTotalAmount());
        assertEquals(1, result.getItems().size());
        
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCheckout_PatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.checkout(1L, checkoutDto);
        });
        
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testCheckout_PharmacyNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.checkout(1L, checkoutDto);
        });
        
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testCheckout_CartNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(orderRepository.findByPatientIdAndPharmacyIdAndStatus(1L, 1L, OrderStatus.CART))
                .thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> {
            orderService.checkout(1L, checkoutDto);
        });
        
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testCheckout_EmptyCart() {
        cart.setOrderItems(new HashSet<>());
        
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(orderRepository.findByPatientIdAndPharmacyIdAndStatus(1L, 1L, OrderStatus.CART))
                .thenReturn(Optional.of(cart));

        assertThrows(BadRequestException.class, () -> {
            orderService.checkout(1L, checkoutDto);
        });
        
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testCheckout_InsufficientStock() {
        orderItem.setQuantity(150); // More than available stock (100)
        
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(orderRepository.findByPatientIdAndPharmacyIdAndStatus(1L, 1L, OrderStatus.CART))
                .thenReturn(Optional.of(cart));

        assertThrows(BadRequestException.class, () -> {
            orderService.checkout(1L, checkoutDto);
        });
        
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testGetById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(cart));

        OrderDto result = orderService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getPatientId());
        assertEquals("John Doe", result.getPatientName());
        assertEquals(1L, result.getPharmacyId());
        assertEquals("City Pharmacy", result.getPharmacyName());
    }

    @Test
    void testGetById_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.getById(1L);
        });
    }

    @Test
    void testListForPatient_Success() {
        Order completedOrder = new Order();
        completedOrder.setId(2L);
        completedOrder.setPatient(patient);
        completedOrder.setPharmacy(pharmacy);
        completedOrder.setStatus(OrderStatus.COMPLETED);
        completedOrder.setTotalAmount(BigDecimal.valueOf(30));
        completedOrder.setCreatedAt(LocalDateTime.now());
        completedOrder.setOrderItems(new HashSet<>(Arrays.asList(orderItem)));

        List<Order> orders = Arrays.asList(completedOrder);
        
        when(orderRepository.findByPatientIdAndStatusNot(1L, OrderStatus.CART))
                .thenReturn(orders);

        List<OrderDto> result = orderService.listForPatient(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals(OrderStatus.COMPLETED, result.get(0).getStatus());
    }
}
