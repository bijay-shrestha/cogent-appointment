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
 * @author rupak ON 2020/06/04-10:03 AM
 */
@Entity
@Table(name = "admin_mode_query_parameters")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@EntityListeners(ApiQueryParametersEntityListener.class)
public class AdminModeQueryParameters extends Auditable<String> implements Serializable {

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
        return "AdminModeQueryParameters{" +
                "id=" + id +
                ", apiIntegrationFormatId='" + apiIntegrationFormatId + '\'' +
                ", param='" + param + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }



}
