package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentWeekDaysDutyRoster;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Sauravi Thapa २०/२/५
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hospital_department_week_days_duty_roster_history")
public class HospitalDepartmentWeekDaysDutyRosterHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_department_week_days_duty_roster_id")
    private HospitalDepartmentWeekDaysDutyRoster hospitalDepartmentWeekDaysDutyRoster;

    @Column(name = "hospital_department_weekDays_duty_roster_content")
    @Lob
    private String hospitalDepartmentWeekDaysDutyRosterContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HospitalDepartmentWeekDaysDutyRosterHistory(HospitalDepartmentWeekDaysDutyRoster hospitalDepartmentWeekDaysDutyRoster,
                                                       Action action) {
        this.hospitalDepartmentWeekDaysDutyRoster = hospitalDepartmentWeekDaysDutyRoster;
        this.hospitalDepartmentWeekDaysDutyRosterContent = hospitalDepartmentWeekDaysDutyRoster.toString();
        this.action = action;
    }
}
