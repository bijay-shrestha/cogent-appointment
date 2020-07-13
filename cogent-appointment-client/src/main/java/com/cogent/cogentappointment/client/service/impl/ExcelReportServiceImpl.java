package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.jasper.PatientDetailsJasperResponseDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.log.TransactionLogSearchDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.reschedule.AppointmentRescheduleLogSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.log.*;
import com.cogent.cogentappointment.client.dto.response.appointment.txnLog.TransactionLogDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.txnLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog.AppointmentTransferLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.reschedule.AppointmentRescheduleLogDTO;
import com.cogent.cogentappointment.client.dto.response.reschedule.AppointmentRescheduleLogResponseDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.repository.AppointmentRepository;
import com.cogent.cogentappointment.client.repository.AppointmentTransferRepository;
import com.cogent.cogentappointment.client.repository.PatientRepository;
import com.cogent.cogentappointment.client.service.ExcelReportService;
import com.cogent.cogentappointment.commons.dto.jasper.JasperReportDownloadResponse;
import com.cogent.cogentappointment.commons.dto.request.jasper.appointmentLog.AppointmentLogJasperData;
import com.cogent.cogentappointment.commons.dto.request.jasper.reshsceduleLog.RescheduleLogJasperData;
import com.cogent.cogentappointment.commons.dto.request.jasper.transactionLog.TransactionLogJasperData;
import com.cogent.cogentappointment.commons.dto.request.jasper.transferLog.AppointmentTransferLogJasperData;
import com.cogent.cogentappointment.commons.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DEPARTMENT_CONSULTATION_CODE;
import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DOCTOR_CONSULTATION_CODE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.INVALID_APPOINTMENT_SERVICE_TYPE_CODE;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;
import static com.cogent.cogentappointment.commons.constants.JasperReportFileConstants.*;
import static com.cogent.cogentappointment.commons.utils.jasperreport.GenerateExcelReportUtils.generateExcelReport;

/**
 * @author rupak ON 2020/07/09-12:54 PM
 */
@Service
@Transactional
@Slf4j
public class ExcelReportServiceImpl implements ExcelReportService {

    private final AppointmentRepository appointmentRepository;

    private final AppointmentTransferRepository appointmentTransferRepository;

    private final PatientRepository patientRepository;

    public ExcelReportServiceImpl(AppointmentRepository appointmentRepository,
                                  AppointmentTransferRepository appointmentTransferRepository,
                                  PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentTransferRepository = appointmentTransferRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public JasperReportDownloadResponse generatePatientDetailsExcelReport(PatientSearchRequestDTO searchRequestDTO,
                                                                          Pageable pageable) {

        PatientDetailsJasperResponseDTO patientDetailsJasperResponseDTO = patientRepository.getPatientDetailsForExcel
                (searchRequestDTO, pageable);

        Map hParam = new HashMap<String, String>();

        hParam.put(LOGO, JASPER_REPORT_EAPPOINTMENT_LOGO);

        return generateExcelReport(patientDetailsJasperResponseDTO.getResponseList(),
                hParam,
                JASPER_REPORT_PATIENT_DETAILS);

    }

    @Override
    public JasperReportDownloadResponse generateTransactionLogReport(TransactionLogSearchDTO searchRequestDTO,
                                                                     Pageable pageable) {

        String appointmentServiceTypeCode = searchRequestDTO.getAppointmentServiceTypeCode().trim().toUpperCase();

        TransactionLogResponseDTO transactionLogs;

        switch (appointmentServiceTypeCode) {
            case DOCTOR_CONSULTATION_CODE:
                transactionLogs = appointmentRepository.searchDoctorTransactionLogs(
                        searchRequestDTO,
                        pageable,
                        getLoggedInHospitalId(),
                        appointmentServiceTypeCode);
                break;

            case DEPARTMENT_CONSULTATION_CODE:
                transactionLogs = appointmentRepository.searchHospitalDepartmentTransactionLogs(
                        searchRequestDTO,
                        pageable,
                        getLoggedInHospitalId(),
                        appointmentServiceTypeCode);
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
            transactionLogJasperData.setTransactionDetails(transactionLogDTO.getTransactionNumber() + ", " + transactionLogDTO.getRefundAmount());
            transactionLogJasperData.setPatientDetails(transactionLogDTO.getPatientName() + ", " + StringUtil.toNormalCase(transactionLogDTO.getPatientGender().name()));
            transactionLogJasperData.setRegistrationNumber(
                    (transactionLogDTO.getRegistrationNumber() == null) ?
                            "" : transactionLogDTO.getRegistrationNumber());
            transactionLogJasperData.setEsewaId((transactionLogDTO.getEsewaId() == null) ?
                    "" : transactionLogDTO.getEsewaId());
            transactionLogJasperData.setAddress(transactionLogDTO.getPatientAddress());
            transactionLogJasperData.setDoctorDetails(transactionLogDTO.getDoctorName() + "/" + transactionLogDTO.getSpecializationName());

            jasperData.add(transactionLogJasperData);

        });

        Map hParam = reportParamtersGenerator(searchRequestDTO, transactionLogs);

        return generateExcelReport(jasperData,
                hParam,
                JASPER_REPORT_TRANSACTION_LOG);

    }

