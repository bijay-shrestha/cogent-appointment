package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.HospitalDepartmentRoomInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT_ROOM_INFO;
import static com.cogent.cogentappointment.admin.query.HospitalDepartmentRoomInfoQuery.QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_ROOM_INFO;
import static com.cogent.cogentappointment.admin.query.HospitalDepartmentRoomInfoQuery.QUERY_TO_VALIDATE_DUPLICITY;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;


/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDepartmentRoomInfoRepositoryCustomImpl implements HospitalDepartmentRoomInfoRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Object[]> validateRoomDuplicity(Long roomId, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(ROOM_ID, roomId)
                .setParameter(HOSPITAL_ID, hospitalId);

        return query.getResultList();
    }

    @Override
    public HospitalDepartmentRoomInfo fetchHospitalDepartmentRoomInfo(Long hospitalDepartmentRoomInfoId,
                                                                      Long hospitalDepartmentId) {
        try {
            return entityManager.createQuery(QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_ROOM_INFO, HospitalDepartmentRoomInfo.class)
                    .setParameter(HOSPITAL_DEPARTMENT_ROOM_INFO_ID, hospitalDepartmentRoomInfoId)
                    .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId)
                    .getSingleResult();

        } catch (NoResultException e) {
            throw HOSPITAL_DEPARTMENT_ROOM_INFO_NOT_FOUND.apply(hospitalDepartmentRoomInfoId);
        }
    }

    private Function<Long, NoContentFoundException> HOSPITAL_DEPARTMENT_ROOM_INFO_NOT_FOUND =
            (hospitalDepartmentRoomInfoId) -> {
                log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_DEPARTMENT_ROOM_INFO);
                throw new NoContentFoundException(HospitalDepartmentRoomInfo.class,
                        "hospitalDepartmentRoomInfoId", hospitalDepartmentRoomInfoId.toString());
            };
}
