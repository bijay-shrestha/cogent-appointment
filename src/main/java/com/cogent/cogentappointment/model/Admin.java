package com.cogent.cogentappointment.model;

import com.cogent.cogentappointment.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "admin")
@Entity
@Getter
@Setter
public class Admin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "username", length = 50, updatable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "status")
    private Character status;

    @Column(name = "is_first_login")
    private Character isFirstLogin;

    @Column(name = "has_mac_binding")
    private Character hasMacBinding;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name ="remarks")
    private String remarks;
}



