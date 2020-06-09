package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartment.HospitalDepartmentBillingModeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartment.HospitalDepartmentResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalDepartmentRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HospitalDepartmentConstants.HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.esewa.query.HospitalDepartmentBillingModeInfoQuery.QUERY_TO_FETCH_MIN_BILLING_MODE;
import static com.cogent.cogentappointment.esewa.query.HospitalDepartmentQuery.QUERY_TO_FETCH_ACTIVE_HOSPITAL_DEPARTMENT_FOR_DROPDOWN;
import static com.cogent.cogentappointment.esewa.query.HospitalDepartmentQuery.QUERY_TO_FETCH_ACTIVE_MIN_HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 28/05/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDepartmentRepositoryCustomImpl implements HospitalDepartmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveHospitalDepartment(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_HOSPITAL_DEPARTMENT_FOR_DROPDOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> minInfo = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (minInfo.isEmpty())
            HOSPITAL_DEPARTMENT_NOT_FOUND.get();

        return minInfo;
    }

    @Override
    public  List<HospitalDepartmentResponseDTO> fetchHospitalDepartmentInfo(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_MIN_HOSPITAL_DEPARTMENT)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<HospitalDepartmentResponseDTO> availableHospitalDepartments =
                transformQueryToResultList(query, HospitalDepartmentResponseDTO.class);

        if (availableHospitalDepartments.isEmpty())
            HOSPITAL_DEPARTMENT_NOT_FOUND.get();

        availableHospitalDepartments.forEach(departmentInfo -> {

            List<HospitalDepartmentBillingModeResponseDTO> billingModeInfo =
                    fetchBillingModeInfo(departmentInfo.getHospitalDepartmentId());

            departmentInfo.setBillingModeInfo(billingModeInfo);
        });

        return availableHospitalDepartments;
    }

    private Supplier<NoContentFoundException> HOSPITAL_DEPARTMENT_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL_DEPARTMENT);
        throw new NoContentFoundException(HospitalDepartment.class);
    };

    private List<HospitalDepartmentBillingModeResponseDTO> fetchBillingModeInfo(Long hospitalDepartmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_MIN_BILLING_MODE)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        return transformQueryToResultList(query, HospitalDepartmentBillingModeResponseDTO.class);
    }
}
