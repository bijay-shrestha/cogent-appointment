package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.TransactionLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.transactionLog.TransactionLogResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.repository.AppointmentRepository;
import com.cogent.cogentappointment.admin.service.ExcelReportService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DEPARTMENT_CONSULTATION_CODE;
import static com.cogent.cogentappointment.admin.constants.CogentAppointmentConstants.AppointmentServiceTypeConstant.DOCTOR_CONSULTATION_CODE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.INVALID_APPOINTMENT_SERVICE_TYPE_CODE;
import static com.cogent.cogentappointment.admin.utils.jasperreport.JasperUtils.generateJasperReport;

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

        List<TransactionLogDTO> transactionLogDTOList=transactionLogs.getTransactionLogs();

        Map hParam = new HashMap();

        String patientName = "";
        for (TransactionLogDTO logDTO : transactionLogDTOList) {


            hParam.put("hospitalName", logDTO.getHospitalName());


        }

        generateJasperReport(transactionLogDTOList,hParam);

    }
}
