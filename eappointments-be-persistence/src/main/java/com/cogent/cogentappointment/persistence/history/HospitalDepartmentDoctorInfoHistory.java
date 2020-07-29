package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDoctorInfo;
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
@Table(name = "hospital_department_doctor_info_history")
public class HospitalDepartmentDoctorInfoHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_department_doctor_info_id")
    private HospitalDepartmentDoctorInfo hospitalDepartmentDoctorInfo;

    @Column(name = "hospital_department_doctor_info_content")
    @Lob
    private String hospitalDepartmentDoctorInfoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HospitalDepartmentDoctorInfoHistory(HospitalDepartmentDoctorInfo hospitalDepartmentDoctorInfo,
                                               Action action) {
        this.hospitalDepartmentDoctorInfo = hospitalDepartmentDoctorInfo;
        this.hospitalDepartmentDoctorInfoContent = hospitalDepartmentDoctorInfo.toString();
        this.action = action;
    }
}
