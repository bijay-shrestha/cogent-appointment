package com.cogent.cogentappointment.persistence.model;


import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HmacApiInfoEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/२/२
 */

@Entity
@Table(name = "hmac_api_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HmacApiInfoEntityListener.class)
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

    @Override
    public String toString() {
        return "HmacApiInfo{" +
                "id=" + id +
                ", hospital=" + hospital.getName() +
                ", apiKey='" + apiKey + '\'' +
                ", apiSecret='" + apiSecret + '\'' +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
