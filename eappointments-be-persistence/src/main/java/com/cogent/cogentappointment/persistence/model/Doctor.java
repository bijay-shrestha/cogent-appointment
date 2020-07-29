package com.cogent.cogentappointment.persistence.model;


import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.listener.DoctorEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti on 2019-09-27
 */
@Entity
@Table(name = "doctor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(DoctorEntityListener.class)
public class Doctor extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "salutation")
    private String salutation;

    @Column(name = "code", updatable = false)
    private String code;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "nmc_number")
    private String nmcNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salutation='" + salutation + '\'' +
                ", code='" + code + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", email='" + email + '\'' +
                ", nmcNumber='" + nmcNumber + '\'' +
                ", gender=" + gender +
                ", hospital=" + hospital.getName() +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
