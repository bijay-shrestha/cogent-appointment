package com.cogent.cogentappointment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti ON 13/01/2020
 */
@Entity
@Table(name = "doctor_appointment_charge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAppointmentCharge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_charge")
    private Double appointmentCharge;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctorId")
    private Doctor doctorId;
}
