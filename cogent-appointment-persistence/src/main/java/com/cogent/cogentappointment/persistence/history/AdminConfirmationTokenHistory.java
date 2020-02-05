package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AdminConfirmationToken;
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
 * @author smriti on 2019-09-22
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "admin_confirmation_token_history")
public class AdminConfirmationTokenHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_confirmation_token_id",
            foreignKey = @ForeignKey(name = "FK_admin_confirmation_token_history_admin_confirmation_token"))
    private AdminConfirmationToken adminConfirmationToken;

    @Column(name = "admin_confirmation_token_content")
    @Lob
    private String adminConfirmationTokenContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AdminConfirmationTokenHistory(AdminConfirmationToken adminConfirmationToken, Action action) {
        this.adminConfirmationToken = adminConfirmationToken;
        this.adminConfirmationTokenContent = adminConfirmationToken.toString();
        this.action = action;
    }
}
