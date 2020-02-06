
package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentEntityListener;
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
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentEntityListener.class)
public class Appointment extends Auditable<String> implements Serializable {

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Temporal(TemporalType.DATE)
    @Column(name = "appointment_date")
    private Date appointmentDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appointment_time")
    private Date appointmentTime;

    @Column(name = "appointment_number", updatable = false)
    private String appointmentNumber;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "created_date_nepali")
    private String createdDateNepali;

    /* PA = PENDING APPROVAL
    A= APPROVED
    R= REJECTED
    C = CANCELLED
     */
    @Column(name = "status")
    private String status;

    /*If cancel the appointment, cancellation remarks is must*/
    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospitalId;


    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", specializationId=" + specializationId.getName() +
                ", doctorId=" + doctorId.getName() +
                ", patientId=" + patientId.getName() +
                ", hospital=" + hospital.getName() +
                ", appointmentDate=" + appointmentDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", appointmentNumber='" + appointmentNumber + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                ", createdDateNepali='" + createdDateNepali + '\'' +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
