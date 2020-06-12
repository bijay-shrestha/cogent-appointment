package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDoctorInfo;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;
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
 * @author Sauravi Thapa ON 5/19/20
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hospital_department_room_info_history")
public class HospitalDepartmentRoomInfoHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_department_room_info_id")
    private HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo;

    @Column(name = "hospital_department_room_info_content")
    @Lob
    private String hospitalDepartmentRoomInfoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HospitalDepartmentRoomInfoHistory(HospitalDepartmentRoomInfo hospitalDepartmentRoomInfo,
                                             Action action) {
        this.hospitalDepartmentRoomInfo = hospitalDepartmentRoomInfo;
        this.hospitalDepartmentRoomInfoContent = hospitalDepartmentRoomInfo.toString();
        this.action = action;
    }
}
