package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentDoctorInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDoctorInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.client.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT_DOCTOR_INFO;
import static com.cogent.cogentappointment.client.query.HospitalDepartmentDoctorInfoQuery.QUERY_TO_FETCH_ASSIGNED_HOSPITAL_DEPARTMENT_DOCTOR;
import static com.cogent.cogentappointment.client.query.HospitalDepartmentDoctorInfoQuery.QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_DOCTOR_INFO;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDepartmentDoctorInfoRepositoryCustomImpl implements HospitalDepartmentDoctorInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DoctorDropdownDTO> fetchAssignedHospitalDepartmentDoctor(Long hospitalDepartmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ASSIGNED_HOSPITAL_DEPARTMENT_DOCTOR)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        List<DoctorDropdownDTO> doctors =
                transformQueryToResultList(query, DoctorDropdownDTO.class);

        if (ObjectUtils.isEmpty(doctors))
            throw HOSPITAL_DEPARTMENT_DOCTOR_NOT_FOUND.apply(hospitalDepartmentId);

        return doctors;
    }

    @Override
    public List<HospitalDepartmentDoctorInfo> fetchActiveHospitalDepartmentDoctorInfo(
            List<Long> hospitalDepartmentDoctorInfoIds) {

        String hospitalDepartmentDoctorInfoId = StringUtils.join(hospitalDepartmentDoctorInfoIds, COMMA_SEPARATED);

        try {
            return entityManager.createQuery(QUERY_TO_FETCH_HOSPITAL_DEPARTMENT_DOCTOR_INFO(hospitalDepartmentDoctorInfoId))
                    .getResultList();
        } catch (NoResultException e) {
            throw HOSPITAL_DEPARTMENT_DOCTOR_NOT_FOUND.apply(Long.parseLong(hospitalDepartmentDoctorInfoId));
        }
    }

    private Function<Long, NoContentFoundException> HOSPITAL_DEPARTMENT_DOCTOR_NOT_FOUND = (hospitalDepartmentId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_DEPARTMENT_DOCTOR_INFO);
        throw new NoContentFoundException(HospitalDepartmentDoctorInfo.class,
                "hospitalDepartmentId", hospitalDepartmentId.toString());
    };
}
