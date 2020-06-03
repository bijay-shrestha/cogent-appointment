package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author smriti ON 19/02/2020
 * <p>
 * WHEN USER SELECTS ANY AVAILABLE TIMESLOT,
 * THEN THE SELECTED TIMESLOT IS TEMPORARILY HOLD FOR CERTAIN TIME AND IS SAVED IN THIS TABLE.
 * <p>
 * AFTER SOME TIME, ROW IS DELETED RIGHT AWAY
 * FILTER AVAILABLE TIMESLOT LIST ON THE BASIS OF
 * DDR, EXISTING APPOINTMENTS AND APPOINTMENT RESERVATION
 * <p>
 * DELETION OPERATION IS DONE BY SCHEDULER OR
 * WHEN USER DOESN'T PROCEED APPOINTMENT WHILE SAVING PROCESS (CANCELS INSTEAD OF PROCEED)
 */
@Entity
@Table(name = "appointment_hospital_department_reservation_log")
@Getter
@Setter
public class AppointmentHospitalDepartmentReservationLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_id")
    private HospitalDepartment hospitalDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_billing_mode_info_id")
    private HospitalDepartmentBillingModeInfo hospitalDepartmentBillingModeInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_room_id")
    private HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo;

    /*(can be mobile number/esewa id that identifies the logged in user)*/
    @Column(name = "user_id")
    private String userId;

    @Temporal(TemporalType.DATE)
    @Column(name = "appointment_date")
    private Date appointmentDate;

    @Temporal(TIMESTAMP)
    @Column(name = "appointment_time")
    private Date appointmentTime;

    @Temporal(TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;
}
