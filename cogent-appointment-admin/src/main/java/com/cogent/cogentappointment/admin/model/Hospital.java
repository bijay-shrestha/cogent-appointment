package com.cogent.cogentappointment.admin.model;

import com.cogent.cogentappointment.admin.audit.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author smriti ON 12/01/2020
 */
@Entity
@Table(name = "hospital")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Hospital extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    @Size(max = 4)
    private String code;

    @Column(name = "address")
    private String address;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "api_secret")
    private String apiSecret;
}
