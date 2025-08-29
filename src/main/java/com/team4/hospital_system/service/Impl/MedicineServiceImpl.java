package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.request.CreateMedicineDto;
import com.team4.hospital_system.dto.request.MedicineSearchDto;
import com.team4.hospital_system.dto.request.UpdateMedicineDto;
import com.team4.hospital_system.dto.response.MedicineDto;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Medicine;
import com.team4.hospital_system.model.Pharmacy;
import com.team4.hospital_system.repository.MedicineRepository;
import com.team4.hospital_system.repository.PharmacyRepository;
import com.team4.hospital_system.service.MedicineService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final PharmacyRepository pharmacyRepository;

    public MedicineServiceImpl(MedicineRepository medicineRepository,
                               PharmacyRepository pharmacyRepository) {
        this.medicineRepository = medicineRepository;
        this.pharmacyRepository = pharmacyRepository;
    }

    @Override
    public MedicineDto create(long pharmacyId, CreateMedicineDto request) {
        Pharmacy pharmacy = findPharmacy(pharmacyId);
        Medicine m = new Medicine();
        m.setName(request.getName());
        m.setDescription(request.getDescription());
        m.setExpirationDate(toDateTime(request.getExpirationDate()));
        m.setManufacturingDate(toDateTime(request.getManufacturingDate()));
        m.setPrice(request.getPrice());
        m.setStockQty(request.getStockQty());
        m.setPharmacy(pharmacy);
        m = medicineRepository.save(m);
        return toDto(m);
    }

    @Override
    public MedicineDto update(long pharmacyId, long medicineId, UpdateMedicineDto request) {
        Pharmacy pharmacy = findPharmacy(pharmacyId);
        Medicine m = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + medicineId));
        if (!m.getPharmacy().getId().equals(pharmacy.getId())) {
            throw new ResourceNotFoundException("Medicine not found in this pharmacy: " + medicineId);
        }
        if (request.getName() != null) m.setName(request.getName());
        if (request.getDescription() != null) m.setDescription(request.getDescription());
        if (request.getExpirationDate() != null) m.setExpirationDate(toDateTime(request.getExpirationDate()));
        if (request.getManufacturingDate() != null) m.setManufacturingDate(toDateTime(request.getManufacturingDate()));
        if (request.getPrice() != null) m.setPrice(request.getPrice());
        if (request.getStockQty() != null) m.setStockQty(request.getStockQty());
        m = medicineRepository.save(m);
        return toDto(m);
    }

    @Override
    public void delete(long pharmacyId, long medicineId) {
        Pharmacy pharmacy = findPharmacy(pharmacyId);
        Medicine m = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + medicineId));
        if (!m.getPharmacy().getId().equals(pharmacy.getId())) {
            throw new ResourceNotFoundException("Medicine not found in this pharmacy: " + medicineId);
        }
        medicineRepository.delete(m);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineDto getById(long medicineId) {
        Medicine m = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + medicineId));
        return toDto(m);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineDto> listAll(int page, int size) {
        return medicineRepository.findAll(PageRequest.of(page, size))
                .stream().map(MedicineServiceImpl::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineDto> search(MedicineSearchDto request, int page, int size) {
        Specification<Medicine> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getName() != null && !request.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));
            }
            if (request.getMinPrice() != null) {
                predicates.add(cb.ge(root.get("price"), request.getMinPrice()));
            }
            if (request.getMaxPrice() != null) {
                predicates.add(cb.le(root.get("price"), request.getMaxPrice()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return medicineRepository.findAll(spec, PageRequest.of(page, size))
                .stream().map(MedicineServiceImpl::toDto).toList();
    }

    private static LocalDateTime toDateTime(LocalDate date) {
        return date == null ? null : date.atStartOfDay();
    }

    private static MedicineDto toDto(Medicine m) {
        return new MedicineDto(
                m.getId(),
                m.getName(),
                m.getDescription(),
                m.getExpirationDate() == null ? null : m.getExpirationDate().toLocalDate(),
                m.getManufacturingDate() == null ? null : m.getManufacturingDate().toLocalDate(),
                m.getPrice(),
                m.getStockQty(),
                m.getPharmacy() == null ? null : m.getPharmacy().getId()
        );
    }

    private Pharmacy findPharmacy(long pharmacyId) {
        return pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacy not found: " + pharmacyId));
    }
}


