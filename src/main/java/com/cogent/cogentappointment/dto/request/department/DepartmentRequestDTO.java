package com.cogent.cogentappointment.dto.request.department;

import com.cogent.cogentappointment.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * @author Sauravi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentRequestDTO implements Serializable {

    @NotNull
    @NotBlank
    @NotEmpty

    private String name;

    @NotNull
    @NotBlank
    @NotEmpty
    private String code;

    @NotNull
    @Status
    private Character status;
}
