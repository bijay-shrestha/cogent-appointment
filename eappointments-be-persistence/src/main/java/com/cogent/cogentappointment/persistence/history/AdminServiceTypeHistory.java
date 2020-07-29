package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AppointmentServiceType;
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
@Table(name = "admin_service_type_history")
public class AdminServiceTypeHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_service_type_id",
            foreignKey = @ForeignKey(name = "FK_appointment_service_type"))
    private AppointmentServiceType appointmentServiceType;

    @Column(name = "appointment_service_type_content")
    @Lob
    private String appointmentServiceTypeContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AdminServiceTypeHistory(AppointmentServiceType appointmentServiceType, Action action) {
        this.appointmentServiceType = appointmentServiceType;
        this.appointmentServiceTypeContent = appointmentServiceType.toString();
        this.action = action;
    }
}
