package com.team4.hospital_system.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class PrescriptionDto {
    public static class ItemDto {
        private Long id;
        private Long medicineId;
        private String medicineName;
        private String dosage;
        private String instructions;
        public ItemDto() {}
        public ItemDto(Long id, Long medicineId, String medicineName, String dosage, String instructions) {
            this.id = id; this.medicineId = medicineId; this.medicineName = medicineName; this.dosage = dosage; this.instructions = instructions;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getMedicineId() { return medicineId; }
        public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }
        public String getMedicineName() { return medicineName; }
        public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
        public String getDosage() { return dosage; }
        public void setDosage(String dosage) { this.dosage = dosage; }
        public String getInstructions() { return instructions; }
        public void setInstructions(String instructions) { this.instructions = instructions; }
    }

    private Long id;
    private Long doctorId;
    private Long patientId;
    private LocalDateTime issuedAt;
    private String notes;
    private List<ItemDto> items;

    public PrescriptionDto() {}
    public PrescriptionDto(Long id, Long doctorId, Long patientId, LocalDateTime issuedAt, String notes, List<ItemDto> items) {
        this.id = id; this.doctorId = doctorId; this.patientId = patientId; this.issuedAt = issuedAt; this.notes = notes; this.items = items;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<ItemDto> getItems() { return items; }
    public void setItems(List<ItemDto> items) { this.items = items; }
}
