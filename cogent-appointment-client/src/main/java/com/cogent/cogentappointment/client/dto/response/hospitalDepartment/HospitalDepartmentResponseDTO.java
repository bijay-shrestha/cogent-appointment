package com.cogent.cogentappointment.client.dto.response.hospitalDepartment;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.common.AuditableResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorDropdownDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDepartmentResponseDTO extends AuditableResponseDTO implements Serializable {

    private Long id;

    private String name;

    private String code;

    private String description;

    private Character status;

    private String remarks;

    private List<DoctorDropdownDTO> doctorList;

    private List<DropDownResponseDTO> roomList;

    private List<BillingModeChargeResponse> billingModeChargeResponseList;


}
