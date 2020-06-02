package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterRoomInfoResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalDeptDutyRosterRoomInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterRoomInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.AppointmentHospitalDepartmentMessage.INVALID_ROOM_CHECK_AVAILABILITY_REQUEST;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HOSPITAL_DEPARTMENT_DUTY_ROSTER_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.ROOM_ID;
import static com.cogent.cogentappointment.esewa.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.query.HospitalDeptDutyRosterRoomQuery.QUERY_TO_FETCH_HDD_ROSTER_ROOM_DETAIL;
import static com.cogent.cogentappointment.esewa.query.HospitalDeptDutyRosterRoomQuery.QUERY_TO_FETCH_HDD_ROSTER_ROOM_NUMBER;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 31/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDeptDutyRosterRoomInfoRepositoryCustomImpl implements HospitalDeptDutyRosterRoomInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<HospitalDeptDutyRosterRoomInfoResponseDTO> fetchHospitalDeptRoomInfo(List<Long> hddRosterId) {

        String hddRosterIds = StringUtils.join(hddRosterId, COMMA_SEPARATED);

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HDD_ROSTER_ROOM_DETAIL(hddRosterIds));

        List<HospitalDeptDutyRosterRoomInfoResponseDTO> results =
                transformQueryToResultList(query, HospitalDeptDutyRosterRoomInfoResponseDTO.class);

        if (results.isEmpty())
            throw NO_HOSPITAL_DEPT_ROOM_INFO_FOUND.get();

        return results;
    }

    @Override
    public String fetchRoomNumber(Long hddRosterId, Long roomId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HDD_ROSTER_ROOM_NUMBER)
                .setParameter(HOSPITAL_DEPARTMENT_DUTY_ROSTER_ID, hddRosterId)
                .setParameter(ROOM_ID, roomId);

        try {
            return (String) query.getSingleResult();
        } catch (NoResultException e) {
            throw INVALID_ROOM_REQUEST.get();
        }
    }

    private Supplier<NoContentFoundException> NO_HOSPITAL_DEPT_ROOM_INFO_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HospitalDepartmentDutyRosterRoomInfo.class);
        throw new NoContentFoundException(HospitalDepartmentDutyRosterRoomInfo.class);
    };

    private Supplier<NoContentFoundException> INVALID_ROOM_REQUEST = () -> {
        log.error(INVALID_ROOM_CHECK_AVAILABILITY_REQUEST);
        throw new NoContentFoundException(INVALID_ROOM_CHECK_AVAILABILITY_REQUEST);
    };
}
