package com.cogent.cogentappointment.client.dto.response.university;

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

    private Character status;

    private String remarks;
}
