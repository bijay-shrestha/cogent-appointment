package com.cogent.cogentappointment.admin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 2019-09-22
 */
@Entity
@Table(name = "admin_confirmation_token")
@Getter
@Setter
public class AdminConfirmationToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "status")
    private Character status;
}
