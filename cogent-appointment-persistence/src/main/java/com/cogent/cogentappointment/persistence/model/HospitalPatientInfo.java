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
 * THIS ENTITY IS USED TO DEFINE THE RELATIONSHIP BETWEEN PATIENT AND HOSPITAL
 * ONE PATIENT CAN VISIT MULTIPLE HOSPITALS
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    /* Y - PATIENT IS REGISTERED
    * N - PATIENT IS NOT REGISTERED
    * PATIENT IS REGISTERED WHEN HE/SHE CHECKS IN THE HOSPITAL
    * */
    @Column(name = "is_registered")
    private Character isRegistered;

    @Column(name = "hospital_number")
    private String hospitalNumber;

    /*REGISTRATION NUMBER IS GENERATED WHEN HE/SHE CHECKS IN THE HOSPITAL*/
    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "email", length = 50)
    private String email;

    /*Y -> IN CASE OF DEPT WISE APPOINTMENT
    * N -> IN CASE OF SELF APPOINTMENT
    * */
    @Column(name = "has_address")
    private Character hasAddress = 'N';

    @Column(name = "address")
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
    private Address province;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vdc_or_municipality_id")
    private Address vdcOrMunicipality;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private Address district;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id")
    private Address ward;

    /*PATIENT STATUS
    * Y = ACTIVE
    * N = INACTIVE (CAN BE UPDATED BY HOSPITAL ADMIN)
    * */
    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "HospitalPatientInfo{" +
                "id=" + id +
                ", hospital=" + hospital.getName() +
                ", patient=" + patient.getName() +
                ", isRegistered=" + isRegistered +
                ", hospitalNumber=" + hospitalNumber +
                ", registrationNumber=" + registrationNumber +
                ", email=" + email +
                ", address=" + address +
                ", status=" + status +
                ", remarks=" + remarks +
                '}';
    }

}
