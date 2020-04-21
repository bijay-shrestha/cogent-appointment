package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.ForgotPasswordVerificationEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * @author smriti on 2019-09-19
 */

@Table(name = "forgot_password_verification")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(ForgotPasswordVerificationEntityListener.class)
public class ForgotPasswordVerification extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(name = "reset_code")
    private String resetCode;

    @Column(name = "status")
    private Character status;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Override
    public String toString() {
        return "ForgotPasswordVerification{" +
                "id=" + id +
                ", admin=" + admin.getFullName() +
                ", resetCode='" + resetCode + '\'' +
                ", status=" + status +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
