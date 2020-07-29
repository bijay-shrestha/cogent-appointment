package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/*
 * @author smriti on 28/05/20
 */
@Entity
@Table(name = "appointment_doctor_info")
@Getter
@Setter
public class AppointmentDoctorInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    /*eg.Specialization name like Surgeon, Physician,etc*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    /*eg.Doctor name like Dr.Sanjeev Uprety*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
