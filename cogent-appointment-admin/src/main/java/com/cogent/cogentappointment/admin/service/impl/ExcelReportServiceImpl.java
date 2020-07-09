package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.jasper.TransactionLogJasperData;
import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.repository.AppointmentRepository;
import com.cogent.cogentappointment.admin.repository.PatientRepository;
import com.cogent.cogentappointment.admin.service.ExcelReportService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DEPARTMENT_CONSULTATION_CODE;
import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DOCTOR_CONSULTATION_CODE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.INVALID_APPOINTMENT_SERVICE_TYPE_CODE;
import static com.cogent.cogentappointment.admin.utils.jasperreport.GenerateExcelReportUtils.generateExcelReport;

/**
 * @author rupak ON 2020/07/09-12:54 PM
 */
@Service
@Transactional
@Slf4j
public class ExcelReportServiceImpl implements ExcelReportService {

    private final AppointmentRepository appointmentRepository;

    private final PatientRepository patientRepository;

    public ExcelReportServiceImpl(AppointmentRepository appointmentRepository,
                                  PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
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

            transactionLogJasperData.setSerialNo("1");
            transactionLogJasperData.setAppointmentNumber(transactionLogDTO.getAppointmentNumber());
            transactionLogJasperData.setAppointmentDateTime(transactionLogDTO.getAppointmentDate() + " " + transactionLogDTO.getAppointmentTime());
            transactionLogJasperData.setAppointmentTransactionDate(transactionLogDTO.getTransactionDate() + " " + transactionLogDTO.getTransactionTime());
            transactionLogJasperData.setTransactionDetails(transactionLogDTO.getTransactionNumber());
            transactionLogJasperData.setPatientDetails(transactionLogDTO.getPatientName() + " " + transactionLogDTO.getPatientGender());
            transactionLogJasperData.setRegistrationNumber(transactionLogDTO.getRegistrationNumber());
            transactionLogJasperData.setAddress(transactionLogDTO.getPatientAddress());
            transactionLogJasperData.setDoctorDetails(transactionLogDTO.getDoctorName() + " " + transactionLogDTO.getSpecializationName());

            jasperData.add(transactionLogJasperData);

        });

        generateExcelReport(jasperData, null);

    }

    @Override
    public void generatePatientDetailsExcelReport(PatientSearchRequestDTO searchRequestDTO, Pageable pageable)
            throws IOException, JRException {

    }
}

//        Map hParam = new HashMap();

//        String patientName = "";
//        for (TransactionLogDTO logDTO : transactionLogDTOList) {
//
//            hParam.put("serialNo", "1");
//            hParam.put("appointmentNumber", logDTO.getAppointmentNumber());
//            hParam.put("appointmentDateTime", logDTO.getAppointmentDate() + " " + logDTO.getAppointmentTime());
//            hParam.put("appointmentTransactionDate", logDTO.getTransactionDate() + " " + logDTO.getTransactionTime());
//            hParam.put("transactionDetails", logDTO.getTransactionNumber());
//            hParam.put("patientDetails", logDTO.getPatientName() + " " + logDTO.getPatientGender());
//            hParam.put("registrationNumber", logDTO.getRegistrationNumber());
//            hParam.put("address", logDTO.getPatientAddress());
//            hParam.put("doctorDetails", logDTO.getDoctorName() + " " + logDTO.getSpecializationName());
//
//        }

