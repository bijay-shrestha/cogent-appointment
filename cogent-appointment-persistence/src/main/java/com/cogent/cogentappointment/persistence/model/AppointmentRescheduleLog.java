package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentRescheduleLogEntityListener;
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
@Table(name = "appointment_reschedule_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentRescheduleLogEntityListener.class)
public class AppointmentRescheduleLog extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointmentId;

    @Temporal(TemporalType.DATE)
    @Column(name = "previous_appointment_date")
    private Date previousAppointmentDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "reschedule_date")
    private Date rescheduleDate;

    /*RES =RESCHEDULED*/
    @Column(name = "status")
    private String status;

    @Column(name = "remarks")
    private String remarks;
}
