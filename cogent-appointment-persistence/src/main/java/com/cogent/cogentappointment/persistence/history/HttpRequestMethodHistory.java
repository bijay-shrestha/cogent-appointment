package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.HttpRequestMethod;
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
 * @author rupak on 2020-05-18
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "http_request_method_history")
public class HttpRequestMethodHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "http_request_method_id",
            foreignKey = @ForeignKey(name = "FK_http_request_method_history_http_request_method"))
    private HttpRequestMethod httpRequestMethod;

    @Column(name = "http_request_method_content")
    @Lob
    private String httpRequestMethodContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HttpRequestMethodHistory(HttpRequestMethod httpRequestMethod, Action action) {
        this.httpRequestMethod = httpRequestMethod;
        this.httpRequestMethodContent = httpRequestMethod.toString();
        this.action = action;
    }
}