    @Override
    public JasperReportDownloadResponse generateAppointmentLogExcelReport(AppointmentLogSearchDTO searchRequestDTO,
                                                                          Pageable pageable) {
        String appointmentServiceTypeCode = searchRequestDTO.getAppointmentServiceTypeCode().trim().toUpperCase();

        AppointmentLogResponseDTO appointmentLogs;

        switch (appointmentServiceTypeCode) {
            case DOCTOR_CONSULTATION_CODE:
                appointmentLogs = appointmentRepository.searchDoctorAppointmentLogs(
                        searchRequestDTO,
                        pageable,
                        getLoggedInHospitalId(),
                        appointmentServiceTypeCode);
                break;

            case DEPARTMENT_CONSULTATION_CODE:
                appointmentLogs = appointmentRepository.searchHospitalDepartmentAppointmentLogs(
                        searchRequestDTO,
                        pageable,
                        getLoggedInHospitalId(),
                        appointmentServiceTypeCode);
                break;

            default:
                throw new BadRequestException(String.format(INVALID_APPOINTMENT_SERVICE_TYPE_CODE, appointmentServiceTypeCode));
        }

        List<AppointmentLogDTO> appointmentLogDTOList = appointmentLogs.getAppointmentLogs();

        List<AppointmentLogJasperData> jasperData = new ArrayList<>();

        appointmentLogDTOList.forEach(appointmentLogDTO -> {

            AppointmentLogJasperData appointmentLogJasperData = new AppointmentLogJasperData();

            appointmentLogJasperData.setAppointmentStatus(appointmentLogDTO.getStatus());
            appointmentLogJasperData.setAppointmentNumber(appointmentLogDTO.getAppointmentNumber());
            appointmentLogJasperData.setAppointmentDateTime(new SimpleDateFormat("yyyy/MM/dd").format(appointmentLogDTO.getTransactionDate()) +
                    ", " + appointmentLogDTO.getAppointmentTime());
            appointmentLogJasperData.setTransactionDetails(appointmentLogDTO.getTransactionNumber() +
                    ", " + appointmentLogDTO.getRefundAmount());
            appointmentLogJasperData.setPatientDetails(appointmentLogDTO.getPatientName() +
                    ", " + StringUtil.toNormalCase(appointmentLogDTO.getPatientGender().name()));
            appointmentLogJasperData.setRegistrationNumber(
                    (appointmentLogDTO.getRegistrationNumber() == null) ?
                            "" : appointmentLogDTO.getRegistrationNumber());
            appointmentLogJasperData.setEsewaId((appointmentLogDTO.getEsewaId() == null) ?
                    "" : appointmentLogDTO.getEsewaId());
            appointmentLogJasperData.setAddress(appointmentLogDTO.getPatientAddress());
            appointmentLogJasperData.setDoctorDetails(appointmentLogDTO.getDoctorName() + "/" + appointmentLogDTO.getSpecializationName());

            jasperData.add(appointmentLogJasperData);

        });

        Map hParam = appointmentLogReportParamtersGenerator(searchRequestDTO, appointmentLogs);

        return generateExcelReport(jasperData,
                hParam,
                JASPER_REPORT_APPOINTMENT_LOG);


    }

