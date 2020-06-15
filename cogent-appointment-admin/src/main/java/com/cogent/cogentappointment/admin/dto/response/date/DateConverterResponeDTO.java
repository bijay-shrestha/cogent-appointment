package com.cogent.cogentappointment.admin.dto.response.date;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author ayush.regmi(ERP)
 */
@Getter
@Setter
public class DateConverterResponeDTO implements Serializable {

    private Character convertedTo;

    // 2074-1-10
    private Integer year, month, day;

    // 1 = Sunday
    private Integer dayNumber;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DateConverterResponeDTO) {
            DateConverterResponeDTO o = (DateConverterResponeDTO) obj;
            return this.year.equals(o.getYear()) &&
                    this.month.equals(o.getMonth()) && this.day.equals(o.getDay());
        }
        return false;
    }

    public DateConverterResponeDTO() {
    }

    public DateConverterResponeDTO(Integer year, Integer month, Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateConverterResponeDTO(Integer year, Integer month, Integer day, Integer dayNumber) {
        this(year, month, day);
        this.dayNumber = dayNumber;
    }

    public String getFormattedDate() {
        return this.year + "-" + this.month + "-" + this.day;
    }
}
