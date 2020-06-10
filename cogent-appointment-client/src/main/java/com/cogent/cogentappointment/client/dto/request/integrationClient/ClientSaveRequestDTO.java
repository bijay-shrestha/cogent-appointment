package com.cogent.cogentappointment.client.dto.request.integrationClient;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak on 2020-05-22
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientSaveRequestDTO implements Serializable {

    private String name;
    private int age;
    private int ageMonth;
    private int ageDay;
    private String sex;
    private String district;
    private String  vdc;
    private String wardNo;
    private String address;
    private String phoneNo;
    private String mobileNo;
    private String emailAddress;
    private String section;
    private String roomNo;
    private String appointmentNo;



}
