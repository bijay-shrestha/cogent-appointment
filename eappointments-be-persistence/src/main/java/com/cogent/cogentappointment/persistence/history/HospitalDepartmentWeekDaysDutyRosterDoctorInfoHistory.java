package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentWeekDaysDutyRosterDoctorInfo;
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
 * @author smriti on 05/06/20
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hospital_department_week_days_duty_roster_doctor_info_history")
public class HospitalDepartmentWeekDaysDutyRosterDoctorInfoHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_dept_week_days_duty_roster_doctor_info_id")
    private HospitalDepartmentWeekDaysDutyRosterDoctorInfo hospitalDepartmentWeekDaysDutyRosterDoctorInfo;

    @Column(name = "hospital_dept_week_days_duty_roster_doctor_info_content")
    @Lob
    private String hospitalDepartmentWeekDaysDutyRosterDoctorInfoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HospitalDepartmentWeekDaysDutyRosterDoctorInfoHistory(
            HospitalDepartmentWeekDaysDutyRosterDoctorInfo hospitalDepartmentWeekDaysDutyRosterDoctorInfo,
            Action action) {
        this.hospitalDepartmentWeekDaysDutyRosterDoctorInfo = hospitalDepartmentWeekDaysDutyRosterDoctorInfo;
        this.hospitalDepartmentWeekDaysDutyRosterDoctorInfoContent = hospitalDepartmentWeekDaysDutyRosterDoctorInfo.toString();
        this.action = action;
    }
}
