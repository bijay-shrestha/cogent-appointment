package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.AppointmentTransferSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.commons.dto.jasper.JasperReportDownloadResponse;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ExcelReportService {

    JasperReportDownloadResponse generateTransactionLogReport(TransactionLogSearchDTO searchRequestDTO,
                                                              Pageable pageable);

    JasperReportDownloadResponse generateAppointmentLogExcelReport(AppointmentLogSearchDTO searchRequestDTO,
                                                                   Pageable pageable);

    JasperReportDownloadResponse generateRescheduleLogExcelReport(AppointmentRescheduleLogSearchDTO searchRequestDTO,
                                                                  Pageable pageable);

    JasperReportDownloadResponse generateAppointmentTransferLogReport(AppointmentTransferSearchRequestDTO searchRequestDTO,
                                                                      Pageable pageable);
}
