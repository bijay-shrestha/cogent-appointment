package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentFollowUpLogEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti ON 12/02/2020
 */
@Entity
@Table(name = "appointment_follow_up_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentFollowUpLogEntityListener.class)
public class AppointmentFollowUpLog extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_appointment_id")
    private Long parentAppointmentId;

    @Column(name = "follow_up_appointment_id")
    private Long followUpAppointmentId;

    @Override
    public String toString() {
        return "AppointmentFollowUpLog{" +
                " id=" + id +
                ", parentAppointmentId=" + parentAppointmentId +
                ", followUpAppointmentId=" + followUpAppointmentId +
                '}';
    }
}
