package com.cogent.cogentappointment.admin.dto.response.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Sauravi Thapa २०/२/९
 */
@Getter
@Setter
public class OverallRegisteredPatientsResponseDTO implements Serializable {

    private Long registeredPatient;

    private Character pillType;
}
