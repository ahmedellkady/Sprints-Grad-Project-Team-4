package com.team4.hospital_system.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class DoctorScheduleDto {
    private long doctorId;
    private String doctorName;
    private List<SlotDto> slots;

    public DoctorScheduleDto(long doctorId, String doctorName, List<SlotDto> slots) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.slots = slots;
    }

    public long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public List<SlotDto> getSlots() { return slots; }

    public static class SlotDto {
        private LocalDateTime start;
        private String status; // BOOKED / AVAILABLE

        public SlotDto(LocalDateTime start, String status) {
            this.start = start;
            this.status = status;
        }

        public LocalDateTime getStart() { return start; }
        public String getStatus() { return status; }
    }
}
