package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.ApiFeatureIntegration;
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
 * @author rupak on 2020-05-18
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "api_feature_integration_history")
public class ApiFeatureIntegrationHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "api_feature_integration_id",
            foreignKey = @ForeignKey(name = "FK_api_feature_integration_history_api_feature_integration"))
    private ApiFeatureIntegration apiFeatureIntegration;

    @Column(name = "api_feature_integration_content")
    @Lob
    private String apiFeatureIntegrationContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public ApiFeatureIntegrationHistory(ApiFeatureIntegration apiFeatureIntegration, Action action) {
        this.apiFeatureIntegration = apiFeatureIntegration;
        this.apiFeatureIntegrationContent = apiFeatureIntegration.toString();
        this.action = action;
    }

}
