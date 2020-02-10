package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
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
 * @author smriti ON 10/02/2020
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hospital_patient_info_history")
public class HospitalPatientInfoHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_patient_info_id",
            foreignKey = @ForeignKey(name = "FK_hospital_patient_info"))
    private HospitalPatientInfo hospitalPatientInfo;

    @Column(name = "hospital_patient_info_content")
    @Lob
    private String hospitalPatientInfoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HospitalPatientInfoHistory(HospitalPatientInfo hospitalPatientInfo, Action action) {
        this.hospitalPatientInfo = hospitalPatientInfo;
        this.hospitalPatientInfoContent = hospitalPatientInfoContent.toString();
        this.action = action;
    }
}
