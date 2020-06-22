package com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.refund;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak ON 2020/06/22-3:29 PM
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCancelApprovalResponse implements Serializable{

    private List<DepartmentCancelApprovalResponseDTO> response;

    private int totalItems;
}
