package com.cogent.cogentappointment.admin.dto.request.company;

import com.cogent.cogentappointment.admin.constraintvalidator.SpecialCharacters;
import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalContactNumberUpdateRequestDTO;
import lombok.*;

import javax.validation.constraints.NotBlank;
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
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String companyCode;

    @NotNull
    @NotEmpty
    @SpecialCharacters
    @NotBlank
    private String address;

    @NotNull
    @NotEmpty
    @SpecialCharacters
    @NotBlank
    private String panNumber;

    @Status
    private Character status;

    @NotNull
    @NotEmpty
    @NotBlank
    private String remarks;

    /*Y-> NEW LOGO IS UPDATED
   * N-> LOGO IS SAME AS BEFORE. SO IF IT IS 'N', THEN NO NEED TO UPDATE LOGO
   */
    @NotNull
    @Status
    private Character isLogoUpdate;

    @NotEmpty
    private List<CompanyContactNumberUpdateRequestDTO> contactNumberUpdateRequestDTOS;

    @NotNull
    @NotEmpty
    @NotBlank
    private String alias;
}
