
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

    /*eg.Specialization name like Surgeon, Physician,etc*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id")
    private Specialization specializationId;

    /*eg.Doctor name like Dr.Sanjeev Uprety*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patientId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_mode_id")
    private AppointmentMode appointmentModeId;

    /*saved in format YYYY-MM-DD*/
    @Temporal(TemporalType.DATE)
    @Column(name = "appointment_date")
    private Date appointmentDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appointment_time")
    private Date appointmentTime;

    @Column(name = "appointment_number", updatable = false)
    private String appointmentNumber;

    /*maintained to avoid duplicate row persist*/
    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "created_date_nepali")
    private String createdDateNepali;

    /* PA = PENDING APPROVAL
       A= VISITED AND APPROVED (CHECKED -IN)
       R= REJECTED BY HOSPITAL
       C = CANCELLED BUT NOT REFUNDED
       RE = CANCELLED AND REFUNDED
    */
    @Column(name = "status")
    private String status;

    /*If cancel the appointment, cancellation remarks is must*/
    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospitalId;

    /*Y - FOLLOW UP APPOINTMENT
    * N - NORMAL APPOINTMENT*/
    @Column(name = "is_follow_up")
    private Character isFollowUp;

    /*Y - MYSELF
    * N - OTHERS*/
    @Column(name = "is_self")
    private Character isSelf;

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", specializationId=" + specializationId.getName() +
                ", doctorId=" + doctorId.getName() +
                ", patientId=" + patientId.getName() +
                ", appointmentModeId=" + appointmentModeId.getName() +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", appointmentNumber='" + appointmentNumber + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", createdDateNepali='" + createdDateNepali + '\'' +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                ", hospitalId=" + hospitalId.getName() +
                ", isFollowUp=" + isFollowUp +
                ", isSelf=" + isSelf +
                '}';
    }
}
