package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HospitalPatientInfoEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti ON 10/02/2020
 */
@Entity
@Table(name = "hospital_patient_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(HospitalPatientInfoEntityListener.class)
public class HospitalPatientInfo extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hospital_id")
    private Long hospitalId;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "is_self")
    private Character isSelf;

    @Column(name = "is_registered")
    private Character isRegistered;

    @Column(name = "hospital_number")
    private String hospitalNumber;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "address")
    private String address;

    /*PATIENT STATUS*/
    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "HospitalPatientInfo{" +
                "id=" + id +
                ", hospitalId='" + hospitalId +
                ", patientId='" + patientId +
                ", isSelf=" + isSelf +
                ", isRegistered=" + isRegistered +
                ", hospitalNumber='" + hospitalNumber +
                ", registrationNumber='" + registrationNumber +
                ", email=" + email +
                ", address=" + address +
                ", status=" + status +
                ", remarks=" + remarks +
                '}';
    }

}
