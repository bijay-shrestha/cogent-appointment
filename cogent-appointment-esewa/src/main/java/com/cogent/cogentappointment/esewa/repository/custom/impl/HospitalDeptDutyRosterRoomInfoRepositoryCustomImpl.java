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

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.ID;
import static com.cogent.cogentappointment.esewa.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.query.HospitalDeptDutyRosterRoomQuery.QUERY_TO_FETCH_HDD_ROSTER_ROOM_DETAIL;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformQueryToResultList;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformQueryToSingleResult;

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
    public HospitalDeptDutyRosterRoomInfoResponseDTO fetchHospitalDeptRoomInfo(Long hddRosterId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HDD_ROSTER_ROOM_DETAIL)
                .setParameter(ID, hddRosterId);

        try {
            return transformQueryToSingleResult(query, HospitalDeptDutyRosterRoomInfoResponseDTO.class);
        } catch (NoResultException e) {
            throw NO_HOSPITAL_DEPT_ROOM_INFO_FOUND.get();
        }

    }

    private Supplier<NoContentFoundException> NO_HOSPITAL_DEPT_ROOM_INFO_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HospitalDepartmentDutyRosterRoomInfo.class);
        throw new NoContentFoundException(HospitalDepartmentDutyRosterRoomInfo.class);
    };
}
