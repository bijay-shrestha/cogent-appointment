package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.ApiQueryParameters;
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
@Table(name = "api_query_parameters_history")
public class ApiQueryParametersHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "api_query_parameters_id", foreignKey = @ForeignKey(name = "FK_api_query_parameters_history_api_query_parameters"))
    private ApiQueryParameters apiQueryParameters;

    @Column(name = "api_query_parameters_content")
    @Lob
    private String apiQueryParametersContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public ApiQueryParametersHistory(ApiQueryParameters apiQueryParameters, Action action) {
        this.apiQueryParameters = apiQueryParameters;
        this.apiQueryParametersContent = apiQueryParameters.toString();
        this.action = action;
    }
}
