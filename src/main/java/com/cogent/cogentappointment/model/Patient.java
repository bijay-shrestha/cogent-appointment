package com.cogent.cogentappointment.model;

import com.cogent.cogentappointment.enums.Gender;
import com.cogent.cogentappointment.enums.Title;
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

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "is_self")
    private Character isSelf;

    @Column(name = "is_registered")
    private Character isRegistered;

    @Column(name = "esewa_id")
    private String esewaId;

    @Column(name = "address")
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nationality_id")
    private Nationality nationality;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospitalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tile")
    private Title title;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "status")
    private Character status;

    @Column(name = "remarks")
    private String remarks;
}

