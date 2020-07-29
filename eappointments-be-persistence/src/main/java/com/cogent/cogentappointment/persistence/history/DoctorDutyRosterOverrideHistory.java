package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterOverride;
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
@Table(name = "doctor_duty_roster_override_history")
public class DoctorDutyRosterOverrideHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_duty_roster_override_id",
            foreignKey = @ForeignKey(name = "FK_roster_override_history_roster_override"))
    private DoctorDutyRosterOverride doctorDutyRosterOverride;

    @Column(name = "doctor_duty_roster_override_content")
    @Lob
    private String doctorDutyRosterOverrideContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public DoctorDutyRosterOverrideHistory(DoctorDutyRosterOverride doctorDutyRosterOverride, Action action) {
        this.doctorDutyRosterOverride = doctorDutyRosterOverride;
        this.doctorDutyRosterOverrideContent = doctorDutyRosterOverride.toString();
        this.action = action;
    }
}
