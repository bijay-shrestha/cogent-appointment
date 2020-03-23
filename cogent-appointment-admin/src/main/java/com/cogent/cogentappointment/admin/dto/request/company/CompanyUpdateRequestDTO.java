package com.cogent.cogentappointment.admin.dto.request.company;

import com.cogent.cogentappointment.admin.constraintvalidator.SpecialCharacters;
import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalContactNumberUpdateRequestDTO;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyUpdateRequestDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    @SpecialCharacters
    private String name;

    private String companyCode;

    @NotNull
    @NotEmpty
    @SpecialCharacters
    private String address;

    @NotNull
    @NotEmpty
    @SpecialCharacters
    private String panNumber;

    @Status
    private Character status;

    @NotNull
    @NotEmpty
    private String remarks;

    @NotEmpty
    private List<CompanyContactNumberUpdateRequestDTO> contactNumberUpdateRequestDTOS;

    @NotNull
    @NotEmpty
    private String alias;
}
