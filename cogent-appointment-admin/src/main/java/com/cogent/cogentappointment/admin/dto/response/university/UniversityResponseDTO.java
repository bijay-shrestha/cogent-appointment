package com.cogent.cogentappointment.admin.dto.response.university;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 11/11/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniversityResponseDTO implements Serializable {

    private String name;

    private String address;

    private String countryName;

    private Long countryId;

    private Long hospitalId;

    private String hospitalName;

    private Character status;

    private String remarks;
}
