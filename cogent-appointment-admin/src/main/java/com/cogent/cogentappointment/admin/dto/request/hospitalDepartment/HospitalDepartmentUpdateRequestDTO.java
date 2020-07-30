package com.cogent.cogentappointment.admin.dto.request.hospitalDepartment;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 24/01/2020
 */
@Getter
@Setter
public class HospitalDepartmentUpdateRequestDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull(message = "Client cannot be null")
    private Long hospitalId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String departmentNameInNepali;

    @NotNull
    @NotEmpty
    @NotBlank
    private String code;

    @NotNull
    @NotEmpty
    @NotBlank
    private String description;

    @Status
    private Character status;

    private List<DepartmentDoctorUpdateRequestDTO> doctorUpdateList;

    private List<DepartmentRoomUpdateRequestDTO> roomUpdateList;

    private List<BillingModeChargeUpdateDTO> billingModeChargeUpdateDTOS;

    @NotNull
    @NotEmpty
    @NotBlank
    private String remarks;
}
