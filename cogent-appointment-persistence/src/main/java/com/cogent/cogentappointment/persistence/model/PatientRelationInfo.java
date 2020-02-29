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
 * THIS ENTITY IS USED ONLY WHEN APPOINTMENT IS DONE FOR OTHERS
 * ONE PARENT PATIENT CAN MAKE APPOINTMENT OF MULTIPLE OTHER PATIENTS (CHILD PATIENT ID- FRIENDS, RELATIVES, ETC)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_patient_id")
    private Patient parentPatientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_patient_id")
    private Patient childPatientId;

    /*OTHER PATIENT IS ACTIVE/DELETED*/
    /*DELETED - D (IF OTHER CARD IS DELETED)
    IF USER PROVIDES SAME INFORMATION AS ONE WHICH WAS DELETED PREVIOUSLY (USING +ADD NEW),
    THEN STATUS IS UPDATED AS ACTIVE AGAIN
    * ACTIVE -Y
    * */
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
