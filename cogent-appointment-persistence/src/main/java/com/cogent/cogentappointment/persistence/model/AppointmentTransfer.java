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
 */
@Entity
@Table(name = "appointment_transfer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentTransferEntityListener.class)
public class AppointmentTransfer extends Auditable<String> implements Serializable {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "previous_doctor_id", updatable = false)
    private Long previousDoctorId;

    @Column(name = "previous_specialization_id", updatable = false)
    private Long previousSpecializationId;

    /*saved in format YYYY-MM-DD*/
    @Temporal(TemporalType.DATE)
    @Column(name = "previous_appointment_date", updatable = false)
    private Date previousAppointmentDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "previous_appointment_time", updatable = false)
    private Date previousAppointmentTime;

    @Column(name = "remarks")
    private String remarks;
}
