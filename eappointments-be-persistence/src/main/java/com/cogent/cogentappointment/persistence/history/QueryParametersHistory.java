package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.Qualification;
import com.cogent.cogentappointment.persistence.model.QueryParameters;
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
@Table(name = "query_parameters_history")
public class QueryParametersHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "query_parameters_id",
            foreignKey = @ForeignKey(name = "FK_query_parameters_history_query_parameters"))
    private QueryParameters queryParameters;

    @Column(name = "query_parameters_content")
    @Lob
    private String queryParametersContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public QueryParametersHistory(QueryParameters queryParameters, Action action) {
        this.queryParameters = queryParameters;
        this.queryParametersContent = queryParameters.toString();
        this.action = action;
    }
}
