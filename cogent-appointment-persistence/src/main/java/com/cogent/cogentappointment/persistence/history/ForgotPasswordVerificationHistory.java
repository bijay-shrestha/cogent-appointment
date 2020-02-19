package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.ForgotPasswordVerification;
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
@Table(name = "forgot_password_verification_history")
public class ForgotPasswordVerificationHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "forgot_password_verification_id",
            foreignKey = @ForeignKey(name = "FK_fgpassword_verification_history_fgpassword_verification"))
    private ForgotPasswordVerification forgotPasswordVerification;

    @Column(name = "admin_content")
    @Lob
    private String forgotPasswordVerificationContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public ForgotPasswordVerificationHistory(ForgotPasswordVerification forgotPasswordVerification,Action action) {
        this.forgotPasswordVerification=forgotPasswordVerification;
        this.forgotPasswordVerificationContent=forgotPasswordVerification.toString();
        this.action=action;
    }
}
