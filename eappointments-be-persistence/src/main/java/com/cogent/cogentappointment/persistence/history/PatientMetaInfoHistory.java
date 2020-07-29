package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;
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
 * @author Sauravi Thapa २०/२/४
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "patient_meta_info_history")
public class PatientMetaInfoHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_meta_info_id",
            foreignKey = @ForeignKey(name = "FK_patient_meta_info_history_patient_meta_info"))
    private PatientMetaInfo patientMetaInfo;

    @Column(name = "patient_meta_info_content")
    @Lob
    private String patientMetaInfoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public PatientMetaInfoHistory(PatientMetaInfo patientMetaInfo, Action action) {
        this.patientMetaInfo = patientMetaInfo;
        this.patientMetaInfoContent = patientMetaInfo.toString();
        this.action = action;
    }
}
