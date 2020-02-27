package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.PatientRelationInfoEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 26/02/20
 */
@Entity
@Table(name = "patient_relation_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(PatientRelationInfoEntityListener.class)
public class PatientRelationInfo extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_patient_id")
    private Long parentPatientId;

    @Column(name = "child_patient_id")
    private Long childPatientId;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "PatientRelationInfo{" +
                "id=" + id +
                ", parentPatientId=" + parentPatientId +
                ", childPatientId=" + childPatientId +
                ", status =" + status +
                '}';
    }
}
