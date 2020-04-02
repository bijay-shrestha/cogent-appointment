package com.cogent.cogentappointment.esewa.dto.eSewa;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 15/03/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDatesWithSpecialization implements Serializable {
    private Long specializationId;

    private String SpecilaizationName;

    private List<Date> avaliableDates;
}
