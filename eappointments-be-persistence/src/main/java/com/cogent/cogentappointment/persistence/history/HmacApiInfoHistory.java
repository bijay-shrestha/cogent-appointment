package com.cogent.cogentappointment.persistence.history;


import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.HmacApiInfo;
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
 * @author Sauravi Thapa २०/२/५
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hmac_api_info_history")
public class HmacApiInfoHistory extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hmac_api_info_id", foreignKey = @ForeignKey(name = "FK_hmac_api_info_history_hmac_api_info"))
    private HmacApiInfo hmacApiInfo;

    @Column(name = "hmac_api_info_content")
    @Lob
    private String hmacApiInfoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HmacApiInfoHistory(HmacApiInfo hmacApiInfo, Action action) {
        this.hmacApiInfo = hmacApiInfo;
        this.hmacApiInfoContent = hmacApiInfo.toString();
        this.action = action;
    }
}
