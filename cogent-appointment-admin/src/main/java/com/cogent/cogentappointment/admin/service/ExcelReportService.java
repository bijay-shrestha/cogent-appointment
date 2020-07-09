package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ExcelReportService {

    void generateTransactionLogReport(TransactionLogSearchDTO searchRequestDTO,
                                      Pageable pageable) throws IOException, JRException;
}
