package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.HospitalDeptExistingDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.HospitalDeptDutyRosterMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.detail.*;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.existing.HospitalDeptExistingDutyRosterDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.existing.HospitalDeptExistingDutyRosterResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.HospitalDeptDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
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
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.NO_RECORD_FOUND;
import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.constants.HospitalDepartmentDutyRosterLog.HOSPITAL_DEPARTMENT_DUTY_ROSTER;
import static com.cogent.cogentappointment.client.query.HospitalDeptDutyRosterOverrideQuery.QUERY_TO_CHECK_IF_OVERRIDE_EXISTS;
import static com.cogent.cogentappointment.client.query.HospitalDeptDutyRosterOverrideQuery.QUERY_TO_FETCH_HDD_ROSTER_OVERRIDE_DETAILS;
import static com.cogent.cogentappointment.client.query.HospitalDeptDutyRosterQuery.*;
import static com.cogent.cogentappointment.client.query.HospitalDeptDutyRosterRoomQuery.QUERY_TO_FETCH_HDD_ROSTER_ROOM_DETAIL;
import static com.cogent.cogentappointment.client.query.HospitalDeptDutyRosterRoomQuery.QUERY_TO_FETCH_HDD_ROSTER_ROOM_NUMBER;
import static com.cogent.cogentappointment.client.query.HospitalDeptWeekDaysDutyRosterDoctorInfoQuery.QUERY_TO_FETCH_WEEK_DAYS_DOCTOR_INFO;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.splitByCharacterTypeCamelCase;
import static com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster.HospitalDeptDutyRosterUtils.parseHDDRosterDetails;
import static com.cogent.cogentappointment.client.utils.hospitalDeptDutyRoster.HospitalDeptDutyRosterUtils.parseToExistingRosterDetails;

