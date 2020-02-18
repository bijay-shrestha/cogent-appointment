
package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentReservationEntityListener;
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
@Table(name = "appointment_reservation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentReservationEntityListener.class)
public class AppointmentReservation extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospitalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id")
    private Specialization specializationId;

    @Temporal(TemporalType.DATE)
    @Column(name = "reserved_appointment_date")
    private Date reservedAppointmentDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reserved_appointment_time")
    private Date reservedAppointmentTime;

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id + ", " +
                "specializationId=" + specializationId.getName() +
                ", doctorId=" + doctorId.getName() +
                ", hospital=" + hospitalId.getName() +
                ", reservedAppointmentDate=" + reservedAppointmentDate +
                ", reservedAppointmentTime=" + reservedAppointmentTime +
                '}';
    }
}
