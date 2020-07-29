package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AdminModeApiFeatureIntegration;
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
 * @author rupak on 2020-05-24
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "admin_mode_api_feature_integration_history")
public class AdminModeApiFeatureIntegrationHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_mode_api_feature_integration_id", foreignKey = @ForeignKey(name = "FK_admin_mode_api_integration_history_admin_mode_api_integration"))
    private AdminModeApiFeatureIntegration adminModeApiFeatureIntegration;

    @Column(name = "admin_mode_api_feature_integration_content")
    @Lob
    private String adminModeApiFeatureIntegrationContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AdminModeApiFeatureIntegrationHistory(AdminModeApiFeatureIntegration adminModeApiFeatureIntegration, Action action) {
        this.adminModeApiFeatureIntegration = adminModeApiFeatureIntegration;
        this.adminModeApiFeatureIntegrationContent = adminModeApiFeatureIntegration.toString();
        this.action = action;
    }

}
