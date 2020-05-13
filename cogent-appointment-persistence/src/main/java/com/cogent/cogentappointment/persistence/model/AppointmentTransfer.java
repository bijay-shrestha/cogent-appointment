package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentTransferEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/6/20
 * TO TRACK PREVIOUS APPOINTMENT DETAILS
 * i.e doctorId,SpecializatoinId,AppointmentDate,AppointmentTime
 */
@Entity
@Table(name = "appointment_transfer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentTransferEntityListener.class)
public class AppointmentTransfer extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_doctor_id", updatable = false)
    private Doctor previousDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_doctor_id", updatable = false)
    private Doctor currentDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_specialization_id", updatable = false)
    private Specialization previousSpecialization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_specialization_id", updatable = false)
    private Specialization currentSpecialization;

    /*saved in format YYYY-MM-DD*/
    @Temporal(TemporalType.DATE)
    @Column(name = "previous_appointment_date", updatable = false)
    private Date previousAppointmentDate;

    /*saved in format YYYY-MM-DD*/
    @Temporal(TemporalType.DATE)
    @Column(name = "current_appointment_date", updatable = false)
    private Date currentAppointmentDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "previous_appointment_time", updatable = false)
    private Date previousAppointmentTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "current_appointment_time", updatable = false)
    private Date currentAppointmentTime;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "AppointmentTransfer{" +
                "id=" + id +
                ", appointment=" + appointment.getAppointmentNumber() +
                ", previousDoctor=" + previousDoctor.getName() +
                ", currentDoctor=" + currentDoctor.getName() +
                ", previousSpecialization=" + previousSpecialization.getName() +
                ", currentSpecialization=" + currentSpecialization.getName() +
                ", previousAppointmentDate=" + previousAppointmentDate +
                ", currentAppointmentDate=" + currentAppointmentDate +
                ", previousAppointmentTime=" + previousAppointmentTime +
                ", currentAppointmentTime=" + currentAppointmentTime +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
