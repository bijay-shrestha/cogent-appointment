package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.DoctorQualificationEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 12/11/2019
 */
@Entity
@Table(name = "doctor_qualification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(DoctorQualificationEntityListener.class)
public class DoctorQualification extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "qualification_id")
    private Long qualificationId;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "DoctorQualification{" +
                "id=" + id +
                ", doctorId=" + doctorId +
                ", qualificationId=" + qualificationId +
                ", status=" + status +
                '}';
    }
}
