
package com.cogent.cogentappointment.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 2019-10-14
 */
@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*eg.Doctor name like Dr.Sanjeev Uprety*/
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id")
    private Specialization specializationId;

    /*eg.Doctor name like Dr.Sanjeev Uprety*/
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctorId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patientId;

    @Temporal(TemporalType.DATE)
    @Column(name = "appointment_date")
    private Date appointmentDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appointment_time")
    private Date appointmentTime;

    @Column(name = "appointment_number", updatable = false)
    private String appointmentNumber;

    @Column(name = "unique_id")
    private String uniqueId;
    @Column(name = "created_date_nepali")
    private String createdDateNepali;

    /* /*I = Incomplete
     * C = Cancel
     * S= Success
     */
    @Column(name = "status")
    private String status;

    /*If cancel the appointment, cancellation remarks is must*/
    @Column(name = "remarks")
    private String remarks;
}
