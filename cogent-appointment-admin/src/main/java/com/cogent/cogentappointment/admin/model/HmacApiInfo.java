package com.cogent.cogentappointment.admin.model;

import com.cogent.cogentappointment.admin.audit.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/२/२
 */

@Entity
@Table(name = "hmac_api_info")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class HmacApiInfo extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "api_secret")
    private String apiSecret;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;
}
