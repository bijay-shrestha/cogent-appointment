package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.refund.refundStatus.RefundStatusSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.refundStatus.HospitalDepartmentAppointmentRefundDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.refundStatus.HospitalDepartmentRefundStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.refundStatus.RefundStatusResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.AppointmentRefundDetailRepository;
import com.cogent.cogentappointment.admin.repository.AppointmentRepository;
import com.cogent.cogentappointment.admin.repository.AppointmentTransactionDetailRepository;
import com.cogent.cogentappointment.admin.service.IntegrationCheckPointService;
import com.cogent.cogentappointment.admin.service.RefundStatusService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentthirdpartyconnector.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.APPOINTMENT_HAS_BEEN_REJECTED;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.REJECTED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentTransactionDetailLog.APPOINTMENT_TRANSACTION_DETAIL;
import static com.cogent.cogentappointment.admin.log.constants.RefundStatusLog.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author Sauravi Thapa ON 5/25/20
 */

@Service
@Transactional
@Slf4j
public class RefundStatusServiceImpl implements RefundStatusService {

    private final AppointmentRefundDetailRepository refundDetailRepository;

    private final AppointmentRepository appointmentRepository;

    private final AppointmentTransactionDetailRepository appointmentTransactionDetailRepository;

    private final IntegrationCheckPointService integrationCheckpointService;

    public RefundStatusServiceImpl(AppointmentRefundDetailRepository refundDetailRepository,
                                   AppointmentRepository appointmentRepository,
                                   AppointmentTransactionDetailRepository appointmentTransactionDetailRepository,
                                   IntegrationCheckPointService integrationCheckpointService) {
        this.refundDetailRepository = refundDetailRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentTransactionDetailRepository = appointmentTransactionDetailRepository;
        this.integrationCheckpointService = integrationCheckpointService;
    }

    @Override
    public RefundStatusResponseDTO searchRefundAppointments(RefundStatusSearchRequestDTO requestDTO,
                                                            Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, DOCTOR_REFUND_STATUS);

        RefundStatusResponseDTO response = refundDetailRepository.searchRefundAppointments(requestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, DOCTOR_REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));

        return response;
    }

    @Override
    public HospitalDepartmentRefundStatusResponseDTO searchHospitalDepartmentRefundAppointments(
            RefundStatusSearchRequestDTO requestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_REFUND_STATUS);

        HospitalDepartmentRefundStatusResponseDTO response=refundDetailRepository
                .searchHospitalDepartmentRefundAppointments(requestDTO,pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));

        return response;
    }

    @Override
    public void checkRefundStatus(RefundStatusRequestDTO requestDTO)
            throws IOException {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, REFUND_STATUS);

        AppointmentRefundDetail appointmentRefundDetail = getAppointmentRefundDetail(requestDTO);

        if (appointmentRefundDetail.getStatus().equals(REJECTED)) {
            throw new BadRequestException(APPOINTMENT_HAS_BEEN_REJECTED);
        }

        Appointment appointment = getAppointment(requestDTO);

        AppointmentTransactionDetail appointmentTransactionDetail = fetchAppointmentTransactionDetail(appointment.getId());

        integrationCheckpointService.apiIntegrationCheckpointForRefundStatus(appointment,
                appointmentRefundDetail,
                appointmentTransactionDetail,
                requestDTO);


        log.info(SAVING_PROCESS_COMPLETED, REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));
    }


    @Override
    public AppointmentRefundDetailResponseDTO fetchRefundDetailsById(Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_REFUND_STATUS);

        AppointmentRefundDetailResponseDTO refundAppointments = refundDetailRepository.fetchRefundDetailsById(appointmentId);

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));

        return refundAppointments;
    }

    @Override
    public HospitalDepartmentAppointmentRefundDetailResponseDTO fetchHospitalDepartmentRefundDetailsById(Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_REFUND_STATUS);

        HospitalDepartmentAppointmentRefundDetailResponseDTO response=refundDetailRepository
                .fetchHospitalDepartmentRefundDetailsById(appointmentId);

        log.info(FETCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_REFUND_STATUS, getDifferenceBetweenTwoTime(startTime));

        return response;
    }

    private AppointmentTransactionDetail fetchAppointmentTransactionDetail(Long appointmentId) {
        return appointmentTransactionDetailRepository.fetchByAppointmentId(appointmentId)
                .orElseThrow(() -> APPOINTMENT_TRANSACTION_DETAIL_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));
    }

    private AppointmentRefundDetail getAppointmentRefundDetail(RefundStatusRequestDTO requestDTO) {
        return refundDetailRepository.fetchAppointmentRefundDetail(requestDTO);
    }

    private Appointment getAppointment(RefundStatusRequestDTO requestDTO) {
        return appointmentRepository.fetchCancelledAppointmentDetails(requestDTO);
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_TRANSACTION_DETAIL_WITH_GIVEN_ID_NOT_FOUND = (appointmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_TRANSACTION_DETAIL, appointmentId);
        throw new NoContentFoundException(AppointmentTransactionDetail.class, "appointmentId", appointmentId.toString());
    };
}
