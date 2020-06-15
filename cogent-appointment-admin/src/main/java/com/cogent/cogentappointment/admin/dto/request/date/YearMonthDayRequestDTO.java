package com.cogent.cogentappointment.admin.dto.request.date;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author nikesh(ERP)
 */
@Getter
@Setter
public class YearMonthDayRequestDTO implements Serializable {

    private Long id;

    private Integer year;

    private Integer zero;

    private Integer baisakh;

    private Integer jestha;

    private Integer ashar;

    private Integer shrawan;

    private Integer bhadra;

    private Integer ashwin;

    private Integer kartik;

    private Integer mangsir;

    private Integer poush;

    private Integer magh;

    private Integer falgun;

    private Integer chaitra;
}
