package com.cogent.cogentappointment.dto.request.specialization;

import com.cogent.cogentappointment.constraintvalidator.Status;
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
public class SpecializationUpdateRequestDTO implements Serializable {
    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @NotEmpty
    private String remarks;
}
