package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;
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
@Table(name = "qualification_alias_hisotry")
public class QualificationAliasHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "qualification_alias_id",
            foreignKey = @ForeignKey(name = "FK_qualification_alias_history_qualification_alias"))
    private QualificationAlias qualificationAlias;

    @Column(name = "qualification_alias_content")
    @Lob
    private String qualificationAliasContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public QualificationAliasHistory(QualificationAlias qualificationAlias, Action action) {
        this.qualificationAlias = qualificationAlias;
        this.qualificationAliasContent = qualificationAlias.toString();
        this.action = action;
    }
}
