package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.DashboardFeatureEntityListener;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Rupak
 */
@ToString
@Entity
@Table(name = "dashboard_feature")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(DashboardFeatureEntityListener.class)
public class DashboardFeature extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "DashboardFeature{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }

}
