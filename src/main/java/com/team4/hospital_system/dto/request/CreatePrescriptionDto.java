package com.team4.hospital_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class CreatePrescriptionDto {
    @NotBlank
    @Length(max = 255)
    private String notes; // optional overall notes; can be empty but keep field

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
