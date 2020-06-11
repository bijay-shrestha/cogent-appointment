package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.QueryParametersEntityListener;
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
@Table(name = "query_parameters")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(QueryParametersEntityListener.class)
public class QueryParameters extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_integration_format_id")
    private ApiIntegrationFormat apiIntegrationFormatId;

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
        return "QueryParameters{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", apiIntegrationFormatId='" + apiIntegrationFormatId.getId() + '\'' +
                ", param='" + param + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
