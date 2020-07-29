package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.ApiFeatureIntegrationRequestBodyParametersEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author rupak ON 2020/05/29-11:35 AM
 */
/* It consists of request body parameters assigned to features.
*/
@Table(name = "api_feature_integration_request_body_parameters")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(ApiFeatureIntegrationRequestBodyParametersEntityListener.class)
public class ApiFeatureIntegrationRequestBodyParameters extends
        Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feature_id")
    private Long featureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_request_body_parameters_id")
    private ApiIntegrationRequestBodyParameters requestBodyParametersId;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "ApiFeatureIntegrationRequestBodyParametersEntityListener{" +
                "id=" + id +
                ", featureId=" + featureId+
                ", requestBodyParametersId=" + requestBodyParametersId.getId() +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                '}';
    }


}