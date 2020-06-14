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
 * <p>
 * <p>
 * Connected Table
 * 1. HospitalAppointmentService Type
 * Appointment Service Type can be
 * a. Doctor Consultation (AppointmentDoctorInfo)
 * i. Doctor
 * ii. Specialization
 * <p>
 * b. Department Consultation (AppointmentHospitalDepartmentInfo)
 * i. HospitalDepartment
 * ii. HospitalDepartmentRoomInfo
 * iii. HospitalDepartmentBillingModeInfo
 * <p>
 * 2. AppointmentMode
 * 3. AppointmentTransactionDetail
 * 4. AppointmentFollowUpTracker/AppointmentHospitalDepartmentFollowUpTracker
 * 5. AppointmentFollowUpRequestLog/AppointmentHospitalDepartmentFollowUpRequestLog
 * 6. AppointmentFollowUpLog/AppointmentHospitalDepartmentFollowUpLog
 * 7. AppointmentReservationLog/AppointmentHospitalDepartmentReservationLog
 * 8. AppointmentStatistics
 * <p>
 * RESCHEDULE -> AppointmentRescheduleLog
 * REFUND -> AppointmentRefundDetail
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_appointment_service_type_id")
    private HospitalAppointmentServiceType hospitalAppointmentServiceType;

    //todo : remove doctor and specialization from here
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

    /* If Appointment has been transferred - Y
     * Else N */
    @Column(name = "has_transferred")
    private Character hasTransferred = 'N';

    @Column(name = "appointment_date_in_nepali")
    private String appointmentDateInNepali;

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patientId=" + patientId.getName() +
                ", specializationId=" + specializationId.getName() +
                ", doctorId=" + doctorId +
                ", patientId=" + patientId +
                ", appointmentModeId=" + appointmentModeId.getName() +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", appointmentNumber='" + appointmentNumber + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", createdDateNepali='" + createdDateNepali + '\'' +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                ", hospitalId=" + hospitalId +
                ", isFollowUp=" + isFollowUp +
                ", isSelf=" + isSelf +
                ", hasTransferred=" + hasTransferred +
                ", appointmentDateInNepali='" + appointmentDateInNepali + '\'' +
                '}';
    }
}
