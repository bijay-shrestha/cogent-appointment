package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.ApiIntegrationApproveRefundRequestDTO;
import com.cogent.cogentappointment.client.dto.request.clientIntegration.ApiIntegrationCheckInRequestDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.IntegrationService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.CogentAppointmentConstants.RefundResponseConstant.*;
import static com.cogent.cogentappointment.client.constants.StatusConstants.AppointmentStatusConstants.APPROVED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.client.utils.AppointmentUtils.parseRefundRejectDetails;
import static com.cogent.cogentappointment.client.utils.RefundStatusUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author rupak on 2020-05-19
 */
@Service
@Transactional
@Slf4j
public class IntegrationServiceImpl implements IntegrationService {

    private final IntegrationRepository integrationRepository;

    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;

    private final AppointmentRepository appointmentRepository;

    private final AppointmentRefundDetailRepository appointmentRefundDetailRepository;

    private final AppointmentTransactionDetailRepository appointmentTransactionDetailRepository;

    public IntegrationServiceImpl(IntegrationRepository integrationRepository,
                                  HospitalPatientInfoRepository hospitalPatientInfoRepository,
                                  AppointmentRepository appointmentRepository,
                                  AppointmentRefundDetailRepository appointmentRefundDetailRepository,
                                  AppointmentTransactionDetailRepository appointmentTransactionDetailRepository) {
        this.integrationRepository = integrationRepository;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
    }

    @Override
    public void approveAppointmentCheckIn(ApiIntegrationCheckInRequestDTO requestDTO) {

        Appointment appointment = appointmentRepository.fetchPendingAppointmentById(requestDTO.getAppointmentId())
                .orElseThrow(() -> APPOINTMENT_INFO_NOT_FOUND.apply(requestDTO.getAppointmentId()));


        if (requestDTO.isStatus()) {

            HospitalPatientInfo hospitalPatientInfo = hospitalPatientInfoRepository.
                    findByPatientAndHospitalId(appointment.getPatientId().getId(), appointment.getHospitalId().getId())
                    .orElseThrow(() -> HOSPITAL_PATIENT_INFO_NOT_FOUND.apply(appointment.getPatientId().getId()));

            hospitalPatientInfo.setHospitalNumber(requestDTO.getHospitalNumber());
        }

        appointment.setStatus(APPROVED);

        save(appointment);
    }

    @Override
    public void approveRefund(ApiIntegrationApproveRefundRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT_CANCEL_APPROVAL);

        Long appointmentId = requestDTO.getId();

        String response = requestDTO.getBody();

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentIdAndHospitalId
                        (appointmentId, getLoggedInHospitalId())
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        Appointment appointment = appointmentRepository.fetchRefundAppointmentByIdAndHospitalId
                (appointmentId, getLoggedInHospitalId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));


        updateAppointmentAndAppointmentRefundDetails(response, appointment, refundAppointmentDetail);

        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT_CANCEL_APPROVAL, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void rejectRefund(ApiIntegrationApproveRefundRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REJECT_PROCESS_STARTED, APPOINTMENT_CANCEL_APPROVAL);

        Long appointmentId = requestDTO.getId();

        String response = requestDTO.getBody();

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentIdAndHospitalId
                        (appointmentId, getLoggedInHospitalId())
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        Appointment appointment = appointmentRepository.fetchRefundAppointmentByIdAndHospitalId
                (appointmentId, getLoggedInHospitalId())
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        updateAppointmentAndAppointmentRefundDetails(response, appointment, refundAppointmentDetail);

        log.info(REJECT_PROCESS_COMPLETED, APPOINTMENT_CANCEL_APPROVAL, getDifferenceBetweenTwoTime(startTime));

    }

    private void updateAppointmentAndAppointmentRefundDetails(String response,
                                                              Appointment appointment,
                                                              AppointmentRefundDetail refundAppointmentDetail) {
        switch (response) {

            case PARTIAL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment, refundAppointmentDetail, response);
                break;

            case FULL_REFUND:
                changeAppointmentAndAppointmentRefundDetailStatus(appointment, refundAppointmentDetail, response);
                break;

            case SUCCESS:
                saveAppointmentRefundDetail(parseRefundRejectDetails(response, refundAppointmentDetail));
                break;

            case AMBIGIOUS:
                defaultAppointmentAndAppointmentRefundDetailStatusChanges(appointment, refundAppointmentDetail, response);
                break;

            default:
                defaultAppointmentAndAppointmentRefundDetailStatusChanges(appointment, refundAppointmentDetail, response);
                break;

        }
    }

    private void changeAppointmentAndAppointmentRefundDetailStatus(Appointment appointment,
                                                                   AppointmentRefundDetail refundAppointmentDetail,
                                                                   String remarks) {

        save(changeAppointmentStatus.apply(appointment, remarks));

        saveAppointmentRefundDetail(changeAppointmentRefundDetailStatus.apply(refundAppointmentDetail, remarks));

    }

    private void defaultAppointmentAndAppointmentRefundDetailStatusChanges(Appointment appointment,
                                                                           AppointmentRefundDetail refundAppointmentDetail,
                                                                           String remarks) {

        save(defaultAppointmentStatusChange.apply(appointment, remarks));

        saveAppointmentRefundDetail(defaultAppointmentRefundDetailStatusChange.apply(refundAppointmentDetail, remarks));

    }

    private void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    private void saveAppointmentRefundDetail(AppointmentRefundDetail appointmentRefundDetail) {
        appointmentRefundDetailRepository.save(appointmentRefundDetail);
    }

    private AppointmentTransactionDetail fetchAppointmentTransactionDetail(Long appointmentId) {
        return appointmentTransactionDetailRepository.fetchByAppointmentId(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_TRANSACTION_DETAIL, id);
        throw new NoContentFoundException(AppointmentTransactionDetail.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> APPOINTMENT_INFO_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Appointment.class);
    };

    private Function<Long, NoContentFoundException> HOSPITAL_PATIENT_INFO_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(HospitalPatientInfo.class);
    };

}
