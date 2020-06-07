package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.ChargeRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.ChargeResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.HospitalDepartmentBillingModeInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentBillingModeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.BILLING_MODE_ID;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT_BILLING_MODE_INFO;
import static com.cogent.cogentappointment.admin.query.HospitalDepartmentBillingModeInfoQuery.QUERY_TO_GET_CHARGE_BY_BILLING_MODE_AND_HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToSingleResult;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDepartmentBillingModeInfoRepositoryCustomImpl implements HospitalDepartmentBillingModeInfoRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public ChargeResponseDTO fetchAppointmentCharge(ChargeRequestDTO requestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_CHARGE_BY_BILLING_MODE_AND_HOSPITAL_DEPARTMENT_ID)
                .setParameter(BILLING_MODE_ID, requestDTO.getBillingModeId())
                .setParameter(HOSPITAL_DEPARTMENT_ID, requestDTO.getHospitalDepartmentId());

        try {
            return transformQueryToSingleResult(query, ChargeResponseDTO.class);
        } catch (NoResultException e) {
            throw HOSPITAL_DEPARTMENT_BILLING_MODE_INFO_NOT_FOUND.get();
        }
    }

    private Supplier<NoContentFoundException> HOSPITAL_DEPARTMENT_BILLING_MODE_INFO_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL_DEPARTMENT_BILLING_MODE_INFO);
        throw new NoContentFoundException(HospitalDepartmentBillingModeInfo.class);
    };
}
