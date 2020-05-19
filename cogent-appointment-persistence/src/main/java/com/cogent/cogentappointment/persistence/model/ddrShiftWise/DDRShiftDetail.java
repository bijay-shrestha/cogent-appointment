package com.cogent.cogentappointment.persistence.model.ddrShiftWise;

import com.cogent.cogentappointment.persistence.model.Shift;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 08/05/20
 */
@Entity
@Table(name = "ddr_shift_detail")
@Getter
@Setter
public class DDRShiftDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ddr_shift_wise_id")
    private DoctorDutyRosterShiftWise ddrShiftWise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @Column(name = "roster_gap_duration")
    private Integer rosterGapDuration;

    /*DURING EDIT, SHIFT INFORMATION CAN BE UPDATED SEPARATELY.
    * SO, IF ANY SHIFTS ARE ASSIGNED IN DDR, ITS STATUS IS INITIALLY INACTIVE
    * AND WHEN WEEK DAYS INFO ARE PROVIDED, ITS STATUS IS ACTIVE.*/
    /* Y-> ACTIVE
    N-> INACTIVE
    * D-> DELETED*/
    @Column(name = "status")
    private Character status;
}
