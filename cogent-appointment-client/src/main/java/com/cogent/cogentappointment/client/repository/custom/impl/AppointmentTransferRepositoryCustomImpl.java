package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.appointmentTransfer.AppointmentTransferSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog.AppointmentTransferLogDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog.AppointmentTransferLogResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog.LastModifiedAppointmentIdAndStatus;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.AppointmentTransferLog.previewDTO.AppointmentTransferPreviewResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableDates.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableDates.OverrideDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableTime.ActualDateAndTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableTime.OverrideDateAndTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableTime.StartTimeAndEndTimeDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.charge.AppointmentChargeResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AppointmentTransferRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentTransfer;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.constants.QueryConstants.AppointmentConstants.APPOINTMENT_ID;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.AppointmentTransferLog.APPOINTMENT_TRANSFER;
import static com.cogent.cogentappointment.client.log.constants.DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER;
import static com.cogent.cogentappointment.client.query.AppointmentTransferQuery.*;
import static com.cogent.cogentappointment.client.utils.AppointmentTransferUtils.mergeCurrentAppointmentStatus;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author Sauravi Thapa ON 5/6/20
 */

@Repository
@Slf4j
@Transactional(readOnly = true)
public class AppointmentTransferRepositoryCustomImpl implements AppointmentTransferRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DoctorDatesResponseDTO> getDutyRosterByDoctorIdAndSpecializationId(Long doctorId, Long specializationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DATES_BY_DOCTOR_ID_AND_SPECIALIZATION_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        List<DoctorDatesResponseDTO> response = transformQueryToResultList(query, DoctorDatesResponseDTO.class);

        return response;
    }

    @Override
    public List<String> getDayOffDaysByRosterId(Long doctorDutyRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_DAY_OFF_WEEKS_BY_DUTY_ROSTER_ID)
                .setParameter(DOCTOR_DUTY_ROSTER_ID, doctorDutyRosterId);

        List<String> response = query.getResultList();

        return response;
    }

    @Override
    public StartTimeAndEndTimeDTO getWeekDaysByRosterIdAndCode(Long doctorDutyRosterId, String code) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_WEEKS_TIME_BY_DOCTOR_ID)
                .setParameter(DOCTOR_DUTY_ROSTER_ID, doctorDutyRosterId)
                .setParameter(CODE, code);

        try {
            return transformQueryToSingleResult(query, StartTimeAndEndTimeDTO.class);
        } catch (NoResultException e) {
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();
        }

    }

    @Override
    public List<OverrideDatesResponseDTO> getOverrideDatesByDoctorId(Long doctorId, Long specializationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_OVERRIDE_DATES_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        List<OverrideDatesResponseDTO> response = transformQueryToResultList(
                query, OverrideDatesResponseDTO.class);

        return response;
    }

    @Override
    public List<ActualDateAndTimeResponseDTO> getActualTimeByDoctorId(Long doctorId, Long specializationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DATE_AND_TIME_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        List<ActualDateAndTimeResponseDTO> response = transformQueryToResultList(
                query, ActualDateAndTimeResponseDTO.class);

        if (response.isEmpty()) {
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();
        }

        return response;
    }

    @Override
    public List<String> getUnavailableTimeByDateAndDoctorId(Long doctorId, Long specializationId, Date date) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_UNAVAILABLE_TIME)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(DATE, date)
                .setParameter(HOSPITAL_ID,getLoggedInHospitalId());

        List<String> response = query.getResultList();

        return response;
    }

    @Override
    public AppointmentChargeResponseDTO getAppointmentChargeByDoctorId(Long doctorId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_DOCTOR_CHARGE_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID, doctorId);

        AppointmentChargeResponseDTO response = transformQueryToSingleResult(
                query, AppointmentChargeResponseDTO.class);

        return response;
    }

    @Override
    public List<OverrideDateAndTimeResponseDTO> getOverideRosterDateAndTime(Long doctorId, Long specializationId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_OVERRIDE_DATE_AND_TIME_BY_DOCTOR_ID)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        List<OverrideDateAndTimeResponseDTO> response = transformQueryToResultList(
                query, OverrideDateAndTimeResponseDTO.class);

        return response;

    }

    @Override
    public AppointmentTransferLogResponseDTO getApptTransferredList(AppointmentTransferSearchRequestDTO requestDTO,
                                                                    Pageable pageable) {

        AppointmentTransferLogResponseDTO appointmentTransferLogResponseDTO = new AppointmentTransferLogResponseDTO();

        Long hospitalId=getLoggedInHospitalId();

        Query query = createQuery.apply(entityManager, QUERY_TO_GET_CURRENT_TRANSFERRED_DETAIL(requestDTO))
                .setParameter(HOSPITAL_ID,hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

//        Query queryToGetCurretAppointment=createQuery.apply(entityManager,
//                QUERY_TO_GET_CURRENT_APPOINTMENT_INFOS(requestDTO));
//
//        List<LastModifiedAppointmentIdAndStatus> currentDetails=transformQueryToResultList(
//                queryToGetCurretAppointment, LastModifiedAppointmentIdAndStatus.class);

        Query queryToGetTransferredAppointmentId = createQuery.apply(entityManager,
                QUERY_TO_GET_LIST_OF_TRANSFERRED_APPOINTMENT_FROM_ID)
                .setParameter(HOSPITAL_ID,hospitalId);

        List<Long> appointmentIds = queryToGetTransferredAppointmentId.getResultList();

        List<LastModifiedAppointmentIdAndStatus> lastModifiedAppointmentIdAndStatuses = new ArrayList<>();

        appointmentIds.forEach(appointmentId -> {

            Query query1 = createQuery.apply(entityManager,
                    QUERY_TO_GET_LASTEST_APPOINTMENT_TRANSFERRED_ID_AND_STATUS_BY_APPOINTMENTID)
                    .setParameter(APPOINTMENT_ID, appointmentId);

            LastModifiedAppointmentIdAndStatus dtoList = transformQueryToSingleResult(
                    query1, LastModifiedAppointmentIdAndStatus.class);

            lastModifiedAppointmentIdAndStatuses.add(dtoList);
        });

        List<AppointmentTransferLogDTO> responses = transformQueryToResultList(
                query, AppointmentTransferLogDTO.class);

        if (responses.isEmpty()) {
            throw APPOINTMENT_TRANSFERE_NOT_FOUND.get();
        }

        appointmentTransferLogResponseDTO.setResponse(mergeCurrentAppointmentStatus(lastModifiedAppointmentIdAndStatuses, responses));

        appointmentTransferLogResponseDTO.setTotalItems(totalItems);

        return appointmentTransferLogResponseDTO;
    }

    @Override
    public AppointmentTransferPreviewResponseDTO fetchAppointmentTransferDetailById(Long appointmentTransferId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_APPOINTMENT_TRANSFER_DETAIL_BY_ID)
                .setParameter(APPOINTMENT_TRANSFER_ID, appointmentTransferId);

        AppointmentTransferPreviewResponseDTO response = transformQueryToSingleResult(
                query, AppointmentTransferPreviewResponseDTO.class);

        return response;
    }

    private Supplier<NoContentFoundException> DOCTOR_DUTY_ROSTER_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, DOCTOR_DUTY_ROSTER);
        throw new NoContentFoundException(DoctorDutyRoster.class);
    };

    private Supplier<NoContentFoundException> APPOINTMENT_TRANSFERE_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, APPOINTMENT_TRANSFER);
        throw new NoContentFoundException(AppointmentTransfer.class);
    };
}
