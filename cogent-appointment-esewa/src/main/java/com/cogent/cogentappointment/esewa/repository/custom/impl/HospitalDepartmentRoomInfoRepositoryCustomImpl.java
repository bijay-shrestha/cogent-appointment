package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalDepartmentRoomInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HOSPITAL_DEPARTMENT_ROOM_INFO_ID;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT_ROOM_INFO;
import static com.cogent.cogentappointment.esewa.query.HospitalDepartmentRoomInfoQuery.QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_ROOM_INFO;

/**
 * @author smriti on 04/06/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDepartmentRoomInfoRepositoryCustomImpl implements HospitalDepartmentRoomInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

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
