package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AppointmentModeHospitalInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author rupak on 2020-05-22
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "appointment_mode_hospital_info_history")
public class AppointmentModeHospitalInfoHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_mode_hospital_info_id",foreignKey = @ForeignKey(name = "FK_appointment_mode_hospital_history_appointment_mode_hospital"))
    private AppointmentModeHospitalInfo appointmentModeHospitalInfo;

    @Column(name = "appointment_mode_hospital_content")
    @Lob
    private String appointmentModeHospitalInfoContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AppointmentModeHospitalInfoHistory(AppointmentModeHospitalInfo appointmentModeHospitalInfo, Action action) {
        this.appointmentModeHospitalInfo=appointmentModeHospitalInfo;
        this.appointmentModeHospitalInfoContent=appointmentModeHospitalInfo.toString();
        this.action=action;
    }
}
