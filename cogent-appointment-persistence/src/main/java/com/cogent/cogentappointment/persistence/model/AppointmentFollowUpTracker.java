package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 12/02/2020
 *
 * THIS TABLE REFLECTS THE REMAINING FOLLOW UPS FOR A SPECIFIC APPOINTMENT
 *
 * INITIALLY,
 * remainingNumberOfFollowUps = allowed number of follow ups for selected hospital (say 2)
 * appointmentApprovedDate = appointment approved date
 *
 *  IF PATIENT CHECK-IN,
 *  remainingNumberOfFollowUps = previous count -1 (2- 1 = 1)
 *
 * IF remainingNumberOfFollowUps > 0 AND
 * APPOINTMENT APPROVED DATE  + HOSPITAL FOLLOW UP INTERVAL DAYS > REQUESTED DATE
 *      THEN STATUS = 'Y'
 * ELSE
 *      STATUS = 'N'
 *
 * STATUS FLAG IS UPDATED BY SCHEDULER ALSO
 */

/* TO BE A FOLLOW UP APPOINTMENT:
     1. IF REMAINING NUMBER OF FOLLOW UPS IN AppointmentFollowUpTracker > 0
         (SUPPOSE INITIALLY IT IS 3(AS PER HOSPITAL). NOW WHEN USER CHECKS IN, THAT COUNT IS DECREMENTED BY 1
         ie NOW ITS 2 AND DECREMENTS TILL 0)

    2. IF FOLLOW UP REQUEST COUNT IN AppointmentFollowUpRequestLogConstant < ALLOWED numberOfFollowUps IN Hospital
    (WHEN FIRST APPOINTMENT IS APPROVED ->
        PERSIST IN AppointmentFollowUpTracker AND
        AppointmentFollowUpRequestLogConstant WITH REQUEST COUNT AS 0.
    WHEN CONSECUTIVE FOLLOW UP APPOINTMENT IS TAKEN, REQUEST COUNT IS INCREMENTED BY 1 )

    3. IF REQUESTED APPOINTMENT DATE HAS NOT EXPIRED WHERE
         EXPIRY DATE = APPOINTMENT APPROVED DATE + FOLLOW UP INTERVAL DAYS (FROM Hospital)

    IF ALL THREE CONDITIONS ARE SATISFIED,
    THEN
        IT IS FOLLOW UP APPOINTMENT WITH APPOINTMENT CHARGE AS DOCTOR FOLLOW UP APPOINTMENT CHARGE (FROM Doctor SETUP)
    ELSE
        IT IS NORMAL APPOINTMENT WITH APPOINTMENT CHARGE AS DOCTOR APPOINTMENT CHARGE (FROM Doctor SETUP)
    */
@Entity
@Table(name = "appointment_follow_up_tracker")
@Getter
@Setter
public class AppointmentFollowUpTracker implements Serializable {

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

    @Column(name = "remaining_number_of_follow_ups")
    private Integer remainingNumberOfFollowUps;

    @Temporal(TemporalType.DATE)
    @Column(name = "appointment_approved_date")
    private Date appointmentApprovedDate;

    @Column(name = "status")
    private Character status;

}
