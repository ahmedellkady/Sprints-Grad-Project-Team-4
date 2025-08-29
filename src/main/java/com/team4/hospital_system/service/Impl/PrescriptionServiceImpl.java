package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.request.AddPrescriptionItemDto;
import com.team4.hospital_system.dto.request.CreatePrescriptionDto;
import com.team4.hospital_system.dto.response.PrescriptionDto;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.*;
import com.team4.hospital_system.repository.*;
import com.team4.hospital_system.service.PrescriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository,
                                   DoctorRepository doctorRepository,
                                   PatientRepository patientRepository,
                                   MedicineRepository medicineRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.medicineRepository = medicineRepository;
    }

    @Override
    public PrescriptionDto create(long doctorId, long patientId, CreatePrescriptionDto request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorId));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));
        Prescription p = new Prescription();
        p.setDoctor(doctor);
        p.setPatient(patient);
        p.setIssuedAt(LocalDateTime.now());
        p = prescriptionRepository.save(p);
        return toDto(p);
    }

    @Override
    public PrescriptionDto addItem(long doctorId, long prescriptionId, AddPrescriptionItemDto request) {
        Prescription p = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found: " + prescriptionId));
        if (!p.getDoctor().getId().equals(doctorId)) {
            throw new ResourceNotFoundException("Prescription not found for this doctor: " + prescriptionId);
        }
        Medicine med = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + request.getMedicineId()));
        PrescriptionItem item = new PrescriptionItem();
        item.setPrescription(p);
        item.setMedicine(med);
        item.setDosage(request.getDosage());
        item.setInstructions(request.getInstructions());
        if (p.getPrescriptionItems() == null) {
            p.setPrescriptionItems(new java.util.ArrayList<>());
        }
        p.getPrescriptionItems().add(item);
        p = prescriptionRepository.save(p);
        return toDto(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionDto> listForPatient(long patientId) {
        return prescriptionRepository.findByPatientId(patientId).stream()
                .map(PrescriptionServiceImpl::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionDto getById(long prescriptionId) {
        Prescription p = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found: " + prescriptionId));
        return toDto(p);
    }

    private static PrescriptionDto toDto(Prescription p) {
        List<PrescriptionDto.ItemDto> items = p.getPrescriptionItems() == null ? List.of() :
                p.getPrescriptionItems().stream().map(i ->
                        new PrescriptionDto.ItemDto(
                                i.getId(),
                                i.getMedicine().getId(),
                                i.getMedicine().getName(),
                                i.getDosage(),
                                i.getInstructions()
                        )
                ).collect(Collectors.toList());
        return new PrescriptionDto(
                p.getId(),
                p.getDoctor() == null ? null : p.getDoctor().getId(),
                p.getPatient() == null ? null : p.getPatient().getId(),
                p.getIssuedAt(),
                null,
                items
        );
    }
}


