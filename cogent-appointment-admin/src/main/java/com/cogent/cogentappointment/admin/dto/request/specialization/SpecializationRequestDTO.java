package com.cogent.cogentappointment.admin.dto.request.specialization;

import com.cogent.cogentappointment.admin.constraintvalidator.SpecialCharacters;
import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 2019-09-25
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecializationRequestDTO implements Serializable {
    @NotNull
    @NotEmpty
    @SpecialCharacters
    private String name;

    @NotNull
    @Status
    private Character status;

    @NotNull
    private Long hospitalId;
}
