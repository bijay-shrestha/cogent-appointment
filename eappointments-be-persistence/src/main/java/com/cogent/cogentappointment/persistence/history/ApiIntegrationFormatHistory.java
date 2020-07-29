package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.ApiIntegrationFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author rupak on 2020-05-19
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "api_integration_format_history")
public class ApiIntegrationFormatHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "api_integration_format_id",foreignKey = @ForeignKey(name = "FK_api_integration_format_history_api_integration_format"))
    private ApiIntegrationFormat apiIntegrationFormat;

    @Column(name = "api_integration_format_content")
    @Lob
    private String apiIntegrationFormatContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public ApiIntegrationFormatHistory(ApiIntegrationFormat apiIntegrationFormat, Action action) {
        this.apiIntegrationFormat = apiIntegrationFormat;
        this.apiIntegrationFormatContent = apiIntegrationFormat.toString();
        this.action = action;
    }


}
