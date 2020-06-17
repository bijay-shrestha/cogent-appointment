package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.ApiFeatureIntegrationEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author rupak on 2020-05-18
 */
/*
This is join table for ClientFeatureIntegration and ApiIntegrationFormat for Client(Hospital) APIs.
 */
@Table(name = "api_feature_integration")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ApiFeatureIntegrationEntityListener.class)
public class ApiFeatureIntegration extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_feature_integration_id")
    private Long clientFeatureIntegrationId;

    @Column(name = "api_integration_format_id")
    private Long apiIntegrationFormatId;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "ApiFeatureIntegration{" +
                "id=" + id +
                ", clientFeatureIntegrationId=" + clientFeatureIntegrationId +
                ", apiIntegrationFormatId=" + apiIntegrationFormatId +
                ", status=" + status +
                '}';
    }

}
