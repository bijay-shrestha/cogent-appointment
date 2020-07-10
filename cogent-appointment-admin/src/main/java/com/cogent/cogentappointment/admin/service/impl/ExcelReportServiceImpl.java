package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.jasper.transferLog.TransactionLogJasperData;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentLog.*;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.repository.AppointmentRepository;
import com.cogent.cogentappointment.admin.service.ExcelReportService;
import com.cogent.cogentappointment.commons.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DEPARTMENT_CONSULTATION_CODE;
import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DOCTOR_CONSULTATION_CODE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.INVALID_APPOINTMENT_SERVICE_TYPE_CODE;
import static com.cogent.cogentappointment.admin.constants.JasperReportFileConstants.JASPER_REPORT_TRANSACTION_LOG;
import static com.cogent.cogentappointment.admin.utils.jasperreport.GenerateExcelReportUtils.generateExcelReport;

/**
 * @author rupak ON 2020/07/09-12:54 PM
 */
@Service
@Transactional
@Slf4j
public class ExcelReportServiceImpl implements ExcelReportService {

    private final AppointmentRepository appointmentRepository;

    public ExcelReportServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void generateTransactionLogReport(TransactionLogSearchDTO searchRequestDTO,
                                             Pageable pageable) throws IOException, JRException {

        String appointmentServiceTypeCode = searchRequestDTO.getAppointmentServiceTypeCode().trim().toUpperCase();

        TransactionLogResponseDTO transactionLogs;

        switch (appointmentServiceTypeCode) {
            case DOCTOR_CONSULTATION_CODE:
                transactionLogs = appointmentRepository.searchDoctorAppointmentTransactionLogs(
                        searchRequestDTO, pageable, appointmentServiceTypeCode);
                break;

            case DEPARTMENT_CONSULTATION_CODE:
                transactionLogs = appointmentRepository.searchHospitalDepartmentTransactionLogs(
                        searchRequestDTO, pageable, appointmentServiceTypeCode);
                break;

            default:
                throw new BadRequestException(String.format(INVALID_APPOINTMENT_SERVICE_TYPE_CODE,
                        appointmentServiceTypeCode));
        }

        List<TransactionLogDTO> transactionLogDTOList = transactionLogs.getTransactionLogs();

        List<TransactionLogJasperData> jasperData = new ArrayList<>();

        transactionLogDTOList.forEach(transactionLogDTO -> {

            TransactionLogJasperData transactionLogJasperData = new TransactionLogJasperData();

            transactionLogJasperData.setAppointmentNumber(transactionLogDTO.getAppointmentNumber());
            transactionLogJasperData.setAppointmentDateTime(new SimpleDateFormat("yyyy/MM/dd").format(transactionLogDTO.getTransactionDate()) + ", " + transactionLogDTO.getAppointmentTime());
            transactionLogJasperData.setAppointmentTransactionDate(new SimpleDateFormat("yyyy/MM/dd").format(transactionLogDTO.getTransactionDate()) + ", " + transactionLogDTO.getTransactionTime());
            transactionLogJasperData.setTransactionDetails(transactionLogDTO.getTransactionNumber());
            transactionLogJasperData.setPatientDetails(transactionLogDTO.getPatientName() + ", " + StringUtil.toNormalCase(transactionLogDTO.getPatientGender().name()) + ", " + transactionLogDTO.getEsewaId());
            transactionLogJasperData.setRegistrationNumber(
                    (transactionLogDTO.getRegistrationNumber() == null) ?
                            "" : transactionLogDTO.getRegistrationNumber());
            transactionLogJasperData.setAddress(transactionLogDTO.getPatientAddress());
            transactionLogJasperData.setDoctorDetails(transactionLogDTO.getDoctorName() + "/" + transactionLogDTO.getSpecializationName());

            jasperData.add(transactionLogJasperData);

        });

        Map hParam = reportParamtersGenerator(searchRequestDTO, transactionLogs);

        generateExcelReport(jasperData,
                hParam,
                JASPER_REPORT_TRANSACTION_LOG);

    }

    private Map reportParamtersGenerator(TransactionLogSearchDTO searchRequestDTO, TransactionLogResponseDTO transactionLogs) {

        BookedAppointmentResponseDTO bookedInfo = transactionLogs.getBookedInfo();
        CheckedInAppointmentResponseDTO checkedInInfo = transactionLogs.getCheckedInInfo();
        RefundAppointmentResponseDTO refundInfo = transactionLogs.getRefundInfo();
//        transactionLogs.getRe
        CancelledAppointmentResponseDTO cancelledInfo = transactionLogs.getCancelledInfo();
        RevenueFromRefundAppointmentResponseDTO revenueFromRefundInfo = transactionLogs.getRevenueFromRefundInfo();

        Map hParam = new HashMap<String, String>();

        hParam.put("fromDate", new SimpleDateFormat("yyyy/MM/dd").format(searchRequestDTO.getFromDate()));
        hParam.put("toDate", new SimpleDateFormat("yyyy/MM/dd").format(searchRequestDTO.getToDate()));

        hParam.put("booked", "NPR " + bookedInfo.getBookedAmount() +
                " from " + bookedInfo.getBookedCount() + " Appt. " + "Follow-up " +
                "NPR " + bookedInfo.getFollowUpAmount() + " from " +
                bookedInfo.getFollowUpCount() + " Appt.");

        hParam.put("checkedIn", "NPR " + checkedInInfo.getCheckedInAmount() +
                " from " + checkedInInfo.getCheckedInCount() + " Appt. " +
                "Follow-up " + "NPR " + checkedInInfo.getFollowUpAmount() + " from " +
                checkedInInfo.getFollowUpCount() + " Appt.");

        hParam.put("refunded", "NPR " + refundInfo.getRefundedAmount() +
                " from " + refundInfo.getRefundedCount() +
                " Appt. " + "Follow-up " + "NPR " + refundInfo.getFollowUpAmount() +
                " from " + refundInfo.getFollowUpCount() + " Appt.");

        hParam.put("refundedToClient", "NPR " + "20" + " from " + "20 Appt. " + "Follow-up " + "NPR 0 " + " from 1 Appt.");

        hParam.put("cancelled", "NPR " + cancelledInfo.getCancelAmount() + " from " + cancelledInfo.getCancelledCount() + " Appt. " +
                "Follow-up " + "NPR " + cancelledInfo.getFollowUpAmount() + " from " + cancelledInfo.getFollowUpCount() + " Appt.");

        hParam.put("totalRevenue", "NPR " + revenueFromRefundInfo.getRevenueFromRefundAmount() + " from " + revenueFromRefundInfo.getRevenueFromRefundCount() + " Appt. " +
                "Follow-up " + "NPR " + revenueFromRefundInfo.getFollowUpAmount() + " from " + revenueFromRefundInfo.getFollowUpCount() + " Appt.");

        return hParam;
    }
}
