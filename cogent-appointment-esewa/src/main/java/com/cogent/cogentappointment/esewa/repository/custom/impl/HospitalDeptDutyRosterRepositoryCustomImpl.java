package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterTimeResponseTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.HospitalDeptDutyRosterOverrideRepository;
import com.cogent.cogentappointment.esewa.repository.HospitalDeptWeekDaysDutyRosterRepository;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalDeptDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.HospitalDepartmentDutyRosterMessages.HOSPITAL_DEPARTMENT_NOT_AVAILABLE_DEBUG_MESSAGE;
import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.HospitalDepartmentDutyRosterMessages.HOSPITAL_DEPARTMENT_NOT_AVAILABLE_MESSAGE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.DATE;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HospitalDepartmentConstants.HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HospitalDepartmentConstants.HOSPITAL_DEPARTMENT_ROOM_INFO_ID;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalDepartmentDutyRosterLog.HOSPITAL_DEPARTMENT_DUTY_ROSTER;
import static com.cogent.cogentappointment.esewa.query.HospitalDeptDutyRosterQuery.*;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.utilDateToSqlDate;

/**
 * @author smriti on 29/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDeptDutyRosterRepositoryCustomImpl implements HospitalDeptDutyRosterRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final HospitalDeptDutyRosterOverrideRepository hospitalDeptDutyRosterOverrideRepository;

    private final HospitalDeptWeekDaysDutyRosterRepository hospitalDeptWeekDaysDutyRosterRepository;

    public HospitalDeptDutyRosterRepositoryCustomImpl(
            HospitalDeptDutyRosterOverrideRepository hospitalDeptDutyRosterOverrideRepository,
            HospitalDeptWeekDaysDutyRosterRepository hospitalDeptWeekDaysDutyRosterRepository) {
        this.hospitalDeptDutyRosterOverrideRepository = hospitalDeptDutyRosterOverrideRepository;
        this.hospitalDeptWeekDaysDutyRosterRepository = hospitalDeptWeekDaysDutyRosterRepository;
    }

    @Override
    public List<HospitalDepartmentDutyRoster> fetchHospitalDeptDutyRoster(Date date, Long hospitalDepartmentId) {

        List<HospitalDepartmentDutyRoster> hospitalDepartmentDutyRoster =
                entityManager.createQuery(QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_INFO, HospitalDepartmentDutyRoster.class)
                        .setParameter(DATE, utilDateToSqlDate(date))
                        .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                        .getResultList();

        if (ObjectUtils.isEmpty(hospitalDepartmentDutyRoster))
            throw HOSPITAL_DEPARTMENT_DUTY_ROSTER_NOT_FOUND.get();

        return hospitalDepartmentDutyRoster;
    }

    @Override
    public HospitalDepartmentDutyRoster fetchHospitalDeptDutyRosterWithoutRoom(Date date,
                                                                               Long hospitalDepartmentId) {

        try {
            return entityManager.createQuery(QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_WITHOUT_ROOM,
                    HospitalDepartmentDutyRoster.class)
                    .setParameter(DATE, utilDateToSqlDate(date))
                    .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw HOSPITAL_DEPARTMENT_DUTY_ROSTER_NOT_FOUND.get();
        }
    }

    @Override
    public HospitalDepartmentDutyRoster fetchHospitalDeptDutyRosterWithRoom(Date date,
                                                                            Long hospitalDepartmentId,
                                                                            Long hospitalDepartmentRoomInfoId) {
        try {
            return entityManager.createQuery(QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_WITH_ROOM,
                    HospitalDepartmentDutyRoster.class)
                    .setParameter(DATE, utilDateToSqlDate(date))
                    .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                    .setParameter(HOSPITAL_DEPARTMENT_ROOM_INFO_ID, hospitalDepartmentRoomInfoId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw HOSPITAL_DEPARTMENT_DUTY_ROSTER_NOT_FOUND.get();
        }
    }

    @Override
    public HospitalDeptDutyRosterTimeResponseTO fetchHospitalDeptDutyRoster(Date appointmentDate,
                                                                            Long hospitalDepartmentId,
                                                                            Long hospitalDepartmentRoomInfoId) {

        HospitalDepartmentDutyRoster hospitalDepartmentDutyRoster =
                Objects.isNull(hospitalDepartmentRoomInfoId)
                        ? fetchHospitalDepartmentDutyRosterWithoutRoom(appointmentDate, hospitalDepartmentId)
                        : fetchHospitalDepartmentDutyRosterWithRoom(
                        appointmentDate, hospitalDepartmentId, hospitalDepartmentRoomInfoId);

        HospitalDeptDutyRosterTimeResponseTO dutyRosterTimeResponseTO;

        if (hospitalDepartmentDutyRoster.getHasOverrideDutyRoster().equals(YES)) {
            HospitalDeptDutyRosterTimeResponseTO overrideRoster =
                    hospitalDeptDutyRosterOverrideRepository.fetchHospitalDeptDutyRosterOverrideTimeInfo(
                            hospitalDepartmentDutyRoster.getId(),
                            appointmentDate, hospitalDepartmentId,
                            hospitalDepartmentRoomInfoId);

            if (!Objects.isNull(overrideRoster))
                dutyRosterTimeResponseTO = overrideRoster;
            else
                dutyRosterTimeResponseTO = hospitalDeptWeekDaysDutyRosterRepository.fetchWeekDaysTimeInfo(
                        hospitalDepartmentDutyRoster.getId(), appointmentDate);
        } else {
            dutyRosterTimeResponseTO = hospitalDeptWeekDaysDutyRosterRepository.fetchWeekDaysTimeInfo(
                    hospitalDepartmentDutyRoster.getId(), appointmentDate);
        }

        if (dutyRosterTimeResponseTO.getDayOffStatus().equals(YES)) {
            Date date = utilDateToSqlDate(appointmentDate);
            log.error(String.format(HOSPITAL_DEPARTMENT_NOT_AVAILABLE_DEBUG_MESSAGE, date));
            throw new NoContentFoundException(
                    String.format(HOSPITAL_DEPARTMENT_NOT_AVAILABLE_MESSAGE, date),
                    String.format(HOSPITAL_DEPARTMENT_NOT_AVAILABLE_DEBUG_MESSAGE, date));
        }

        return dutyRosterTimeResponseTO;
    }

    @Override
    public List<HospitalDepartmentDutyRoster> fetchHospitalDeptDutyRoster(Long hospitalDepartmentId) {

        List<HospitalDepartmentDutyRoster> dutyRosters = entityManager.createQuery(
                QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_BY_HOSPITAL_DEPARTMENT_ID, HospitalDepartmentDutyRoster.class)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                .getResultList();

        if (ObjectUtils.isEmpty(dutyRosters))
            HOSPITAL_DEPARTMENT_DUTY_ROSTER_NOT_FOUND.get();

        return dutyRosters;
    }

    private HospitalDepartmentDutyRoster fetchHospitalDepartmentDutyRosterWithoutRoom(Date appointmentDate,
                                                                                      Long hospitalDepartmentId) {
        return fetchHospitalDeptDutyRosterWithoutRoom(appointmentDate, hospitalDepartmentId);
    }

    private HospitalDepartmentDutyRoster fetchHospitalDepartmentDutyRosterWithRoom(Date appointmentDate,
                                                                                   Long hospitalDepartmentId,
                                                                                   Long hospitalDepartmentRoomInfoId) {

        return fetchHospitalDeptDutyRosterWithRoom(appointmentDate, hospitalDepartmentId, hospitalDepartmentRoomInfoId);
    }

    private Supplier<NoContentFoundException> HOSPITAL_DEPARTMENT_DUTY_ROSTER_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL_DEPARTMENT_DUTY_ROSTER);
        throw new NoContentFoundException(HospitalDepartmentDutyRoster.class);
    };


}
