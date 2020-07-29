package com.cogent.cogentappointment.thirdparty.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckInRequestDTO implements Serializable {

    public String name;
    public Integer age;
    public Integer ageMonth;
    public Integer ageDay;
    public String sex;
    public String district;
    public String vdc;
    public String wardNo;
    public String address;
    public String phoneNo;
    public String mobileNo;
    public String emailAddress;
    public String section;
    public String roomNo;
    public String appointmentNo;
    public String patientId;
}
