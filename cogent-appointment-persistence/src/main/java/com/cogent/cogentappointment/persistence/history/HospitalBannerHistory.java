package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.HospitalBanner;
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
 * @author Sauravi Thapa २०/२/३
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hospital_banner_history")
public class HospitalBannerHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_banner_id",foreignKey = @ForeignKey(name = "FK_hbanner_history_hbanner"))
    private HospitalBanner hospitalBanner;

    @Column(name = "hospital_banner_content")
    @Lob
    private String hospitalBannerContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HospitalBannerHistory(HospitalBanner hospitalBanner, Action action) {
        this.hospitalBanner = hospitalBanner;
        this.hospitalBannerContent = hospitalBanner.toString();
        this.action = action;
    }

}
