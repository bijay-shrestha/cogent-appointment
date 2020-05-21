package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminModeFeatureIntegrationEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author rupak on 2020-05-21
 */
@Table(name = "admin_mode_feature_integration")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AdminModeFeatureIntegrationEntityListener.class)
public class AdminModeFeatureIntegration extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "appointment_mode_id")
    private AppointmentMode appointmentModeId;

    @Column(name = "feature_id")
    private Long featureId;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "AdminModeFeatureIntegration{" +
                "id=" + id +
                ", appointmentModeId=" + appointmentModeId.getId() +
                ", featureId=" + featureId +
                ", status=" + status +
                '}';
    }

}
