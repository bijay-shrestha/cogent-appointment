package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 08/05/20
 */
@Entity
@Table(name = "ddr_break_detail")
@Getter
@Setter
public class DDRBreakDetail implements Serializable {

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
    @JoinColumn(name = "ddr_week_days_detail_id")
    private DDRWeekDaysDetail ddrWeekDaysDetail;

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
