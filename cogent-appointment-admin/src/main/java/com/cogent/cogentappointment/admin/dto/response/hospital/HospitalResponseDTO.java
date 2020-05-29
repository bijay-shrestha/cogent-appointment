package com.cogent.cogentappointment.admin.dto.response.hospital;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
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

    private Integer numberOfAdmins;

    private Character isCompany;

    private List<HospitalContactNumberResponseDTO> contactNumberResponseDTOS;

    private Integer numberOfFollowUps;

    private Integer followUpIntervalDays;

    private String alias;

    private List<DropDownResponseDTO> billingMode;

    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY 'at' hh:mm a", timezone = "Asia/Kathmandu")
    private Date createdDate;

    private String lastModifiedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY 'at' hh:mm a", timezone = "Asia/Kathmandu")
    private Date lastModifiedDate;
}
