package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AppointmentServiceTypeEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 26/05/20
 * eg. Department Consultation, Doctor Consultation
 */
@Entity
@Table(name = "appointment_service_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AppointmentServiceTypeEntityListener.class)
public class AppointmentServiceType extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "code", updatable = false)
    private String code;

    @Column(name = "is_default")
    private Character isDefault;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;
}
