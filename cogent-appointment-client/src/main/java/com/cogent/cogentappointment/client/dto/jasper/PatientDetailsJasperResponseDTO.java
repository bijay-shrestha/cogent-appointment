package com.cogent.cogentappointment.client.dto.jasper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sauravi Thapa ON 7/9/20
 */
@Getter
@Setter
@Builder
public class PatientDetailsJasperResponseDTO implements Serializable {

    List<PatientDetailsJasperResponse> responseList;

    private Integer totalItems;
}
