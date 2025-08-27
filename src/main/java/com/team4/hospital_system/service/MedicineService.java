package com.team4.hospital_system.service;

import java.util.List;

import com.team4.hospital_system.dto.request.CreateMedicineDto;
import com.team4.hospital_system.dto.request.MedicineSearchDto;
import com.team4.hospital_system.dto.request.UpdateMedicineDto;
import com.team4.hospital_system.dto.response.MedicineDto;

public interface MedicineService {
    MedicineDto create(long pharmacyId, CreateMedicineDto request);
    MedicineDto update(long pharmacyId, long medicineId, UpdateMedicineDto request);
    void delete(long pharmacyId, long medicineId);

    MedicineDto getById(long medicineId);
    List<MedicineDto> listAll(int page, int size);
    List<MedicineDto> search(MedicineSearchDto request, int page, int size);
}
