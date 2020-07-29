package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalAppointmentServiceTypeEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 26/05/20
 */
@Entity
@Table(name = "hospital_appointment_service_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(HospitalAppointmentServiceTypeEntityListener.class)
public class HospitalAppointmentServiceType extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_service_type_id")
    private AppointmentServiceType appointmentServiceType;

    /* is_primary reflected after selecting a hospital
     during the appointment process  in eSewa.
     By default, it is 'N' and only one AppointmentServiceType can be primary
     */
    @Column(name = "is_primary")
    private Character isPrimary = 'N';

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;
}
