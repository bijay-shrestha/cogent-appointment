package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AdminModeFeatureIntegration;
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
 * @author rupak on 2020-05-21
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "admin_mode_feature_integration_history")
public class AdminModeFeatureIntegrationHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_mode_feature_integration_id", foreignKey = @ForeignKey(name = "FK_admin_mode_integration_history_admin_mode_integration"))
    private AdminModeFeatureIntegration adminModeFeatureIntegration;

    @Column(name = "admin_mode_feature_integration_content")
    @Lob
    private String adminModeFeatureIntegrationContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AdminModeFeatureIntegrationHistory(AdminModeFeatureIntegration adminModeFeatureIntegration, Action action) {
        this.adminModeFeatureIntegration = adminModeFeatureIntegration;
        this.adminModeFeatureIntegrationContent = adminModeFeatureIntegration.toString();
        this.action = action;
    }

}
