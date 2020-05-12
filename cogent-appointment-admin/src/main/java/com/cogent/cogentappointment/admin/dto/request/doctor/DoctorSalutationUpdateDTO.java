package com.cogent.cogentappointment.admin.dto.request.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSalutationUpdateDTO {

    private Long salutationId;

    private Character status;
}
