package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.DoctorAppointmentChargeEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author smriti ON 13/01/2020
 */
@Entity
@Table(name = "doctor_appointment_charge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(DoctorAppointmentChargeEntityListener.class)
public class DoctorAppointmentCharge extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_charge")
    private Double appointmentCharge;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctorId")
    private Doctor doctorId;

    @Override
    public String toString() {
        return "DoctorAppointmentCharge{" +
                "id=" + id +
                ", appointmentCharge=" + appointmentCharge +
                ", doctorId=" + doctorId.getName() +
                '}';
    }
}
