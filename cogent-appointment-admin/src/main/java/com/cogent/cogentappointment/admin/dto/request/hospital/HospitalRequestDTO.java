package com.cogent.cogentappointment.admin.dto.request.hospital;

import com.cogent.cogentappointment.admin.constraintvalidator.SpecialCharacters;
import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
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
public class HospitalRequestDTO implements Serializable {

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String esewaMerchantCode;

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

    @NotNull
    @Status
    private Character status;

    @NotNull
    @Range(min = 0L, message = "Please select positive numbers Only")
    private Double refundPercentage;

    @NotNull
    @Range(min = 0L, message = "Please select positive numbers Only")
    private Integer numberOfAdmins;

    @NotEmpty
    private List<String> contactNumber;

    @Range(min = 0L, message = "Please select positive numbers Only")
    private Integer numberOfFollowUps;

    @Range(min = 0L, message = "Please select positive numbers Only")
    private Integer followUpIntervalDays;

    @NotNull
    @NotEmpty
    @NotBlank
    private String alias;

    private List<Long> billingModeId;

    private List<Long> appointmentServiceTypeIds;

    private Long primaryAppointmentServiceTypeId;

}

