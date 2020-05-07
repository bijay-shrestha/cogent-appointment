package com.cogent.cogentappointment.client.dto.response.patient;

import com.cogent.cogentappointment.persistence.enums.Gender;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author smriti ON 18/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDTOForOthersWithStatus implements Serializable {

    private PatientResponseDTOForOthers minResponseDTOForOthers;

    private int responseCode;

    private HttpStatus responseStatus;
}
