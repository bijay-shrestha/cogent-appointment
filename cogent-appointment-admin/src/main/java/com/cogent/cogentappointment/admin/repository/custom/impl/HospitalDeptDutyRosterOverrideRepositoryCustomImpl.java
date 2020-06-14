package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.HospitalDeptDutyRosterOverrideRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterOverride;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.query.HospitalDeptDutyRosterOverrideQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.hospitalDeptDutyRoster.HospitalDeptOverrideDutyRosterUtils.parseQueryResultToHospitalDeptDutyRosterStatusResponseDTO;

/**
 * @author smriti on 20/05/20
 */
@Repository
@Transactional
@Slf4j
public class HospitalDeptDutyRosterOverrideRepositoryCustomImpl implements
        HospitalDeptDutyRosterOverrideRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long fetchOverrideCountWithoutRoom(Long hospitalDepartmentId, Date fromDate, Date toDate) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITHOUT_ROOM)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }

    @Override
    public Long fetchOverrideCountWithRoom(Long hospitalDepartmentId, Date fromDate,
                                           Date toDate, Long hospitalDepartmentRoomInfoId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITH_ROOM)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                .setParameter(HOSPITAL_DEPARTMENT_ROOM_INFO_ID, hospitalDepartmentRoomInfoId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate));

        return (Long) query.getSingleResult();
    }

    @Override
    public Long fetchOverrideCountWithoutRoomExceptCurrentId(Long hospitalDepartmentId, Date fromDate,
                                                             Date toDate, Long rosterOverrideId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITHOUT_ROOM_EXCEPT_CURRENT_ID)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate))
                .setParameter(ID, rosterOverrideId);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long fetchOverrideCountWithRoomExceptCurrentId(Long hospitalDepartmentId, Date fromDate, Date toDate,
                                                          Long hospitalDepartmentRoomInfoId, Long rosterOverrideId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DDR_OVERRIDE_COUNT_WITH_ROOM_EXCEPT_CURRENT_ID)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                .setParameter(HOSPITAL_DEPARTMENT_ROOM_INFO_ID, hospitalDepartmentRoomInfoId)
                .setParameter(FROM_DATE, utilDateToSqlDate(fromDate))
                .setParameter(TO_DATE, utilDateToSqlDate(toDate))
                .setParameter(ID, rosterOverrideId);

        return (Long) query.getSingleResult();
    }

    @Override
    public void updateOverrideStatus(Long hddRosterId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_UPDATE_OVERRIDE_STATUS)
                .setParameter(ID, hddRosterId);
        query.executeUpdate();
    }

    @Override
    public void updateOverrideRoomInfo(Long hddRosterId, Long hospitalDepartmentRoomInfoId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_UPDATE_OVERRIDE_ROOM(hospitalDepartmentRoomInfoId))
                .setParameter(ID, hddRosterId);

        query.executeUpdate();
    }

    @Override
    public List<HospitalDepartmentDutyRosterOverride> fetchOverrideList(List<HospitalDeptDutyRosterOverrideUpdateRequestDTO>
                                                                                updateRequestDTOS) {
        List<HospitalDepartmentDutyRosterOverride> overrides =
                entityManager.createQuery(QUERY_TO_FETCH_HDD_ROSTER_OVERRIDE(updateRequestDTOS),
                        HospitalDepartmentDutyRosterOverride.class)
                        .getResultList();

        if (overrides.isEmpty())
            throw new NoContentFoundException(HospitalDepartmentDutyRosterOverride.class);

        return overrides;
    }

    @Override
    public List<HospitalDeptDutyRosterStatusResponseDTO> fetchHospitalDeptDutyRosterOverrideStatus(
            HospitalDeptAppointmentStatusRequestDTO requestDTO, List<Long> rosterIdList) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DEPT_DUTY_ROSTER_OVERRIDE_STATUS(requestDTO,
                rosterIdList))
                .setParameter(FROM_DATE, utilDateToSqlDate(requestDTO.getFromDate()))
                .setParameter(TO_DATE, utilDateToSqlDate(requestDTO.getToDate()));

        if (rosterIdList.size() > 0)
            query.setParameter(HOSPITAL_DEPARTMENT_DUTY_ROSTER_ID, rosterIdList);

        if (!Objects.isNull(requestDTO.getHospitalId()))
            query.setParameter(HOSPITAL_ID, requestDTO.getHospitalId());

        if (!Objects.isNull(requestDTO.getHospitalDepartmentId()))
            query.setParameter(HOSPITAL_DEPARTMENT_ID, requestDTO.getHospitalDepartmentId());

        if (!Objects.isNull(requestDTO.getHospitalDepartmentRoomInfoId()))
            query.setParameter(HOSPITAL_DEPARTMENT_ROOM_INFO_ID, requestDTO.getHospitalDepartmentRoomInfoId());

        List<Object[]> results = query.getResultList();

        return parseQueryResultToHospitalDeptDutyRosterStatusResponseDTO(
                results, requestDTO.getFromDate(), requestDTO.getToDate());
    }
}
