package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.ClientFeatureIntegration;
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
@Table(name = "client_feature_integration_history")
public class ClientFeatureIntegrationHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_feature_integration_id", foreignKey = @ForeignKey(name = "FK_client_feature_integration_history_client_feature_integration"))
    private ClientFeatureIntegration clientFeatureIntegration;

    @Column(name = "client_feature_integration_content")
    @Lob
    private String clientFeatureIntegrationContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public ClientFeatureIntegrationHistory(ClientFeatureIntegration clientFeatureIntegration, Action action) {
        this.clientFeatureIntegration = clientFeatureIntegration;
        this.clientFeatureIntegrationContent = clientFeatureIntegration.toString();
        this.action = action;
    }

}
