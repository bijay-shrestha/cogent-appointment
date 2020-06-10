package com.cogent.cogentappointment.client.dto.response.date;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 * @author ayush.regmi
 */
@Getter
@Setter
public class YearMonthDaysResponseDTO implements Serializable {

    private Integer year;

    private Integer month;

    private Integer numberOfDays;
}
