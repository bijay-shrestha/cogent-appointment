package com.cogent.cogentappointment.persistence.model.ddrShiftWise;

import com.cogent.cogentappointment.persistence.model.Shift;
import com.cogent.cogentappointment.persistence.model.ddrShiftWise.DoctorDutyRosterShiftWise;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 11/05/20
 */
@Entity
@Table(name = "ddr_override_detail")
@Getter
@Setter
public class DDROverrideDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "off_status")
    private Character offStatus;

    @Column(name = "roster_gap_duration")
    private Integer rosterGapDuration;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ddr_shift_wise_id")
    private DoctorDutyRosterShiftWise ddrShiftWise;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    /*DEFAULT 'N' -> SAVED FROM ADD PROCESS
    * 'Y' -> WHEN ADDED FROM DAY-WISE ADD SHIFT PROCESS*/
    @Column(name = "is_shift_added")
    private Character isAddedShift;

    /*Y-> ACTIVE
    * N-> INACTIVE
    */
    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;
}
