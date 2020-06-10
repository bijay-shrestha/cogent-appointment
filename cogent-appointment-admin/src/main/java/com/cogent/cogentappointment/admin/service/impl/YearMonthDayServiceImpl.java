package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.repository.YearMonthDayRepository;
import com.cogent.cogentappointment.admin.service.YearMonthDayService;
import com.cogent.cogentappointment.persistence.model.YearMonthDay;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author nikesh
 */
@Service
@Transactional("transactionManager")
public class YearMonthDayServiceImpl implements YearMonthDayService {

    private final YearMonthDayRepository yearMonthDayRepository;

    public YearMonthDayServiceImpl(YearMonthDayRepository yearMonthDayRepository) {
        this.yearMonthDayRepository = yearMonthDayRepository;
    }

    @Override
    public YearMonthDay saveData(YearMonthDay data) {
        return yearMonthDayRepository.save(data);
    }

    @Override
    public YearMonthDay findByYear(Integer year) {
        return yearMonthDayRepository.findByYear(year);
    }

    @Override
    public List<YearMonthDay> findAll() {
        return yearMonthDayRepository.findAll();
    }

}
