package com.team4.hospital_system.controller;

import com.team4.hospital_system.dto.request.CreateMedicineDto;
import com.team4.hospital_system.dto.request.MedicineSearchDto;
import com.team4.hospital_system.dto.request.UpdateMedicineDto;
import com.team4.hospital_system.dto.response.MedicineDto;
import com.team4.hospital_system.exception.BadRequestException;
import com.team4.hospital_system.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping("/pharmacies/{pharmacyId}")
    public ResponseEntity<MedicineDto> create(@PathVariable long pharmacyId,
                                              @Valid @RequestBody CreateMedicineDto request) {
        MedicineDto created = medicineService.create(pharmacyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/pharmacies/{pharmacyId}/{medicineId}")
    public ResponseEntity<MedicineDto> update(@PathVariable long pharmacyId,
                                              @PathVariable long medicineId,
                                              @Valid @RequestBody UpdateMedicineDto request) {
        return ResponseEntity.ok(medicineService.update(pharmacyId, medicineId, request));
    }

    @DeleteMapping("/pharmacies/{pharmacyId}/{medicineId}")
    public ResponseEntity<Void> delete(@PathVariable long pharmacyId,
                                       @PathVariable long medicineId) {
        medicineService.delete(pharmacyId, medicineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDto> getById(@PathVariable long id) {
        return ResponseEntity.ok(medicineService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MedicineDto>> list(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(medicineService.listAll(page, size));
    }

    @PostMapping("/search")
    public ResponseEntity<List<MedicineDto>> search(@Valid @RequestBody MedicineSearchDto request,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "20") int size) {
        if (request.getMinPrice() != null && request.getMaxPrice() != null
                && request.getMinPrice() > request.getMaxPrice()) {
            throw new BadRequestException("minPrice cannot be greater than maxPrice");
        }
        return ResponseEntity.ok(medicineService.search(request, page, size));
    }
}


