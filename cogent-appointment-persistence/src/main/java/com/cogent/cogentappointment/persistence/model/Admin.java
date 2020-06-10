package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.listener.AdminEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/*WHEN ADMIN IS FIRST CREATED, ITS STATUS INITIALLY IS INACTIVE.
 WHEN HE/SHE SAVES PASSWORD EITHER THROUGH EMAIL VERIFICATION
  OR SOMEONE ELSE RESET ITS PASSWORD, ONLY THEN ADMIN WILL BE ACTIVE.
 INACTIVE ADMINS CANNOT USE FORGOT PASSWORD FEATURE AND LOGIN INTO APPLICATION.

 WHEN INACTIVE ADMIN IS UPDATED (MAINLY IF STATUS IS SET AS ACTIVE),
 FIRST CHECK IF THAT ADMIN IS ACTIVATED (MEANING IT HAS SAVED ITS PASSWORD)
  */
@Table(name = "admin")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AdminEntityListener.class)
public class Admin extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "password")
    private String password;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    /*INITIALLY, AFTER SAVING ADMIN, THIS FLAG IS 'N'
    * AND AFTER IT ACTIVATES ACCOUNT (SAVES PASSWORD FROM CONFIRMATION URL, THIS FLAG IS SET AS 'Y').
    *
    * SUPPOSE ADMIN IS CREATED SUCCESSFULLY BUT IT HAS NOT SAVED ITS PASSWORD,
    * BUT ATTEMPTS TO UPDATE PASSWORD USING 'FORGOT PASSWORD' FEATURE. TO AVOID THIS, THIS FLAG IS MAINTAINED.
    * */
    @Column(name = "status")
    private Character status;

    @Column(name = "has_mac_binding")
    private Character hasMacBinding;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "remarks")
    private String remarks;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profileId;

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", status=" + status +
                ", hasMacBinding=" + hasMacBinding +
                ", gender=" + gender +
                ", remarks='" + remarks + '\'' +
                ", profileId=" + profileId.getName() +
                '}';
    }
}



