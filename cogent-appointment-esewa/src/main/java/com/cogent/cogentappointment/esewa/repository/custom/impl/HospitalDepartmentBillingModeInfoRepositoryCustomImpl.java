package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.repository.custom.HospitalDepartmentBillingModeInfoRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalDepartmentBillingModeInfoRepositoryCustomImpl implements HospitalDepartmentBillingModeInfoRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

//    @Override
//    public ChargeResponseDTO fetchAppointmentCharge(ChargeRequestDTO requestDTO) {
//        Query query = createQuery.apply(entityManager, QUERY_TO_GET_CHARGE_BY_BILLING_MODE_AND_HOSPITAL_DEPARTMENT_ID)
//                .setParameter(BILLING_MODE_ID, requestDTO.getBillingModeId())
//                .setParameter(HOSPITAL_DEPARTMENT_ID, requestDTO.getHospitalDepartmentId());
//
//        try {
//            return transformQueryToSingleResult(query, ChargeResponseDTO.class);
//        } catch (NoResultException e) {
//            throw HOSPITAL_DEPARTMENT_BILLING_MODE_INFO_NOT_FOUND.get();
//        }
//    }
//
//    private Supplier<NoContentFoundException> HOSPITAL_DEPARTMENT_BILLING_MODE_INFO_NOT_FOUND = () -> {
//        log.error(CONTENT_NOT_FOUND, HOSPITAL_DEPARTMENT_BILLING_MODE_INFO);
//        throw new NoContentFoundException(HospitalDepartmentBillingModeInfo.class);
//    };
}
