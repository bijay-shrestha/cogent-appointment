package com.cogent.cogentappointment.client.dto.response.common;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorMinResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 05/02/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSpecializationResponseDTO implements Serializable {

    private List<DoctorMinResponseDTO> doctorInfo;

    private List<DropDownResponseDTO> specializationInfo;
}
