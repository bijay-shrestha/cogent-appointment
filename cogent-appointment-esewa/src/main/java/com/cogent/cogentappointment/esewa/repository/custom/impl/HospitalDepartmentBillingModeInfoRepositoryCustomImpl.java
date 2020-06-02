package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.esewa.dto.request.hospitalDepartment.ChargeRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartment.ChargeResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalDepartmentBillingModeInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentBillingModeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.constants.QueryConstants.BILLING_MODE_ID;
import static com.cogent.cogentappointment.esewa.constants.QueryConstants.HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT_BILLING_MODE_INFO;
import static com.cogent.cogentappointment.esewa.query.HospitalDepartmentBillingModeInfoQuery.QUERY_TO_GET_ACTIVE_BILLING_MODE_BY_HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.esewa.query.HospitalDepartmentBillingModeInfoQuery.QUERY_TO_GET_CHARGE_BY_BILLING_MODE_AND_HOSPITAL_DEPARTMENT_ID;
import static com.cogent.cogentappointment.esewa.utils.commons.QueryUtils.*;

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
    public List<DropDownResponseDTO> fetchBillingModeByDepartmentId(Long hospitalDepartmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_ACTIVE_BILLING_MODE_BY_HOSPITAL_DEPARTMENT_ID)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        List<DropDownResponseDTO> billingModes =
                transformQueryToResultList(query, DropDownResponseDTO.class);

        if (billingModes.isEmpty())
            throw HOSPITAL_DEPARTMENT_BILLING_MODE_INFO_NOT_FOUND.get();

        return billingModes;
    }

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
