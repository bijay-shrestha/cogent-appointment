package com.cogent.cogentappointment.admin.dto.response.company;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalContactNumberResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponseDTO extends AuditableResponseDTO implements Serializable {

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
}
