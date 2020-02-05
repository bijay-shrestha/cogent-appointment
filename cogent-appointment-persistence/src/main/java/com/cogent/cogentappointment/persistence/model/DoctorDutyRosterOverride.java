package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.DoctorDutyRosterEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/*SAVE IN THIS ENTITY IN SPECIAL CASE WHEREIN
 * IF WEEK DAYS ROSTER NEEDS TO BE SET FOR SPECIFIC DATE RANGE
 * FOR A PARTICULAR DOCTOR WITH SPECIFIC SPECIALIZATION
 **/
@Table(name = "doctor_duty_roster_override")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(DoctorDutyRosterEntityListener.class)
public class DoctorDutyRosterOverride extends Auditable<String> implements Serializable {

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
    @JoinColumn(name = "doctor_duty_roster_id")
    private DoctorDutyRoster doctorDutyRosterId;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "DoctorDutyRosterOverride{" +
                "id=" + id +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", dayOffStatus=" + dayOffStatus +
                ", doctorDutyRosterId=" + doctorDutyRosterId.getId() +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
