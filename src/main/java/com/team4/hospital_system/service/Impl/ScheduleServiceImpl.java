
package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.response.DoctorScheduleDto;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Appointment;
import com.team4.hospital_system.model.Doctor;
import com.team4.hospital_system.model.User;
import com.team4.hospital_system.repository.AppointmentRepository;
import com.team4.hospital_system.repository.DoctorRepository;
import com.team4.hospital_system.repository.UserRepository;
import com.team4.hospital_system.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public DoctorScheduleDto getDoctorSchedule(long doctorId) {
        // 1. Fetch the Doctor entity directly from its repository.
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + doctorId + " not found"));

        // 2. Get the doctor's name from the associated User object.
        String doctorName = doctor.getUser().getName();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAhead = now.plusDays(7);

        // 3. Find appointments using the doctorId.
        List<Appointment> appts = appointmentRepository.findDoctorOverlaps(doctorId, now, weekAhead);

        List<DoctorScheduleDto.SlotDto> slots = appts.stream()
                .map(a -> new DoctorScheduleDto.SlotDto(a.getStartTime(), a.getStatus().name()))
                .collect(Collectors.toList());

        return new DoctorScheduleDto(doctorId, doctorName, slots);
    }
}

