package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster.DoctorDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster.DoctorExistingDutyRosterRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.RosterDetailsForStatus;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.*;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.DoctorDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.commons.configuration.MinIOProperties;
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
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.DoctorDutyRosterLog.DOCTOR_DUTY_ROSTER;
import static com.cogent.cogentappointment.admin.query.DoctorDutyRosterOverrideQuery.QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_DETAILS;
import static com.cogent.cogentappointment.admin.query.DoctorDutyRosterQuery.*;
import static com.cogent.cogentappointment.admin.utils.DoctorDutyRosterUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;
import static com.cogent.cogentappointment.commons.utils.MinIOUtils.fileUrlCheckPoint;

/**
 * @author smriti on 26/11/2019
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class DoctorDutyRosterRepositoryCustomImpl implements DoctorDutyRosterRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final MinIOProperties minIOProperties;

    public DoctorDutyRosterRepositoryCustomImpl(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
    }

    @Override
    public Long validateDoctorDutyRosterCount(Long doctorId,
                                              Long specializationId,
                                              Date fromDate,
                                              Date toDate) {

        Query query = createQuery.apply(entityManager, VALIDATE_DOCTOR_DUTY_ROSTER_COUNT)
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }

    @Override
    public List<DoctorDutyRosterMinimalResponseDTO> search(DoctorDutyRosterSearchRequestDTO searchRequestDTO,
                                                           Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_DOCTOR_DUTY_ROSTER(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate())
                .setParameter(CDN_URL, minIOProperties.getCDN_URL());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<DoctorDutyRosterMinimalResponseDTO> results = transformQueryToResultList(
                query, DoctorDutyRosterMinimalResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();
        } else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public DoctorDutyRosterDetailResponseDTO fetchDetailsById(Long doctorDutyRosterId) {

        DoctorDutyRosterResponseDTO doctorDutyRosterResponseDTO =
                fetchDoctorDutyRosterDetails(doctorDutyRosterId);

        if (doctorDutyRosterResponseDTO.getFileUri() != null) {
            doctorDutyRosterResponseDTO.setFileUri(fileUrlCheckPoint(doctorDutyRosterResponseDTO.getFileUri()));
        }

        List<DoctorWeekDaysDutyRosterResponseDTO> weekDaysDutyRosterResponseDTO =
                fetchDoctorWeekDaysDutyRosterResponseDTO(doctorDutyRosterId);

        List<DoctorDutyRosterOverrideResponseDTO> overrideResponseDTOS =
                doctorDutyRosterResponseDTO.getHasOverrideDutyRoster().equals(YES)
                        ? fetchDoctorDutyRosterOverrideResponseDTO(doctorDutyRosterId) : new ArrayList<>();

        return parseToDoctorDutyRosterDetailResponseDTO(
                doctorDutyRosterResponseDTO, weekDaysDutyRosterResponseDTO, overrideResponseDTOS);
    }

    @Override
    public List<DoctorExistingDutyRosterResponseDTO> fetchExistingDoctorDutyRosters(
            DoctorExistingDutyRosterRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_EXISTING_DOCTOR_DUTY_ROSTERS)
                .setParameter(DOCTOR_ID, requestDTO.getDoctorId())
                .setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId())
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()));

        return transformQueryToResultList(query, DoctorExistingDutyRosterResponseDTO.class);
    }

    @Override
    public DoctorExistingDutyRosterDetailResponseDTO fetchExistingRosterDetails(Long doctorDutyRosterId) {
        Character hasOverrideRosters = checkIfDoctorDutyRosterOverrideExists(doctorDutyRosterId);

        List<DoctorWeekDaysDutyRosterResponseDTO> weekDaysDutyRosterResponseDTO =
                fetchDoctorWeekDaysDutyRosterResponseDTO(doctorDutyRosterId);

        List<DoctorDutyRosterOverrideResponseDTO> overrideResponseDTOS =
                hasOverrideRosters.equals(YES)
                        ? fetchDoctorDutyRosterOverrideResponseDTO(doctorDutyRosterId)
                        : new ArrayList<>();

        return parseToExistingRosterDetails(weekDaysDutyRosterResponseDTO, overrideResponseDTOS);
    }

    @Override
    public List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus(
            AppointmentStatusRequestDTO requestDTO) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_STATUS(requestDTO))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()));

        if (!Objects.isNull(requestDTO.getDoctorId()))
            query.setParameter(DOCTOR_ID, requestDTO.getDoctorId());

        if (!Objects.isNull(requestDTO.getSpecializationId()))
            query.setParameter(SPECIALIZATION_ID, requestDTO.getSpecializationId());

        if (!Objects.isNull(requestDTO.getHospitalId()))
            query.setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        List<Object[]> results = query.getResultList();

        return parseQueryResultToDoctorDutyRosterStatusResponseDTOS(
                results, requestDTO.getFromDate(), requestDTO.getToDate());
    }

    @Override
    public RosterDetailsForStatus fetchRosterDetailsToSearchByApptNumber(Long doctorId, Long specializationId, Date date) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ROSTER_DETAILS)
                .setParameter(DATE, utilDateToSqlDate(date))
                .setParameter(DOCTOR_ID, doctorId)
                .setParameter(SPECIALIZATION_ID, specializationId);

        try {
            RosterDetailsForStatus response = transformQueryToSingleResult(query, RosterDetailsForStatus.class);
            return response;
        } catch (NoResultException ex) {
            error();
            throw DOCTOR_DUTY_ROSTER_NOT_FOUND.get();
        }


    }

    private DoctorDutyRosterResponseDTO fetchDoctorDutyRosterDetails(Long doctorDutyRosterId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_DETAILS)
                .setParameter(ID, doctorDutyRosterId);
        try {
            return transformQueryToSingleResult(query, DoctorDutyRosterResponseDTO.class);
        } catch (NoResultException e) {
            throw DOCTOR_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND.apply(doctorDutyRosterId);
        }
    }

    private List<DoctorWeekDaysDutyRosterResponseDTO> fetchDoctorWeekDaysDutyRosterResponseDTO(
            Long doctorDutyRosterId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_WEEK_DAYS_DUTY_ROSTER)
                .setParameter(ID, doctorDutyRosterId);

        return transformQueryToResultList(query, DoctorWeekDaysDutyRosterResponseDTO.class);
    }

    private List<DoctorDutyRosterOverrideResponseDTO> fetchDoctorDutyRosterOverrideResponseDTO(
            Long doctorDutyRosterId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DOCTOR_DUTY_ROSTER_OVERRIDE_DETAILS)
                .setParameter(ID, doctorDutyRosterId);

        return transformQueryToResultList(query, DoctorDutyRosterOverrideResponseDTO.class);
    }

    private Supplier<NoContentFoundException> DOCTOR_DUTY_ROSTER_NOT_FOUND = () ->
            new NoContentFoundException(DoctorDutyRoster.class);

    private Function<Long, NoContentFoundException> DOCTOR_DUTY_ROSTER_WITH_GIVEN_ID_NOT_FOUND =
            (doctorDutyRosterId) -> {
                log.error(CONTENT_NOT_FOUND_BY_ID, DOCTOR_DUTY_ROSTER, doctorDutyRosterId);
                throw new NoContentFoundException
                        (DoctorDutyRoster.class, "doctorDutyRosterId", doctorDutyRosterId.toString());
            };

    private Character checkIfDoctorDutyRosterOverrideExists(Long doctorDutyRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_CHECK_IF_OVERRIDE_EXISTS)
                .setParameter(ID, doctorDutyRosterId);

        return (Character) query.getSingleResult();
    }

    private void error() {
        log.error(CONTENT_NOT_FOUND, DOCTOR_DUTY_ROSTER);
    }
}
