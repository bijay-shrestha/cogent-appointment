package com.cogent.cogentappointment.client.dto.request.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 2019-09-27
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorUpdateRequestDTO implements Serializable {

    @Valid
    private DoctorUpdateDTO doctorInfo;

    @Valid
    @NotEmpty
    private List<DoctorSpecializationUpdateDTO> doctorSpecializationInfo;

    @Valid
    @NotEmpty
    private List<DoctorSalutationUpdateDTO> doctorSalutationInfo;private List<DoctorQualificationUpdateDTO> doctorQualificationInfo;


}
