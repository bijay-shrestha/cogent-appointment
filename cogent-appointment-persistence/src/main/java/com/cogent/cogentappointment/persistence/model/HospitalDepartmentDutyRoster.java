package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalDepartmentDutyRosterEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 20/05/20
 *
 * CONNECTED TABLE :
 * 1. HospitalDepartmentWeekDaysDutyRoster
 * 2. HospitalDepartmentDutyRosterOverride
 * 3. HospitalDepartmentDutyRosterRoomInfo
 * 4. HospitalDeptDutyRosterDoctorInfo
 */
@Table(name = "hospital_department_duty_roster")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(HospitalDepartmentDutyRosterEntityListener.class)
public class HospitalDepartmentDutyRoster extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", updatable = false)
    private Hospital hospital;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_id", updatable = false)
    private HospitalDepartment hospitalDepartment;

    @Column(name = "roster_gap_duration")
    private Integer rosterGapDuration;

    @Temporal(TemporalType.DATE)
    @Column(name = "from_date")
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "status")
    private Character status;

    @Column(name = "has_override_duty_roster")
    private Character hasOverrideDutyRoster;

    @Column(name = "is_room_enabled")
    private Character isRoomEnabled;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "HospitalDepartmentDutyRoster{" +
                "id=" + id +
                ", hospitalDepartment=" + hospitalDepartment.getName() +
                ", rosterGapDuration=" + rosterGapDuration +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", status=" + status +
                ", hasOverrideDutyRoster=" + hasOverrideDutyRoster +
                ", isRoomEnabled=" + isRoomEnabled +
                ", remarks=" + remarks +
                '}';
    }
}
