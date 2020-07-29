package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalBillingModeInfo;
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
 * @author Sauravi Thapa 29/05/2020
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hospital_billing_mode_info_history")
public class HospitalBillingModeInfoHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_billing_mode_info_id")
    private HospitalBillingModeInfo hospitalBillingModeInfo;

    @Column(name = "hospital_billing_mode_info_content")
    @Lob
    private String hospitalBillingModeInfoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HospitalBillingModeInfoHistory(HospitalBillingModeInfo hospitalBillingModeInfo, Action action) {
        this.hospitalBillingModeInfo = hospitalBillingModeInfo;
        this.hospitalBillingModeInfoContent = hospitalBillingModeInfo.toString();
        this.action = action;
    }


}
