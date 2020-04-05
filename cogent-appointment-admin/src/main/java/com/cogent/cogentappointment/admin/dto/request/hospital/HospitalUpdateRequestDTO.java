package com.cogent.cogentappointment.admin.dto.request.hospital;

import com.cogent.cogentappointment.admin.constraintvalidator.SpecialCharacters;
import com.cogent.cogentappointment.admin.constraintvalidator.Status;
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
public class HospitalUpdateRequestDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    @SpecialCharacters
    private String name;

    private String hospitalCode;

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

    @NotNull
    private Double refundPercentage;

    @NotNull
    private Integer numberOfAdmins;

    private Integer numberOfFollowUps;

    private Integer followUpIntervalDays;

    @NotEmpty
    private List<HospitalContactNumberUpdateRequestDTO> contactNumberUpdateRequestDTOS;

    private String alias;
}
