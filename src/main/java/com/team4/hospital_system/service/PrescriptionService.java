package com.team4.hospital_system.service;

import java.util.List;

import com.team4.hospital_system.dto.request.AddPrescriptionItemDto;
import com.team4.hospital_system.dto.request.CreatePrescriptionDto;
import com.team4.hospital_system.dto.response.PrescriptionDto;

public interface PrescriptionService {
    PrescriptionDto create(long doctorId, long patientId, CreatePrescriptionDto request);
    PrescriptionDto addItem(long doctorId, long prescriptionId, AddPrescriptionItemDto request);
    List<PrescriptionDto> listForPatient(long patientId);
    PrescriptionDto getById(long prescriptionId);
}
