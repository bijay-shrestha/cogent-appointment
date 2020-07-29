package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.ClientFeatureIntegrationEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author rupak on 2020-05-18
 */
@Entity
@Table(name = "client_feature_integration")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(ClientFeatureIntegrationEntityListener.class)
public class ClientFeatureIntegration extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hospital_id")
    private Long hospitalId;

    @Column(name = "feature_id")
    private Long featureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "integration_channel_id")
    private IntegrationChannel integrationChannelId;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "ClientFeatureIntegration{" +
                "id=" + id +
                ", hospitalId=" + hospitalId +
                ", featureId=" + featureId +
                ", integrationChannelId=" + integrationChannelId.getId() +
                ", remarks='" + remarks + '\'' +
                ", status=" + status +
                '}';
    }


}
