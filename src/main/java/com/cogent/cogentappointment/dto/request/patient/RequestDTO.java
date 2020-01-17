package com.cogent.cogentappointment.dto.request.patient;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa 9/23/19
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RequestDTO implements Serializable {

    private Long nationalityId;

    private Long insuranceCompanyId;

    private Long municipalityId;

    private Long surnameId;

    private  Long religionId;

    private Long maritalStatusId;

    private Long titleId;

    private Long categoryId;

}
