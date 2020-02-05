package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.AdminEntityListener;
import com.cogent.cogentappointment.persistence.listener.DoctorSpecializationEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author smriti on 2019-09-29
 */
@Entity
@Table(name = "doctor_specialization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(DoctorSpecializationEntityListener.class)
public class DoctorSpecialization extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "specialization_id")
    private Long specializationId;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "DoctorSpecialization{" +
                "id=" + id +
                ", doctorId=" + doctorId +
                ", specializationId=" + specializationId +
                ", status=" + status +
                '}';
    }
}
