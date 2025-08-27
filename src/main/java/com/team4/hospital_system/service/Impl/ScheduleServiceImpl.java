package com.team4.hospital_system.service.Impl;

import com.team4.hospital_system.dto.response.DoctorScheduleDto;
import com.team4.hospital_system.exception.ResourceNotFoundException;
import com.team4.hospital_system.model.Appointment;
import com.team4.hospital_system.model.Doctor;
import com.team4.hospital_system.model.User;
import com.team4.hospital_system.repository.AppointmentRepository;
import com.team4.hospital_system.repository.UserRepository;
import com.team4.hospital_system.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ScheduleServiceImpl implements ScheduleService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public ScheduleServiceImpl(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DoctorScheduleDto getDoctorSchedule(long doctorId) {
        User user = userRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + doctorId + " not found"));
        if (!(user instanceof Doctor)) {
            throw new ResourceNotFoundException("User with id " + doctorId + " is not a doctor");
        }
        Doctor doctor = (Doctor) user;
        String doctorName = doctor.getName();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAhead = now.plusDays(7);

        List<Appointment> appts = appointmentRepository.findDoctorOverlaps(doctorId, now, weekAhead);

        List<DoctorScheduleDto.SlotDto> slots = appts.stream()
                .map(a -> new DoctorScheduleDto.SlotDto(a.getStartTime(), a.getStatus().name()))
                .collect(Collectors.toList());

        return new DoctorScheduleDto(doctorId, doctorName, slots);
    }
}
