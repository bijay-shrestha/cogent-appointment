package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.IntegrationRequestBodyParameters;
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
 * @author rupak ON 5/28/20
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "integration_request_body_parameters_history")
public class IntegrationRequestBodyParametersHistory implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "integration_request_body_id",
            foreignKey = @ForeignKey(name = "FK_integration_request_body_history_integration_request_body"))
    private IntegrationRequestBodyParameters integrationRequestBodyParameters;

    @Column(name = "integration_request_body_content")
    @Lob
    private String integrationRequestBodyParametersContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public IntegrationRequestBodyParametersHistory(IntegrationRequestBodyParameters integrationRequestBodyParameters, Action action) {
        this.integrationRequestBodyParameters = integrationRequestBodyParameters;
        this.integrationRequestBodyParametersContent = integrationRequestBodyParameters.toString();
        this.action = action;
    }
}
