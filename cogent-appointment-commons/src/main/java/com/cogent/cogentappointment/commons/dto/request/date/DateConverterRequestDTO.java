package com.cogent.cogentappointment.commons.dto.request.date;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author ayush.regmi(ERP)
 */
@Getter
@Setter
public class DateConverterRequestDTO implements Serializable {

    private Character convertTo;

    private Integer year, month, day;

    public DateConverterRequestDTO() {
    }

    public DateConverterRequestDTO(Character convertTo, Integer year, Integer month, Integer day) {
        this.convertTo = convertTo;
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
