package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
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
@Table(name = "doctor_duty_roster_history")
public class DoctorDutyRosterHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_duty_roster_id",
            foreignKey = @ForeignKey(name = "FK_doctor_duty_roster_history_doctor_duty_roster"))
    private DoctorDutyRoster doctorDutyRoster;

    @Column(name = "doctor_duty_roster_content")
    @Lob
    private String doctorDutyRosterContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public DoctorDutyRosterHistory(DoctorDutyRoster doctorDutyRoster, Action action) {
        this.doctorDutyRoster = doctorDutyRoster;
        this.doctorDutyRosterContent = doctorDutyRoster.toString();
        this.action = action;
    }
}
