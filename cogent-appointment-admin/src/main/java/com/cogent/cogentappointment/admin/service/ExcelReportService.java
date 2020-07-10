package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.commons.dto.jasper.JasperReportDownloadResponse;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Pageable;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ExcelReportService {

    JasperReportDownloadResponse generatePatientDetailsExcelReport(PatientSearchRequestDTO searchRequestDTO,
                                           Pageable pageable) throws IOException, JRException;

    JasperReportDownloadResponse generateTransactionLogReport(TransactionLogSearchDTO searchRequestDTO,
                                                              Pageable pageable) throws IOException, JRException;

    JasperReportDownloadResponse generateAppointmentLogExcelReport(AppointmentLogSearchDTO searchRequestDTO,
                                                                   Pageable pageable) throws FileNotFoundException, JRException;

    JasperReportDownloadResponse generateRescheduleLogExcelReport(AppointmentRescheduleLogSearchDTO searchRequestDTO,
                                                                  Pageable pageable) throws FileNotFoundException, JRException;
}
