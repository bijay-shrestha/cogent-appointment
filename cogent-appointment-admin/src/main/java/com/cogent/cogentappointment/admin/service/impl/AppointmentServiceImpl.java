package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentLogSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.AppointmentPendingApprovalSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundRejectDTO;
import com.cogent.cogentappointment.admin.dto.request.appointment.refund.AppointmentRefundSearchDTO;
import com.cogent.cogentappointment.admin.dto.request.reschedule.AppointmentRescheduleDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.AppointmentPendingApprovalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.AppointmentStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.refund.AppointmentRefundResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.AppointmentRefundDetailRepository;
import com.cogent.cogentappointment.admin.repository.AppointmentRepository;
import com.cogent.cogentappointment.admin.service.AppointmentService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.APPROVED;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.AppointmentStatusConstants.REFUNDED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentLog.*;
import static com.cogent.cogentappointment.admin.utils.AppointmentUtils.parseRefundRejectDetails;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 2019-10-22
 */
@Service
@Transactional
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AppointmentRefundDetailRepository appointmentRefundDetailRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  AppointmentRefundDetailRepository appointmentRefundDetailRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentRefundDetailRepository = appointmentRefundDetailRepository;
    }

    @Override
    public AppointmentRefundResponseDTO fetchRefundAppointments(AppointmentRefundSearchDTO searchDTO,
                                                                Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_REFUND);

        AppointmentRefundResponseDTO refundAppointments =
                appointmentRepository.fetchRefundAppointments(searchDTO, pageable);

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_REFUND, getDifferenceBetweenTwoTime(startTime));

        return refundAppointments;
    }

    @Override
    public void approveRefundAppointment(Long appointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(APPROVE_PROCESS_STARTED, APPOINTMENT_REFUND);

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentId(appointmentId)
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        Appointment appointment = appointmentRepository.fetchAppointmentById(appointmentId)
                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(appointmentId));

        appointment.setStatus(REFUNDED);

        refundAppointmentDetail.setStatus(APPROVED);

        log.info(APPROVE_PROCESS_COMPLETED, APPOINTMENT_REFUND, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void rejectRefundAppointment(AppointmentRefundRejectDTO refundRejectDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REJECT_PROCESS_STARTED, APPOINTMENT_REFUND);

        AppointmentRefundDetail refundAppointmentDetail =
                appointmentRefundDetailRepository.findByAppointmentId(refundRejectDTO.getAppointmentId())
                        .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(refundRejectDTO.getAppointmentId()));

        parseRefundRejectDetails(refundRejectDTO, refundAppointmentDetail);

        log.info(REJECT_PROCESS_COMPLETED, APPOINTMENT_REFUND, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(
            AppointmentStatusRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT);

        List<AppointmentStatusResponseDTO> responseDTOS =
                appointmentRepository.fetchAppointmentForAppointmentStatus(requestDTO);

        log.info(FETCHING_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AppointmentPendingApprovalResponseDTO searchPendingVisitApprovals(
            AppointmentPendingApprovalSearchDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PENDING_APPROVAL_LIST);

        AppointmentPendingApprovalResponseDTO responseDTOS =
                appointmentRepository.searchPendingVisitApprovals(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, PENDING_APPROVAL_LIST, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AppointmentLogResponseDTO searchAppointmentLogs(AppointmentLogSearchDTO searchRequestDTO,
                                                           Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_LOG);

        AppointmentLogResponseDTO responseDTOS =
                appointmentRepository.searchAppointmentLogs(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AppointmentRefundResponseDTO rescheduleAppointment(AppointmentRescheduleDTO rescheduleDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_LOG);
//
//        Appointment appointment = appointmentRepository.fetchAppointmentById(rescheduleDTO.getAppointmentId())
//                .orElseThrow(() -> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(rescheduleDTO.getAppointmentId()));
//
//        appointment.setStatus(RESCHEDULED);
//
//

        AppointmentRefundResponseDTO responseDTOS =
                appointmentRepository.rescheduleAppointment(rescheduleDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_LOG, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_WITH_GIVEN_ID_NOT_FOUND = (appointmentId) -> {
        throw new NoContentFoundException(Appointment.class, "appointmentId", appointmentId.toString());
    };

}

