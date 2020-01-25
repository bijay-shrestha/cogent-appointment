package com.cogent.cogentappointment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 26/11/2019
 */
@Table(name = "doctor_week_days_duty_roster")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorWeekDaysDutyRoster implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "day_off_status")
    private Character dayOffStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_duty_roster_id")
    private DoctorDutyRoster doctorDutyRosterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_days_id")
    private WeekDays weekDaysId;
}
