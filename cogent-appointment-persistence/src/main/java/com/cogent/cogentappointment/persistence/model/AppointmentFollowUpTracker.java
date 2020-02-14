package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentFollowUpTrackerEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 12/02/2020
 */
@Entity
@Table(name = "appointment_follow_up_tracker")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentFollowUpTrackerEntityListener.class)
public class AppointmentFollowUpTracker extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id")
    private Specialization specializationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospitalId;

    @Column(name = "parent_appointment_id")
    private Long parentAppointmentId;

    @Column(name = "parent_appointment_number")
    private String parentAppointmentNumber;

    @Column(name = "remaining_number_of_follow_ups")
    private Integer remainingNumberOfFollowUps;

    @Temporal(TemporalType.DATE)
    @Column(name = "appointment_approved_date")
    private Date appointmentApprovedDate;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "AppointmentFollowUpTracker{" +
                " id=" + id +
                ", patientId=" + patientId.getName() +
                ", doctorId=" + doctorId.getName() +
                ", specializationId=" + specializationId.getName() +
                ", hospitalId=" + hospitalId.getName() +
                ", parentAppointmentId=" + parentAppointmentId +
                ", parentAppointmentNumber=" + parentAppointmentNumber +
                ", remainingNumberOfFollowUps=" + remainingNumberOfFollowUps +
                ", appointmentApprovedDate=" + appointmentApprovedDate +
                ", status=" + status +
                '}';
    }
}
