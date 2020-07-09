package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.service.ExcelReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author rupak ON 2020/07/09-12:54 PM
 */
@Service
@Transactional
@Slf4j
public class ExcelReportServiceImpl implements ExcelReportService {

    @Override
    public void generateTransactionLogReport() {

    }
}
