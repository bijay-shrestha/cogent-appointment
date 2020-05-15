package com.cogent.cogentappointment.esewa.dto.response.hospital;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 29/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalFollowUpResponseDTO implements Serializable {

    private Integer numberOfFollowUps;

    private Integer followUpIntervalDays;
}
