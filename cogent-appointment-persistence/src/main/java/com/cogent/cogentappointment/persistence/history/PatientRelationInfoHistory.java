package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.PatientRelationInfo;
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
 * @author smriti on 26/02/20
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "patient_relation_info_history")
public class PatientRelationInfoHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", foreignKey = @ForeignKey(name = "FK_prf_history_patient_relation_info"))
    private PatientRelationInfo patientRelationInfo;

    @Column(name = "patient_relation_info_content")
    @Lob
    private String patientRelationInfoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public PatientRelationInfoHistory(PatientRelationInfo patientRelationInfo, Action action) {
        this.patientRelationInfo = patientRelationInfo;
        this.patientRelationInfoContent = patientRelationInfo.toString();
        this.action = action;
    }
}
