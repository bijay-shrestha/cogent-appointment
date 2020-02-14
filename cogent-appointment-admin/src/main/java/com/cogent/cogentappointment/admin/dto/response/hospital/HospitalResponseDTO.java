package com.cogent.cogentappointment.admin.dto.response.hospital;

import lombok.*;

import java.io.Serializable;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalResponseDTO implements Serializable {

    private Long id;

    private String name;

    private String hospitalCode;

    private Character status;

    private String address;

    private String panNumber;

    private String hospitalLogo;

    private String hospitalBanner;

    private String remarks;

    private Double refundPercentage;

<<<<<<< HEAD
    private Integer numberOfAdmins;
=======
    private Character isCogentAdmin;
>>>>>>> f8521fa793d42813374b9429f2c5822a9ef39ad9

    private List<HospitalContactNumberResponseDTO> contactNumberResponseDTOS;

    private Integer numberOfFreeFollowUps;

    private Integer followUpIntervalDays;
}
