package com.cogent.cogentappointment.client.dto.response.university;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 11/11/2019
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversityMinimalResponseDTO implements Serializable {

    private Long id;

    private String name;

    private String address;

    private Long countryId;

    private String countryName;

    private Character status;

    private int totalItems;

}
