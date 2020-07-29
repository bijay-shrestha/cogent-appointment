package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminDashboardFeatureEntityListener;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Rupak
 */
@ToString
@Entity
@Table(name = "admin_dashboard_feature")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AdminDashboardFeatureEntityListener.class)
public class AdminDashboardFeature extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin adminId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_feature_id")
    private DashboardFeature dashboardFeatureId;

    @Column(name = "status")
    private Character status;

}
