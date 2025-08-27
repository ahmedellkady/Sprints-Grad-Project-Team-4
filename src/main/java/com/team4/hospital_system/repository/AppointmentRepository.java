// package com.team4.hospital_system.repository;

// import com.team4.hospital_system.model.Appointment;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

// import java.time.LocalDateTime;
// import java.util.List;

// public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
//     List<Appointment> findByPatientId(Long patientId);

//     @Query("""
//         SELECT a FROM Appointment a
//         WHERE a.doctor.id = :doctorId
//           AND a.appointmentTime BETWEEN :start AND :end
//     """)
//     List<Appointment> findByDoctorIdAndAppointmentTimeBetween(@Param("doctorId") Long doctorId,
//                                                               @Param("start") LocalDateTime start,
//                                                               @Param("end") LocalDateTime end);

//     boolean existsByDoctorIdAndAppointmentTime(Long doctorId, LocalDateTime appointmentTime);
// }
