package com.team4.hospital_system.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor {
    
    @Id
    private Long id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String specialization;

    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(
        name = "doctor_working_days",
        joinColumns = @JoinColumn(name = "doctor_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "day_of_week"})
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 10)
    private Set<DayOfWeek> workingDays = EnumSet.noneOf(DayOfWeek.class);

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;
}
