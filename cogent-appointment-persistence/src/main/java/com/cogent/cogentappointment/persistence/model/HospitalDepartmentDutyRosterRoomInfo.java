package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 20/05/20
 */
@Table(name = "hospital_department_duty_roster_room_info")
@Entity
@Getter
@Setter
public class HospitalDepartmentDutyRosterRoomInfo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_duty_roster_id")
    private HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "status")
    private Character status;

}
