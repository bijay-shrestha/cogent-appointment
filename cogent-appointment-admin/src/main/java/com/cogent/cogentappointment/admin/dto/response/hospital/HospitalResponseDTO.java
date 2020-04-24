package com.cogent.cogentappointment.admin.dto.response.hospital;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalResponseDTO extends AuditableResponseDTO implements Serializable {

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

    private Integer numberOfAdmins;

    private Character isCompany;

    private List<HospitalContactNumberResponseDTO> contactNumberResponseDTOS;

    private Integer numberOfFollowUps;

    private Integer followUpIntervalDays;

    private String alias;
}
