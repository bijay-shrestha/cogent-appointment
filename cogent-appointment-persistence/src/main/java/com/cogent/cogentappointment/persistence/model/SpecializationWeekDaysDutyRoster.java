package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.SpecializationWeekDaysDutyRosterEntityListener;
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
@Table(name = "specialization_week_days_duty_roster")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(SpecializationWeekDaysDutyRosterEntityListener.class)
public class SpecializationWeekDaysDutyRoster extends Auditable<String> implements Serializable {
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
    @JoinColumn(name = "specialization_duty_roster_id")
    private SpecializationDutyRoster specializationDutyRoster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_days_id")
    private WeekDays weekDays;

    @Override
    public String toString() {
        return "SpecializationWeekDaysDutyRoster{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", dayOffStatus=" + dayOffStatus +
                ", specializationDutyRoster=" + specializationDutyRoster.getSpecializationId() +
                ", weekDaysId=" + weekDays.getName() +
                '}';
    }
}
