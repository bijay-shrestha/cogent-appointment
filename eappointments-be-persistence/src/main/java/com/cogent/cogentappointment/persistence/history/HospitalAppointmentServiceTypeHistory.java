package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.HospitalAppointmentServiceType;
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
 * @author smriti on 26/05/20
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "admin_appointment_service_type_history")
public class HospitalAppointmentServiceTypeHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hospital_appointment_service_type_id",
            foreignKey = @ForeignKey(name = "FK_hospital_appointment_service_type"))
    private HospitalAppointmentServiceType hospitalAppointmentServiceType;

    @Column(name = "hospital_appointment_service_type_content")
    @Lob
    private String hospitalAppointmentServiceTypeContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public HospitalAppointmentServiceTypeHistory(HospitalAppointmentServiceType hospitalAppointmentServiceType,
                                                 Action action) {
        this.hospitalAppointmentServiceType = hospitalAppointmentServiceType;
        this.hospitalAppointmentServiceTypeContent = hospitalAppointmentServiceType.toString();
        this.action = action;
    }
}
