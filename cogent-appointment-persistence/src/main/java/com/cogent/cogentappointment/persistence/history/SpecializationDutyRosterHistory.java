package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.SpecializationDutyRoster;
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
@Table(name = "specialization_duty_roster_history")
public class SpecializationDutyRosterHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "specialization_duty_roster_id")
    private SpecializationDutyRoster specializationDutyRoster;

    @Column(name = "specialization_duty_roster_content")
    @Lob
    private String specializationDutyRosterContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public SpecializationDutyRosterHistory(SpecializationDutyRoster specializationDutyRoster, Action action) {
        this.specializationDutyRoster = specializationDutyRoster;
        this.specializationDutyRosterContent = specializationDutyRoster.toString();
        this.action = action;
    }
}