    @Override
    public JasperReportDownloadResponse generateRescheduleLogExcelReport(AppointmentRescheduleLogSearchDTO searchRequestDTO,
                                                                         Pageable pageable) {

        AppointmentRescheduleLogResponseDTO responseDTOS =
                appointmentRepository.fetchRescheduleAppointment(
                        searchRequestDTO,
                        pageable,
                        getLoggedInHospitalId());


        List<AppointmentRescheduleLogDTO> rescheduleLogDTO = responseDTOS.getAppointmentRescheduleLogDTOS();

        List<RescheduleLogJasperData> jasperData = new ArrayList<>();

        rescheduleLogDTO.forEach(rescheduleLog -> {

            RescheduleLogJasperData rescheduleLogJasperData = RescheduleLogJasperData.builder()
                    .appointmentNumber(rescheduleLog.getAppointmentNumber())
                    .appointmentDateTime(new SimpleDateFormat("yyyy/MM/dd, hh:mm a").
                            format(rescheduleLog.getRescheduleAppointmentDate()))
                    .rescheduleDate(new SimpleDateFormat("yyyy/MM/dd, hh:mm a").
                            format(rescheduleLog.getRescheduleAppointmentDate()))
                    .patientDetails(rescheduleLog.getPatientName() + ", " + StringUtil.
                            toNormalCase(rescheduleLog.getPatientGender().name()))
                    .registrationNumber(
                            (rescheduleLog.getRegistrationNumber() == null) ?
                                    "" : rescheduleLog.getRegistrationNumber())
                    .esewaId((rescheduleLog.getEsewaId() == null) ?
                            "" : rescheduleLog.getEsewaId())
                    .doctorDetails(rescheduleLog.getDoctorName() + "/" + rescheduleLog.getSpecializationName())
                    .build();

            jasperData.add(rescheduleLogJasperData);

        });

        Map hParam = new HashMap<String, String>();

        hParam.put("fromDate", new SimpleDateFormat("yyyy/MM/dd").format(searchRequestDTO.getFromDate()));
        hParam.put("toDate", new SimpleDateFormat("yyyy/MM/dd").format(searchRequestDTO.getToDate()));


        return generateExcelReport(jasperData, hParam, JASPER_REPORT_RESHCEDULE_LOG);
    }

    @Override
    public JasperReportDownloadResponse generateAppointmentTransferLogReport(
            AppointmentTransferSearchRequestDTO searchRequestDTO,
            Pageable pageable) {

        AppointmentTransferLogResponseDTO appointmentTransferLogDTOS = appointmentTransferRepository.
                getApptTransferredList(searchRequestDTO, pageable);


        List<AppointmentTransferLogJasperData> jasperData = new ArrayList<>();

        appointmentTransferLogDTOS.getResponse().forEach(transferLog -> {

            AppointmentTransferLogJasperData transferLogJasperData = AppointmentTransferLogJasperData.builder()
                    .appointmentStatus(transferLog.getStatus())
                    .appointmentNumber(transferLog.getApptNumber())
                    .transferFromDateTime(transferLog.getTransferredFromDate() +
                            ", " +
                            transferLog.getTransferredFromTime())
                    .transferToDateTime(transferLog.getTransferredToDate() +
                            ", " +
                            transferLog.getTransferredToTime())
                    .patientDetails(transferLog.getPatientName() +
                            ", " +
                            StringUtil.toNormalCase(transferLog.getGender().name()
                                    + ", " + transferLog.getMobileNumber()))
                    .transferFromDoctor(transferLog.getTransferredFromDoctor()
                            + "/" +
                            transferLog.getTransferredFromSpecialization())
                    .transferToDoctor(transferLog.getTransferredToDoctor() +
                            "/" +
                            transferLog.getTransferredToSpecialization())
                    .build();

            jasperData.add(transferLogJasperData);

        });

        Map hParam = new HashMap<String, String>();

//        hParam.put("fromDate", new SimpleDateFormat("yyyy/MM/dd").format(searchRequestDTO.getAppointmentFromDate()));
//        hParam.put("toDate", new SimpleDateFormat("yyyy/MM/dd").format(searchRequestDTO.getAppointmentToDate()));

        hParam.put("fromDate", "2019/06/10");
        hParam.put("toDate", "2020/01/02");
        return generateExcelReport(jasperData, hParam, JASPER_REPORT_TRANSFER_LOG);
    }

