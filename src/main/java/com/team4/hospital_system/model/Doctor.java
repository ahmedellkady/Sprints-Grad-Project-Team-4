package com.team4.hospital_system.model;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Doctor extends User {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

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