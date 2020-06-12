package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminModeApiFeatureIntegrationEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author rupak on 2020-05-24
 */
@Table(name = "admin_mode_api_feature_integration")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AdminModeApiFeatureIntegrationEntityListener.class)
public class AdminModeApiFeatureIntegration extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "admin_mode_feature_integration_id")
    private AdminModeFeatureIntegration adminModeFeatureIntegrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "api_integration_format_id")
    private ApiIntegrationFormat apiIntegrationFormatId;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "AdminModeApiFeatureIntegration{" +
                "id=" + id +
                ", adminModeFeatureIntegrationId=" + adminModeFeatureIntegrationId.getId() +
                ", apiIntegrationFormatId=" + apiIntegrationFormatId .getId()+
                ", status=" + status +
                '}';
    }
}
