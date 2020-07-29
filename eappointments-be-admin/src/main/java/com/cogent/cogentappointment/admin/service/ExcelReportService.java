package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.AppointmentTransferSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.commons.dto.jasper.JasperReportDownloadResponse;
import org.springframework.data.domain.Pageable;

public interface ExcelReportService {

    JasperReportDownloadResponse generatePatientDetailsExcelReport(PatientSearchRequestDTO searchRequestDTO);

    JasperReportDownloadResponse generateTransactionLogReport(TransactionLogSearchDTO searchRequestDTO);

    JasperReportDownloadResponse generateAppointmentLogExcelReport(AppointmentLogSearchDTO searchRequestDTO);

    JasperReportDownloadResponse generateRescheduleLogExcelReport(AppointmentRescheduleLogSearchDTO searchRequestDTO);

    JasperReportDownloadResponse generateAppointmentTransferLogReport(AppointmentTransferSearchRequestDTO searchRequestDTO);
}
