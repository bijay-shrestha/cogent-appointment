
package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.listener.PatientEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 14/01/2020
 */
@Entity
@Table(name = "patient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(PatientEntityListener.class)
public class Patient extends Auditable<String> implements Serializable {

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

    @Column(name = "eSewa_id")
    private String eSewaId;

    @Column(name = "cogent_id")
    private String cogentId;

    /*used to identify valid patient data
    * in case of agent: date of birth = merchant's created date */
    /*'Y'-> FOR AGENTS
    * 'N' -> FOR NORMAL PATIENTS*/
    @Column(name ="is_agent")
    private Character isAgent;

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", mobileNumber='" + mobileNumber + "'" +
                ", gender='" + gender + "'" +
                ", dateOfBirth='" + dateOfBirth + "'" +
                ", eSewaId='" + eSewaId + "'" +
                ", cogentId='" + cogentId + "'" +
                '}';
    }
}

