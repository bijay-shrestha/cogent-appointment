package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.TransactionLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.commons.dto.jasper.JasperReportDownloadResponse;
import org.springframework.data.domain.Pageable;

public interface ExcelReportService {

    JasperReportDownloadResponse generatePatientDetailsExcelReport(PatientSearchRequestDTO searchRequestDTO,
                                                                   Pageable pageable);

    JasperReportDownloadResponse generateTransactionLogReport(TransactionLogSearchDTO searchRequestDTO,
                                                              Pageable pageable);

    JasperReportDownloadResponse generateAppointmentLogExcelReport(AppointmentLogSearchDTO searchRequestDTO,
                                                                   Pageable pageable);

    JasperReportDownloadResponse generateRescheduleLogExcelReport(AppointmentRescheduleLogSearchDTO searchRequestDTO,
                                                                  Pageable pageable);

    JasperReportDownloadResponse generateAppointmentTransferLogReport(AppointmentTransferSearchRequestDTO searchRequestDTO,
                                                                      Pageable pageable);
}
