package com.cogent.cogentappointment.client.dto.response.date;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author nikesh
 */
@Getter
@Setter
public class YearMonthDayResponseDTO implements Serializable {

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
