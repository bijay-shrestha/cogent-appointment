package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalDepartmentWeekDaysDutyRosterEntityListener;
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
@Table(name = "hospital_department_week_days_duty_roster")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(HospitalDepartmentWeekDaysDutyRosterEntityListener.class)
public class HospitalDepartmentWeekDaysDutyRoster extends Auditable<String> implements Serializable {

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
    @JoinColumn(name = "hospital_department_duty_roster_id")
    private HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_days_id")
    private WeekDays weekDays;

    @Override
    public String toString() {
        return "HospitalDepartmentWeekDaysDutyRoster{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", dayOffStatus=" + dayOffStatus +
                ", hospitalDepartmentDutyRoster=" + hospitalDepartmentDutyRoster.getId() +
                ", weekDaysId=" + weekDays.getName() +
                '}';
    }
}
