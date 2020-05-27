package com.cogent.cogentappointment.admin.dto.request.hospital;

import com.cogent.cogentappointment.admin.constraintvalidator.SpecialCharacters;
import com.cogent.cogentappointment.admin.constraintvalidator.Status;
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
public class HospitalUpdateRequestDTO implements Serializable {

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
    private String hospitalCode;

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

    @NotNull
    private Double refundPercentage;

    @NotNull
    private Integer numberOfAdmins;

    private Integer numberOfFollowUps;

    private Integer followUpIntervalDays;

    @NotEmpty
    private List<HospitalContactNumberUpdateRequestDTO> contactNumberUpdateRequestDTOS;

    /*Y-> NEW LOGO IS UPDATED
    * N-> LOGO IS SAME AS BEFORE. SO IF IT IS 'N', THEN NO NEED TO UPDATE LOGO
    */
    @NotNull
    @Status
    private Character isLogoUpdate;

    /*Y-> NEW BANNER IS UPDATED
   * N-> BANNER IS SAME AS BEFORE. SO IF IT IS 'N', THEN NO NEED TO UPDATE BANNER
   */
    private Character isBannerUpdate;

    /*only updated appointment service type from front-end*/
    @NotNull
    private List<HospitalAppointmentServiceTypeUpdateRequestDTO> appointmentServiceTypeUpdateRequestDTO;

    @NotNull
    private Long primaryAppointmentServiceTypeId;
}
