package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.listener.ThirdPartyInfoEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "third_party_info")
@Getter
@Service
@NoArgsConstructor
@ToString
@AllArgsConstructor
@EntityListeners(ThirdPartyInfoEntityListener.class)
public class ThirdPartyInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "age")
    public Integer age;

    @Column(name = "ageMonth")
    public Integer ageMonth;

    @Column(name = "ageDay")
    public Integer ageDay;

    @Column(name = "sex")
    public String sex;

    @Column(name = "district")
    public String district;

    @Column(name = "vdc")
    public String vdc;

    @Column(name = "wardNo")
    public String wardNo;

    @Column(name = "address")
    public String address;

    @Column(name = "phoneNo")
    public String phoneNo;

    @Column(name = "mobileNo")
    public String mobileNo;

    @Column(name = "emailAddress")
    public String emailAddress;

    @Column(name = "section")
    public String section;

    @Column(name = "roomNo")
    public String roomNo;

    @Column(name = "appointmentNo")
    public String appointmentNo;

    @Column(name = "patientId")
    public String patientId;
}
