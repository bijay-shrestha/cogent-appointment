package com.cogent.cogentappointment.admin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author smriti ON 12/01/2020
 */
@Entity
@Table(name = "hospital_contact_number")
@Getter
@Setter
public class HospitalContactNumber implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "hospital_id")
    private Long hospitalId;

    @Column(name = "status")
    private Character status;
}
