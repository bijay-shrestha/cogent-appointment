package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
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
@Table(name = "hospital_department_duty_roster_history")
public class HospitalDepartmentDutyRosterHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_dept_duty_roster_id",
            foreignKey = @ForeignKey(name = "FK_hospital_dept_duty_roster"))
    private HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster;

    @Column(name = "hospital_dept_duty_roster_content")
    @Lob
    private String hospitalDeptDutyRosterContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HospitalDepartmentDutyRosterHistory(HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster,
                                               Action action) {
        this.hospitalDepartmentDutyRoster = hospitalDepartmentDutyRoster;
        this.hospitalDeptDutyRosterContent = hospitalDepartmentDutyRoster.toString();
        this.action = action;
    }
}
