package com.cogent.cogentappointment.persistence.model.ddrShiftWise;

import com.cogent.cogentappointment.persistence.model.BreakType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 11/05/20
 */
@Entity
@Table(name = "ddr_override_break_detail")
@Getter
@Setter
public class DDROverrideBreakDetail implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ddr_override_detail_id")
    private DDROverrideDetail ddrOverrideDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "break_type_id")
    private BreakType breakType;

    /* Y-> ACTIVE
    * D-> DELETED*/
    @Column(name = "status")
    private Character status;

    /*REMARKS FOR BREAK*/
    @Column(name = "remarks")
    private String remarks;

}
