package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.ApiRequestHeader;
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
@Table(name = "api_request_header_history")
public class ApiRequestHeaderHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "api_request_header_id", foreignKey =
    @ForeignKey(name = "FK_api_request_header_history_api_request_header"))
    private ApiRequestHeader apiRequestHeader;

    @Column(name = "api_request_header_content")
    @Lob
    private String apiRequestHeaderContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public ApiRequestHeaderHistory(ApiRequestHeader apiRequestHeader, Action action) {
        this.apiRequestHeader = apiRequestHeader;
        this.apiRequestHeaderContent = apiRequestHeader.toString();
        this.action = action;
    }
}
