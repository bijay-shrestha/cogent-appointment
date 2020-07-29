package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.ApiIntegrationTypeEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author rupak on 2020-05-26
 */
@Entity
@Table(name = "api_integration_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ApiIntegrationTypeEntityListener.class)
public class ApiIntegrationType  extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "ApiIntegrationType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }

}
