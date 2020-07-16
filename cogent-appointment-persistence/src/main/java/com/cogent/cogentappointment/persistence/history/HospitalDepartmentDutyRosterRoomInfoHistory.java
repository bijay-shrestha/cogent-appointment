package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterRoomInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Sauravi Thapa ON 6/8/20
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hospital_department_duty_roster_room_info_history")
public class HospitalDepartmentDutyRosterRoomInfoHistory  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_dept_duty_roster_room_info_id")
    private HospitalDepartmentDutyRosterRoomInfo hospitalDepartmentDutyRosterRoomInfo;

    @Column(name = "hospital_dept_duty_roster_room_info_content")
    @Lob
    private String hospitalDepartmentDutyRosterRoomInfoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HospitalDepartmentDutyRosterRoomInfoHistory(HospitalDepartmentDutyRosterRoomInfo hospitalDepartmentDutyRosterRoomInfo,
                                                       Action action) {
        this.hospitalDepartmentDutyRosterRoomInfo = hospitalDepartmentDutyRosterRoomInfo;
        this.hospitalDepartmentDutyRosterRoomInfoContent=hospitalDepartmentDutyRosterRoomInfo.toString();
        this.action = action;
    }
}
