package com.team4.hospital_system.model;




import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.team4.hospital_system.model.enums.AppointmentStatus;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_appointment_id")
    private Appointment prevAppointment;
}