/**
 * @author smriti on 20/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDeptDutyRosterRepositoryCustomImpl implements HospitalDeptDutyRosterRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Character fetchRoomStatusIfExists(Long hospitalDepartmentId, Date fromDate, Date toDate) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HDD_ROSTER_STATUS)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        try {
            return (Character) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Character fetchRoomStatusIfExistsExceptCurrentId(Long hospitalDepartmentId,
                                                            Date fromDate, Date toDate, Long hddRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HDD_ROSTER_STATUS_EXCEPT_CURRENT_ID)
                .setParameter(ID, hddRosterId)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));
        try {
            return (Character) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<HospitalDeptDutyRosterMinResponseDTO> search(HospitalDeptDutyRosterSearchRequestDTO searchRequestDTO,
                                                             Pageable pageable,
                                                             Long hospitalId) {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_SEARCH_HOSPITAL_DEPARTMENT_DUTY_ROSTER(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate())
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<HospitalDeptDutyRosterMinResponseDTO> results = transformQueryToResultList(
                query, HospitalDeptDutyRosterMinResponseDTO.class);

        if (results.isEmpty()) HOSPITAL_DEPT_DUTY_ROSTER_NOT_FOUND.get();

        results.get(0).setTotalItems(totalItems);
        return results;
    }

    @Override
    public HospitalDeptDutyRosterDetailResponseDTO fetchDetailsById(Long hddRosterId, Long hospitalId) {

        HospitalDeptDutyRosterResponseDTO hddRosterDetail = fetchHDDRosterDetail(hddRosterId, hospitalId);

        HospitalDeptDutyRosterRoomResponseDTO roomDetail =
                hddRosterDetail.getIsRoomEnabled().equals(YES) ? fetchHDDRosterRoomDetail(hddRosterId) : null;

        List<HospitalDeptWeekDaysDutyRosterResponseDTO> weekDaysDetail = fetchHDDRosterWeekDaysDetail(hddRosterId);

        List<HospitalDeptDutyRosterOverrideResponseDTO> overrideDetail =
                hddRosterDetail.getHasOverrideDutyRoster().equals(YES)
                        ? fetchHDDRosterOverrideDetail(hddRosterId) : new ArrayList<>();

        return parseHDDRosterDetails(hddRosterDetail, roomDetail, weekDaysDetail, overrideDetail);
    }

    @Override
    public List<HospitalDeptExistingDutyRosterResponseDTO> fetchExistingDutyRosters(
            HospitalDeptExistingDutyRosterRequestDTO requestDTO, Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_EXISTING_DUTY_ROSTER)
                .setParameter(ID, requestDTO.getHospitalDeptId())
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()))
                .setParameter(HOSPITAL_ID, hospitalId);

        List<HospitalDeptExistingDutyRosterResponseDTO> existingRosters =
                transformQueryToResultList(query, HospitalDeptExistingDutyRosterResponseDTO.class);

        existingRosters.forEach(existing -> {
            if (existing.getIsRoomEnabled().equals(YES))
                existing.setRoomNumber(fetchRoomNumber(existing.getHddRosterId()));
        });

        return existingRosters;
    }

    @Override
    public HospitalDeptExistingDutyRosterDetailResponseDTO fetchExistingRosterDetails(Long hddRosterId) {
        Character hasOverrideRosters = checkIfDutyRosterOverrideExists(hddRosterId);

        List<HospitalDeptWeekDaysDutyRosterResponseDTO> weekDaysDetail =
                fetchHDDRosterWeekDaysDetail(hddRosterId);

        List<HospitalDeptDutyRosterOverrideResponseDTO> overrideDetail =
                hasOverrideRosters.equals(YES)
                        ? fetchHDDRosterOverrideDetail(hddRosterId)
                        : new ArrayList<>();

        return parseToExistingRosterDetails(weekDaysDetail, overrideDetail);
    }

    private String fetchRoomNumber(Long hddRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HDD_ROSTER_ROOM_NUMBER)
                .setParameter(ID, hddRosterId);

        return (String) query.getSingleResult();
    }

    private Character checkIfDutyRosterOverrideExists(Long hddRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_CHECK_IF_OVERRIDE_EXISTS)
                .setParameter(ID, hddRosterId);

        return (Character) query.getSingleResult();
    }

    private HospitalDeptDutyRosterResponseDTO fetchHDDRosterDetail(Long hddRosterId, Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HDD_ROSTER_DETAIL)
                .setParameter(ID, hddRosterId)
                .setParameter(HOSPITAL_ID, hospitalId);
        try {
            return transformQueryToSingleResult(query, HospitalDeptDutyRosterResponseDTO.class);
        } catch (NoResultException e) {
            throw HOSPITAL_DEPT_DUTY_ROSTER_WITH_ID_NOT_FOUND.apply(hddRosterId);
        }
    }

    private HospitalDeptDutyRosterRoomResponseDTO fetchHDDRosterRoomDetail(Long hddRosterId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HDD_ROSTER_ROOM_DETAIL)
                .setParameter(ID, hddRosterId);

        return transformQueryToSingleResult(query, HospitalDeptDutyRosterRoomResponseDTO.class);
    }

    private List<HospitalDeptWeekDaysDutyRosterResponseDTO> fetchHDDRosterWeekDaysDetail(Long hddRosterId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HDD_WEEK_DAYS_DETAIL)
                .setParameter(ID, hddRosterId);

        List<HospitalDeptWeekDaysDutyRosterResponseDTO> weekDaysRosters =
                transformQueryToResultList(query, HospitalDeptWeekDaysDutyRosterResponseDTO.class);

        weekDaysRosters.forEach(weekDaysRoster -> {
            List<HospitalDeptWeekDaysDutyRosterDoctorInfoResponseDTO> weekDaysDoctorInfo
                    = fetchWeekDaysDoctorInfo(weekDaysRoster.getRosterWeekDaysId());

            weekDaysRoster.setWeekDaysDoctorInfo(weekDaysDoctorInfo);
        });

        return weekDaysRosters;
    }

    private List<HospitalDeptDutyRosterOverrideResponseDTO> fetchHDDRosterOverrideDetail(Long hddRosterId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HDD_ROSTER_OVERRIDE_DETAILS)
                .setParameter(ID, hddRosterId);

        return transformQueryToResultList(query, HospitalDeptDutyRosterOverrideResponseDTO.class);
    }

    private Supplier<NoContentFoundException> HOSPITAL_DEPT_DUTY_ROSTER_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL_DEPARTMENT_DUTY_ROSTER);
        throw new NoContentFoundException(String.format(NO_RECORD_FOUND,
                splitByCharacterTypeCamelCase(HospitalDepartmentDutyRoster.class.getSimpleName())));
    };

    private Function<Long, NoContentFoundException> HOSPITAL_DEPT_DUTY_ROSTER_WITH_ID_NOT_FOUND = (hddRosterId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_DEPARTMENT_DUTY_ROSTER);
        throw new NoContentFoundException(String.format(NO_RECORD_FOUND,
                splitByCharacterTypeCamelCase(HospitalDepartmentDutyRoster.class.getSimpleName())),
                "hddRosterId", hddRosterId.toString());
    };

    private List<HospitalDeptWeekDaysDutyRosterDoctorInfoResponseDTO> fetchWeekDaysDoctorInfo(
            Long hospitalDepartmentWeekDaysDutyRosterId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_WEEK_DAYS_DOCTOR_INFO)
                .setParameter(ID, hospitalDepartmentWeekDaysDutyRosterId);

        return transformQueryToResultList(query, HospitalDeptWeekDaysDutyRosterDoctorInfoResponseDTO.class);
    }
}
