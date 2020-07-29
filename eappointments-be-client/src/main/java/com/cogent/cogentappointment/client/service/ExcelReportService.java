package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.TransactionLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.commons.dto.jasper.JasperReportDownloadResponse;
import org.springframework.data.domain.Pageable;

public interface ExcelReportService {

    JasperReportDownloadResponse generatePatientDetailsExcelReport(PatientSearchRequestDTO searchRequestDTO);

    JasperReportDownloadResponse generateTransactionLogReport(TransactionLogSearchDTO searchRequestDTO);

    JasperReportDownloadResponse generateAppointmentLogExcelReport(AppointmentLogSearchDTO searchRequestDTO);

    JasperReportDownloadResponse generateRescheduleLogExcelReport(AppointmentRescheduleLogSearchDTO searchRequestDTO);

    JasperReportDownloadResponse generateAppointmentTransferLogReport(AppointmentTransferSearchRequestDTO searchRequestDTO);
}
