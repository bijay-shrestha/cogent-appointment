package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalDepartmentDutyRosterRoomInfoEntityListener;
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
@EntityListeners(HospitalDepartmentDutyRosterRoomInfoEntityListener.class)
public class HospitalDepartmentDutyRosterRoomInfo extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_duty_roster_id")
    private HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_department_room_info_id")
    private HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "HospitalDepartmentDutyRosterRoomInfo{" +
                "id=" + id +
                ", hospitalDepartmentDutyRoster=" + hospitalDepartmentDutyRoster.getId() +
                ", hospitalDepartmentRoomInfo=" + hospitalDepartmentRoomInfo.getId() +
                ", status=" + status +
                '}';
    }
}