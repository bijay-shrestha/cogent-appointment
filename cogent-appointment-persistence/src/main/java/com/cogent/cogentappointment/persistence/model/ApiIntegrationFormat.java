package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.ApiFeatureIntegrationEntityListener;
import com.cogent.cogentappointment.persistence.listener.ApiIntegrationFormatEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author rupak on 2020-05-19
 */
@Entity
@Table(name = "api_integration_format")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ApiIntegrationFormatEntityListener.class)
public class ApiIntegrationFormat extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "http_request_method_id")
    private Long httpRequestMethodId;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "ApiIntegrationFormat{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", httpRequestMethodId='" + httpRequestMethodId + '\'' +
                ", status=" + status +
                '}';
    }
}
