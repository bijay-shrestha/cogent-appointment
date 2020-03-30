package com.cogent.cogentappointment.admin.dto.request.company;

import com.cogent.cogentappointment.admin.constraintvalidator.SpecialCharacters;
import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 12/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequestDTO implements Serializable {

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

    @NotNull
    @Status
    private Character status;

    @NotEmpty
    private List<String> contactNumber;

    @NotNull
    @NotEmpty
    private String alias;
}

