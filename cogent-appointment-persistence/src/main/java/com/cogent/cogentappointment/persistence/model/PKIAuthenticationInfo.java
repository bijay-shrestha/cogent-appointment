package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 06/07/20
 */
@Entity
@Table(name = "pki_authentication_info")
@Getter
@Setter
public class PKIAuthenticationInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "server_public_key")
    private String serverPublicKey;

    @Column(name = "server_private_key")
    private String serverPrivateKey;

    @Column(name = "client_public_key")
    private String clientPublicKey;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "status")
    private Character status;
}
