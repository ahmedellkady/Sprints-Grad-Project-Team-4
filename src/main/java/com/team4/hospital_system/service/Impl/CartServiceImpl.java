package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.response.CartDto;
import com.team4.hospital_system.dto.response.CartItemDto;
import com.team4.hospital_system.model.Medicine;
import com.team4.hospital_system.model.Order;
import com.team4.hospital_system.model.OrderItem;
import com.team4.hospital_system.model.enums.OrderStatus;
import com.team4.hospital_system.repository.MedicineRepository;
import com.team4.hospital_system.repository.OrderRepository;
import com.team4.hospital_system.repository.PatientRepository;
import com.team4.hospital_system.repository.PharmacyRepository;
import com.team4.hospital_system.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final OrderRepository orderRepository;
    private final MedicineRepository medicineRepository;
    private final PatientRepository patientRepository;
    private final PharmacyRepository pharmacyRepository;


    @Override
    public CartDto getCart(long patientId, long pharmacyId) {
        Order cart = getOrCreateCart(patientId , pharmacyId);
        return toCartDto(cart);
    }


    @Override
    public CartDto addItem(long patientId, long medicineId, int quantity, long pharmacyId) {
        Order cart = getOrCreateCart(patientId ,pharmacyId);

        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        if (cart.getPharmacy() == null) {
            cart.setPharmacy(medicine.getPharmacy());
        }

        OrderItem item = cart.getOrderItems().stream()
                .filter(i -> i.getMedicine().getId().equals(medicineId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = new OrderItem();
            item.setOrder(cart);
            item.setMedicine(medicine);
            item.setQuantity(quantity);
            item.setPriceAtPurchase(BigDecimal.valueOf(medicine.getPrice()));
            cart.getOrderItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }

        updateTotalAmount(cart);
        orderRepository.save(cart);

        return toCartDto(cart);
    }




    @Override
    public CartDto removeItem(long patientId, long medicineId,long pharmacyId) {
        Order cart = getOrCreateCart(patientId, pharmacyId);
        cart.getOrderItems().removeIf(i -> i.getMedicine().getId().equals(medicineId));

        updateTotalAmount(cart);
        orderRepository.save(cart);

        return toCartDto(cart);
    }


    @Override
    public CartDto clear(long patientId,long pharmacyId) {
        Order cart = getOrCreateCart(patientId, pharmacyId);
        cart.getOrderItems().clear();
        updateTotalAmount(cart);
        orderRepository.save(cart);

        return toCartDto(cart);
    }







    private Order getOrCreateCart(long patientId, long pharmacyId) {
        return orderRepository.findByPatientIdAndPharmacyIdAndStatus(patientId, pharmacyId, OrderStatus.CART)
                .orElseGet(() -> {
                    Order newCart = new Order();
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setStatus(OrderStatus.CART);
                    newCart.setPatient(patientRepository.findById(patientId)
                            .orElseThrow(() -> new RuntimeException("Patient not found")));
                    newCart.setPharmacy(pharmacyRepository.findById(pharmacyId)
                            .orElseThrow(() -> new RuntimeException("Pharmacy not found")));
                    newCart.setOrderItems(new HashSet<>());
                    newCart.setTotalAmount(BigDecimal.ZERO);
                    return orderRepository.save(newCart);
                });
    }




    private void updateTotalAmount(Order order) {
        BigDecimal total = order.getOrderItems().stream()
                .map(i -> i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);
    }


    private CartDto toCartDto(Order order) {
        CartDto dto = new CartDto();
        dto.setCartId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());

        List<CartItemDto> items = order.getOrderItems().stream().map(i -> {
            CartItemDto itemDto = new CartItemDto();
            itemDto.setMedicineId(i.getMedicine().getId());
            itemDto.setMedicineName(i.getMedicine().getName());
            itemDto.setQuantity(i.getQuantity());
            itemDto.setPrice(i.getPriceAtPurchase());
            return itemDto;
        }).toList();

        dto.setItems(items);
        return dto;
    }

}
