package com.cogent.cogentappointment.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sauravi Thapa ON 6/19/20
 * This table stores the esewa id that was used to take an appointment.
 *
 *
 */
@Entity
@Table(name = "appointmentEsewaRequest")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentEsewaRequest implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "esewa_id", updatable = false)
    private String esewaId;

}
