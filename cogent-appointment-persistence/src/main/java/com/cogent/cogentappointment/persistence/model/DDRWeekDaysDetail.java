package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 08/05/20
 */
@Entity
@Table(name = "ddr_shift_detail")
@Getter
@Setter
public class DDRWeekDaysDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "off_status")
    private Character offStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ddr_shift_wise_id")
    private DoctorDutyRosterShiftWise ddrShiftWise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_days_id")
    private WeekDays weekDays;
}
