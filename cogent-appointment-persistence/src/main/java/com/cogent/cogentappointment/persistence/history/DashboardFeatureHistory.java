package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.DashboardFeature;
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
 * @author Rupak
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dashboard_feature_history")
public class DashboardFeatureHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dashboard_feature_id", foreignKey = @ForeignKey(name = "FK_dashboard_feature_history_dashboard_feature"))
    private DashboardFeature dashboardFeature;

    @Column(name = "dashboard_feature_content")
    @Lob
    private String dashboardFeatureContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public DashboardFeatureHistory(DashboardFeature dashboardFeature, Action action) {
        this.dashboardFeature = dashboardFeature;
        this.dashboardFeatureContent = dashboardFeature.toString();
        this.action = action;
    }
}
