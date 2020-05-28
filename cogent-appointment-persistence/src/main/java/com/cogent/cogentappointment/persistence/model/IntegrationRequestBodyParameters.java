package com.cogent.cogentappointment.persistence.model;


import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.IntegrationRequestBodyParametersEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "integration_request_body_parameters")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(IntegrationRequestBodyParametersEntityListener.class)
public class IntegrationRequestBodyParameters extends Auditable<String> implements Serializable {

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
        return "IntegrationRequestBodyParameters{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                '}';
    }




}
