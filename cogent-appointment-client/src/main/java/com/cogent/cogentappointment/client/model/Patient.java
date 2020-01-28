package com.cogent.cogentappointment.client.model;

import com.cogent.cogentappointment.client.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 14/01/2020
 */
@Getter
@Setter
@Entity
@Table(name = "patient")
public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "mobile_number")
    @Size(max = 10)
    private String mobileNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "nepali_date_of_birth")
    private String nepaliDateOfBirth;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "is_self")
    private Character isSelf;

    @Column(name = "is_registered")
    private Character isRegistered;

    @Column(name = "eSewa_id")
    private String eSewaId;

    @Column(name = "address")
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospitalId;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;
}

