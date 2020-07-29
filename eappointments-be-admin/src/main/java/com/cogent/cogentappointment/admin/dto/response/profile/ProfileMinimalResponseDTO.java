package com.cogent.cogentappointment.admin.dto.response.profile;

import lombok.*;

import java.io.Serializable;
/**
 * @author smriti on 7/10/19
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileMinimalResponseDTO implements Serializable {

    private Long id;

    private String name;

    private Character status;

    private String departmentName;

    private String hospitalName;

    private Integer totalItems;
}
