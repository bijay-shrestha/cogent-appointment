package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/*
 * @author smriti on 07/06/20
  * THIS TABLE REFLECTS THE RELATION BETWEEN APPOINTMENT AND ITS CORRESPONDING FOLLOW UP APPOINTMENTS
 * ONE APPOINTMENT (parentAppointmentId) CAN HAVE ITS MULTIPLE FOLLOW UP APPOINTMENTS (followUpAppointmentId)
 *
 * SAVED WHILE TAKING AN APPOINTMENT ONLY IF IT IS A FOLLOW UP APPOINTMENT
 */
@Entity
@Table(name = "appointment_hospital_department_follow_up_log")
@Getter
@Setter
public class AppointmentHospitalDepartmentFollowUpLog implements Serializable{
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
