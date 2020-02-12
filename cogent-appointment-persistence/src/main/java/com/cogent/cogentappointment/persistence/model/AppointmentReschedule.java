package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentRescheduleEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Entity
@Table(name = "appointment_reschedule")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentRescheduleEntityListener.class)
public class AppointmentReschedule extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointmentId;

    @Temporal(TemporalType.DATE)
    @Column(name = "rescheduled_date")
    private Date rescheduledDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "previous_appointment_date")
    private Date previousAppointmentDate;


    @Column(name = "remarks")
    private String remarks;

    /**
     * RES - Rescheduled
     */
    @Column(name = "status")
    private String status;


    @Override
    public String toString() {
        return "AppointmentReschedule{" +
                "id=" + id +
                ", appointmentId=" + appointmentId +
                ", rescheduledDate=" + rescheduledDate +
                ", previousAppointmentDate=" + previousAppointmentDate +
                ", remarks='" + remarks + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
