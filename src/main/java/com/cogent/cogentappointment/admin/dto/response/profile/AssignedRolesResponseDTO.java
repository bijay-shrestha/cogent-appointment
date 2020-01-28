package com.cogent.cogentappointment.admin.dto.response.profile;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 19/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignedRolesResponseDTO implements Serializable {

    private Long parentId;

    private List<ChildMenusResponseDTO> childMenus;
}
