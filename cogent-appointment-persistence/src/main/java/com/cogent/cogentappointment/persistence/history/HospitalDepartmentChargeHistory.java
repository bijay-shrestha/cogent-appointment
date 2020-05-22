package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentCharge;
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
@Table(name = "hospital_department_charge_history")
public class HospitalDepartmentChargeHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_department_charge_id")
    private HospitalDepartmentCharge hospitalDepartmentCharge;

    @Column(name = "hospital_department_charge_content")
    @Lob
    private String hospitalDepartmentChargeContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HospitalDepartmentChargeHistory(HospitalDepartmentCharge hospitalDepartmentCharge,
                                           Action action) {
        this.hospitalDepartmentCharge = hospitalDepartmentCharge;
        this.hospitalDepartmentChargeContent = hospitalDepartmentCharge.toString();
        this.action = action;
    }
}
