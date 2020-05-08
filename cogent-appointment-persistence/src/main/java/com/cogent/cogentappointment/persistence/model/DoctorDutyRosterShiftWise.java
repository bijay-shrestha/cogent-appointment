package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.DoctorDutyRosterShiftWiseEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 08/05/20
 */
@Table(name = "doctor_duty_roster_shift_wise")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(DoctorDutyRosterShiftWiseEntityListener.class)
public class DoctorDutyRosterShiftWise extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", updatable = false)
    private Hospital hospital;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id", updatable = false)
    private Specialization specialization;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", updatable = false)
    private Doctor doctor;

    @Temporal(TemporalType.DATE)
    @Column(name = "from_date")
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "roster_gap_duration")
    private Integer rosterGapDuration;

    /*Y-> ACTIVE
    * N-> INACTIVE
    * D-> DELETED*/
    @Column(name = "status")
    private Character status;

    @Column(name = "has_override")
    private Character hasOverride;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "DoctorDutyRoster{" +
                "id=" + id +
                ", doctor=" + doctor.getName() +
                ", specialization=" + specialization.getName() +
                ", rosterGapDuration=" + rosterGapDuration +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", status=" + status +
                ", hasOverride=" + hasOverride +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
