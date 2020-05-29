package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.ApiFeatureIntegrationRequestBodyParameters;
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
 * @author rupak ON 2020/05/29-11:38 AM
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "api_integration_request_body_parameters_history")
public class ApiFeatureIntegrationRequestBodyParametersHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feature_integration_request_body_id",
            foreignKey = @ForeignKey(name = "FK_feature_integration_request_body_history_feature_integration_request_body"))
    private ApiFeatureIntegrationRequestBodyParameters featureIntegrationRequestBodyParameters;

    @Column(name = "feature_integration_request_body_content")
    @Lob
    private String featureIntegrationRequestBodyParametersContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public ApiFeatureIntegrationRequestBodyParametersHistory(ApiFeatureIntegrationRequestBodyParameters featureIntegrationRequestBodyParameters, Action action) {
        this.featureIntegrationRequestBodyParameters = featureIntegrationRequestBodyParameters;
        this.featureIntegrationRequestBodyParametersContent = featureIntegrationRequestBodyParameters.toString();
        this.action = action;
    }
}
