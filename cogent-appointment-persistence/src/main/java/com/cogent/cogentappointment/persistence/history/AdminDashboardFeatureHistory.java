package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AdminDashboardFeature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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
@Table(name = "admin_dashboard_feature_history")
public class AdminDashboardFeatureHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dashboard_feature_id", foreignKey = @ForeignKey(name = "FK_admin_dashboard_feature_history_admin_dashboard_feature"))
    private AdminDashboardFeature adminDashboardFeature;

    @Column(name = "admin_dashboard_feature_content")
    @Lob
    private String adminDashboardFeatureContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AdminDashboardFeatureHistory(AdminDashboardFeature adminDashboardFeature, Action action) {
        this.adminDashboardFeature = adminDashboardFeature;
        this.adminDashboardFeatureContent = adminDashboardFeature.toString();
        this.action = action;
    }

}
