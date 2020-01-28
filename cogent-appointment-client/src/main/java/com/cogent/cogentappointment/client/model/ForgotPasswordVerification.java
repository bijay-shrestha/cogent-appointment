package com.cogent.cogentappointment.client.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 2019-09-19
 */
@Entity
@Table(name = "forgot_password_verification")
@Getter
@Setter
public class ForgotPasswordVerification  implements Serializable {
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
}
