package com.cogent.cogentappointment.esewa.service;


import com.cogent.cogentappointment.persistence.model.YearMonthDay;

import java.util.List;

/**
 * @author nikesh
 */
public interface YearMonthDayService {

    YearMonthDay saveData(YearMonthDay data);

    YearMonthDay findByYear(Integer year);

    List<YearMonthDay> findAll();

}
