package com.cogent.cogentappointment.esewa.dto.response.doctor;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 2019-09-27
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorDropdownDTO implements Serializable {

    private Long value;

    private String label;

    private String fileUri;
}
