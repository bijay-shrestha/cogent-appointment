package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 29/03/20
 *
 * TO TRACK NUMBER OF FOLLOW UPS REQUESTED FOR A SPECIFIC APPOINTMENT
 *
 * VALIDATE followUpRequestedCount < HOSPITAL ALLOWED NUMBER OF FOLLOW UPS
 * TRUE -> FOLLOW UP
 * FALSE -> NORMAL APPOINTMENT
 *
 * INITALLY SAVED WHEN USER CHECKS IN WHERE followUpRequestedCount = 0
 * AND INCREMENTS BY 1 WHEN USER TAKES ANOTHER CONSECUTIVE FOLLOW UP APPOINTMENT
 */
@Entity
@Table(name = "appointment_follow_up_request_log")
@Getter
@Setter
public class AppointmentFollowUpRequestLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_follow_up_tracker_id")
    private AppointmentFollowUpTracker appointmentFollowUpTracker;

    @Column(name = "follow_up_requested_count")
    private Integer followUpRequestedCount;
}