    private Map appointmentLogReportParamtersGenerator(AppointmentLogSearchDTO searchRequestDTO,
                                                       AppointmentLogResponseDTO appointmentLogResponseDTO) {

        BookedAppointmentResponseDTO bookedInfo = appointmentLogResponseDTO.getBookedInfo();
        CheckedInAppointmentResponseDTO checkedInInfo = appointmentLogResponseDTO.getCheckedInInfo();
        RefundAppointmentResponseDTO refundInfo = appointmentLogResponseDTO.getRefundInfo();
//        transactionLogs.getRe
        CancelledAppointmentResponseDTO cancelledInfo = appointmentLogResponseDTO.getCancelledInfo();
        RevenueFromRefundAppointmentResponseDTO revenueFromRefundInfo = appointmentLogResponseDTO.getRevenueFromRefundInfo();

        Map hParam = new HashMap<String, String>();

        dynamicStataticsGenerator(hParam,
                searchRequestDTO.getFromDate(),
                searchRequestDTO.getToDate(),
                bookedInfo,
                checkedInInfo,
                refundInfo,
                cancelledInfo,
                revenueFromRefundInfo);


        return hParam;
    }

    private Map reportParamtersGenerator(TransactionLogSearchDTO searchRequestDTO, TransactionLogResponseDTO transactionLogs) {

        BookedAppointmentResponseDTO bookedInfo = transactionLogs.getBookedInfo();
        CheckedInAppointmentResponseDTO checkedInInfo = transactionLogs.getCheckedInInfo();
        RefundAppointmentResponseDTO refundInfo = transactionLogs.getRefundInfo();
//        transactionLogs.getRe
        CancelledAppointmentResponseDTO cancelledInfo = transactionLogs.getCancelledInfo();
        RevenueFromRefundAppointmentResponseDTO revenueFromRefundInfo = transactionLogs.getRevenueFromRefundInfo();

        Map hParam = new HashMap<String, String>();

        dynamicStataticsGenerator(hParam,
                searchRequestDTO.getFromDate(),
                searchRequestDTO.getToDate(),
                bookedInfo,
                checkedInInfo,
                refundInfo,
                cancelledInfo,
                revenueFromRefundInfo);

        return hParam;
    }


    private void dynamicStataticsGenerator(Map hParam,
                                           Date fromDate,
                                           Date toDate,
                                           BookedAppointmentResponseDTO bookedInfo,
                                           CheckedInAppointmentResponseDTO checkedInInfo,
                                           RefundAppointmentResponseDTO refundInfo,
                                           CancelledAppointmentResponseDTO cancelledInfo, RevenueFromRefundAppointmentResponseDTO revenueFromRefundInfo) {

        hParam.put("fromDate", new SimpleDateFormat("yyyy/MM/dd").format(fromDate));
        hParam.put("toDate", new SimpleDateFormat("yyyy/MM/dd").format(toDate));

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

    }

}
