package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.DoctorWeekDaysDutyRoster;
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
@Table(name = "doctor_week_days_duty_roster_history")
public class DoctorWeekDaysDutyRosterHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_week_days_duty_roster_id",
            foreignKey = @ForeignKey(name = "FK_week_days_roster_history_week_days_roster"))
    private DoctorWeekDaysDutyRoster doctorWeekDaysDutyRoster;

    @Column(name = "doctor_content")
    @Lob
    private String doctorWeekDaysDutyRosterContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public DoctorWeekDaysDutyRosterHistory(DoctorWeekDaysDutyRoster doctorWeekDaysDutyRoster, Action action) {
        this.doctorWeekDaysDutyRoster = doctorWeekDaysDutyRoster;
        this.doctorWeekDaysDutyRosterContent = doctorWeekDaysDutyRoster.toString();
        this.action = action;
    }
}
