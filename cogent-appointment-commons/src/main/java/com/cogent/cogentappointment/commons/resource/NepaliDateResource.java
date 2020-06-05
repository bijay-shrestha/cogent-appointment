package com.cogent.cogentappointment.commons.resource;

import com.cogent.cogentappointment.commons.repository.YearMonthDayRepository;
import com.cogent.cogentappointment.commons.service.YearMonthDayService;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rupak ON 2020/06/05-11:25 AM
 */
@RestController
public class NepaliDateResource {

    private final YearMonthDayRepository yearMonthDayRepository;

    public NepaliDateResource(YearMonthDayRepository yearMonthDayRepository) {
        this.yearMonthDayRepository = yearMonthDayRepository;
    }


}
