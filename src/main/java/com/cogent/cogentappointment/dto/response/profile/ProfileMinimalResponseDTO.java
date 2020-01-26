package com.cogent.cogentappointment.dto.response.profile;

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
@ToString
public class ProfileMinimalResponseDTO implements Serializable {

    private Long id;

    private String name;

    private Character status;

    private String departmentName;

    private Integer totalItems;
}
