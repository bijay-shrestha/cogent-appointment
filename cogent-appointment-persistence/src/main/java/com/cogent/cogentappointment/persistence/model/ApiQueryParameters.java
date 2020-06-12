package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.ApiQueryParametersEntityListener;
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
@Table(name = "api_query_parameters")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ApiQueryParametersEntityListener.class)
public class ApiQueryParameters extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_integration_format_id")
    private Long apiIntegrationFormatId;

    @Column(name = "param")
    private String param;

    @Column(name = "value")
    private String value;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "ApiQueryParameters{" +
                "id=" + id +
                ", apiIntegrationFormatId='" + apiIntegrationFormatId + '\'' +
                ", param='" + param + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

}
