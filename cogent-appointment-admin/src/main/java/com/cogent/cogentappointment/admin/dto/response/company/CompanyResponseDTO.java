package com.cogent.cogentappointment.admin.dto.response.company;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalContactNumberResponseDTO;
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
public class CompanyResponseDTO implements Serializable {

    private Long id;

    private String name;

    private String companyCode;

    private Character status;

    private String address;

    private String panNumber;

    private String companyLogo;

    private String remarks;

    private Character isCompany;

    private List<CompanyContactNumberResponseDTO> contactNumberResponseDTOS;

    private String alias;

    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd YYYY hh:MM a",timezone = "Asia/Kathmandu")
    private Date createdDate;

    private String lastModifiedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd YYYY hh:MM a",timezone = "Asia/Kathmandu")
    private Date lastModifiedDate;
}
