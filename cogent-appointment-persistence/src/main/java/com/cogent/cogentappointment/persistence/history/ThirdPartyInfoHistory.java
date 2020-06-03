package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AdminAvatar;
import com.cogent.cogentappointment.persistence.model.ThirdPartyInfo;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "third_party_info_history")
public class ThirdPartyInfoHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "third_party_info_id",foreignKey = @ForeignKey(name = "FK_tpi_history_third_party_info"))
    private ThirdPartyInfo thirdPartyInfo;

    @Column(name = "third_party_info_content")
    @Lob
    private String thirdPartyInfoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public ThirdPartyInfoHistory(ThirdPartyInfo thirdPartyInfo,Action action) {
        this.thirdPartyInfo=thirdPartyInfo;
        this.thirdPartyInfoContent=thirdPartyInfo.toString();
        this.action=action;
    }
}
