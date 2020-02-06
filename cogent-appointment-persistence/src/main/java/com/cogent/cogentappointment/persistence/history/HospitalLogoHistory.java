package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalContactNumber;
import com.cogent.cogentappointment.persistence.model.HospitalLogo;
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
 * @author Sauravi Thapa २०/२/५
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hospital_logo_history")
public class HospitalLogoHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_logo_id",
            foreignKey = @ForeignKey(name = "FK_hospital_logo_history_hospital_logo"))
    private HospitalLogo hospitalLogo;

    @Column(name = "hospital_logo_content")
    @Lob
    private String hospitalLogoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HospitalLogoHistory(HospitalLogo hospitalLogo,Action action) {
        this.hospitalLogo = hospitalLogo;
        this.hospitalLogoContent = hospitalLogo.toString();
        this.action = action;
    }
}
