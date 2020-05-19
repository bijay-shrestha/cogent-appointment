package com.cogent.cogentappointment.persistence.model;

import javax.persistence.*;

/**
 * @author rupak on 2020-05-19
 */
public class ApiRequestHeader {

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
