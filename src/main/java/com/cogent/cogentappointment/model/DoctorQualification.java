package com.cogent.cogentappointment.model;

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
@NoArgsConstructor
@AllArgsConstructor
public class DoctorQualification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "qualification_id")
    private Long qualificationId;

    @Column(name = "status")
    private Character status;
}
