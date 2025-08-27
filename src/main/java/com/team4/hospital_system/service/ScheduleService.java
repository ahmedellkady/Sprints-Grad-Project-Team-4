package com.team4.hospital_system.service;


import com.team4.hospital_system.dto.response.DoctorScheduleDto;

public interface ScheduleService {
    DoctorScheduleDto getDoctorSchedule(long doctorId);
}
