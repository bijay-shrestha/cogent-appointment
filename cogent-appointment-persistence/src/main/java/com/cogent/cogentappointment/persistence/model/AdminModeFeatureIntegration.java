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

/* AdminModeFeatureIntegration table is API Integration of Appointment Mode
Appointment Mode is integrated by appointment mode & hospital wise with integration channel.
Integration Channel can be FRONTEND OR BACKEND.
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "hospital_id")
    private Hospital hospitalId;

    @Column(name = "feature_id")
    private Long featureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "integration_channel_id")
    private IntegrationChannel integrationChannelId;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "AdminModeFeatureIntegration{" +
                "id=" + id +
                ", appointmentModeId=" + appointmentModeId.getId() +
                ", integrationChannelId=" + integrationChannelId.getId() +
                ", hospitalId=" + hospitalId.getId() +
                ", featureId=" + featureId +
                ", remarks='" + remarks + '\'' +
                ", status=" + status +
                '}';
    }

}
