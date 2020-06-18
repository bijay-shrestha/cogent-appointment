package com.cogent.cogentappointment.persistence.model;


import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.ApiIntegrationRequestBodyParametersEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/*
This table consists of request body paramters key for request body.
 */
@Table(name = "api_integration_request_body_parameters")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(ApiIntegrationRequestBodyParametersEntityListener.class)
public class ApiIntegrationRequestBodyParameters extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "ApiIntegrationRequestBodyParameters{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                '}';
    }

}
