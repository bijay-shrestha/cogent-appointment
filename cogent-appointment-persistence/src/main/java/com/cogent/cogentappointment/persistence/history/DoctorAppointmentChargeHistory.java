package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.DoctorAppointmentCharge;
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
 * @author Sauravi Thapa २०/२/५
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "doctor_appointment_charge_history")
public class DoctorAppointmentChargeHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_appointment_charge_id",
            foreignKey = @ForeignKey(name = "FK_doctor_appointment_charge_history_doctor_appointment_charge"))
    private DoctorAppointmentCharge doctorAppointmentCharge;

    @Column(name = "doctor_appointment_charge_content")
    @Lob
    private String doctorAppointmentChargeContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public DoctorAppointmentChargeHistory(DoctorAppointmentCharge doctorAppointmentCharge, Action action) {
        this.doctorAppointmentCharge = doctorAppointmentCharge;
        this.doctorAppointmentChargeContent = doctorAppointmentCharge.toString();
        this.action = action;
    }
}
