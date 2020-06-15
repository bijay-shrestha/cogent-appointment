package com.cogent.cogentappointment.admin.dto.response.date;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Nikesh Maharjan(ERP)
 */
@Getter
@Setter
public class YearMonthDetailResponseDTO implements Serializable {

    private Integer month;

    // 1 = Sunday, first day number of month
    private Integer dayNumber;

    // 31, total number of days in month
    private Integer numberOfDays;

}
