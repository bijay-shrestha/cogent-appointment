package com.cogent.cogentappointment.admin.dto.request.date;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bijay on 4/16/17.
 */
@Getter
@Setter
public class MonthAndWeekRequestDTO implements Serializable {
    private Integer year;
    private Integer month;
    private List<Integer> weeks;
}
