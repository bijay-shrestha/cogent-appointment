package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author smriti ON 19/02/2020
 */
@Entity
@Table(name = "appointment_reservation_log")
@Getter
@Setter
public class AppointmentReservationLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hospital_id")
    private Long hospitalId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "specialization_id")
    private Long specializationId;

    @Temporal(TemporalType.DATE)
    @Column(name = "appointment_date")
    private Date appointmentDate;

    @Temporal(TIMESTAMP)
    @Column(name = "appointment_time")
    private Date appointmentTime;

    @Temporal(TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;
}
