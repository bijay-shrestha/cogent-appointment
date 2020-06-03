package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationFormat;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationType;
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
 * @author rupak on 2020-05-26
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "api_integration_type_history")
public class ApiIntegrationTypeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "api_integration_type_id",foreignKey = @ForeignKey(name = "FK_api_integration_type_history_api_integration_type"))
    private ApiIntegrationType apiIntegrationType;

    @Column(name = "api_integration_type_content")
    @Lob
    private String apiIntegrationTypeContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public ApiIntegrationTypeHistory(ApiIntegrationType apiIntegrationType, Action action) {
        this.apiIntegrationType = apiIntegrationType;
        this.apiIntegrationTypeContent = apiIntegrationType.toString();
        this.action = action;
    }
}
