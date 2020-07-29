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

/**
 * @author smriti on 25/11/2019
 */
@Table(name = "doctor_duty_roster")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(DoctorDutyRosterEntityListener.class)
public class DoctorDutyRoster extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", updatable = false)
    private Doctor doctorId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id", updatable = false)
    private Specialization specializationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", updatable = false)
    private Hospital hospitalId;

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

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "from_date_nepali")
    private String fromDateInNepali;

    @Column(name = "to_date_nepali")
    private String toDateInNepali;

    @Override
    public String toString() {
        return "DoctorDutyRoster{" +
                "id=" + id +
                ", doctorId=" + doctorId.getName() +
                ", specializationId=" + specializationId.getName() +
                ", hospitalId=" + hospitalId.getName() +
                ", rosterGapDuration=" + rosterGapDuration +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", status=" + status +
                ", hasOverrideDutyRoster=" + hasOverrideDutyRoster +
                ", remarks='" + remarks + '\'' +
                ", fromDateInNepali='" + fromDateInNepali + '\'' +
                ", toDateInNepali='" + toDateInNepali + '\'' +
                '}';
    }
}
