package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalDepartmentDutyRosterOverrideEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/*SAVE IN THIS ENTITY IN SPECIAL CASE WHEREIN
 * IF WEEK DAYS ROSTER NEEDS TO BE SET FOR SPECIFIC DATE RANGE
 * FOR A PARTICULAR HOSPITAL DEPARTMENT
 **/
@Table(name = "hospital_department_duty_roster_override")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(HospitalDepartmentDutyRosterOverrideEntityListener.class)
public class HospitalDepartmentDutyRosterOverride extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "from_date")
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "to_date")
    private Date toDate;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_room_info_id")
    private HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "from_date_nepali")
    private String fromDateInNepali;

    @Column(name = "to_date_nepali")
    private String toDateInNepali;

    @Override
    public String toString() {
        return "HospitalDepartmentDutyRosterOverride{" +
                "id=" + id +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", dayOffStatus=" + dayOffStatus +
                ", hospitalDepartmentDutyRoster=" + hospitalDepartmentDutyRoster.getId() +
                ", hospitalDepartmentRoomInfo=" + hospitalDepartmentRoomInfo.getId() +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                ", fromDateInNepali='" + fromDateInNepali + '\'' +
                ", toDateInNepali='" + toDateInNepali + '\'' +
                '}';
    }
}
