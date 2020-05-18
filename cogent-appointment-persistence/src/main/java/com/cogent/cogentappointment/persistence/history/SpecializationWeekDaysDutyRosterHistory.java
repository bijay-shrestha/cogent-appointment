package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.SpecializationWeekDaysDutyRoster;
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
@Table(name = "specialization_week_days_duty_roster_history")
public class SpecializationWeekDaysDutyRosterHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "specialization_week_days_duty_roster_id")
    private SpecializationWeekDaysDutyRoster specializationWeekDaysDutyRoster;

    @Column(name = "specialization_weekDays_duty_roster_content")
    @Lob
    private String specializationWeekDaysDutyRosterContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public SpecializationWeekDaysDutyRosterHistory(SpecializationWeekDaysDutyRoster specializationWeekDaysDutyRoster,
                                                   Action action) {
        this.specializationWeekDaysDutyRoster = specializationWeekDaysDutyRoster;
        this.specializationWeekDaysDutyRosterContent = specializationWeekDaysDutyRoster.toString();
        this.action = action;
    }
}
