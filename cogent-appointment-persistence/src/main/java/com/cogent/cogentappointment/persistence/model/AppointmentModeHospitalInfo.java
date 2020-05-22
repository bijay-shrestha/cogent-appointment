package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.listener.AppointmentModeHospitalInfoListener;
import com.cogent.cogentappointment.persistence.listener.AppointmentModeListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author rupak on 2020-05-22
 */
@Entity
@Table(name = "appointment_mode_hospital_info")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AppointmentModeHospitalInfoListener.class)
public class AppointmentModeHospitalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "appointment_mode_id")
    private AppointmentMode appointmentModeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "hospital_id")
    private Hospital hospitalId;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "ApiFeatureIntegration{" +
                "id=" + id +
                ", appointmentModeId=" + appointmentModeId.getId() +
                ", hospitalId=" + hospitalId.getId() +
                ", remarks='" + remarks + '\'' +
                ", status=" + status +
                '}';
    }


}
