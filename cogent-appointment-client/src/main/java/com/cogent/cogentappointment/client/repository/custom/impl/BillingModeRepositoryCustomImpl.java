package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.repository.custom.BillingModeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.BillingMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static com.cogent.cogentappointment.client.constants.QueryConstants.BILLING_MODE_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.client.query.BillingModeQuery.QUERY_TO_GET_ACTIVE_BILLING_MODE_BY_HOSPITAL_ID;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;

/**
 * @author Sauravi Thapa ON 5/29/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class BillingModeRepositoryCustomImpl implements BillingModeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BillingMode fetchBillingModeByHospitalId(Long hospitalId, Long billingModeId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_ACTIVE_BILLING_MODE_BY_HOSPITAL_ID)
                .setParameter(BILLING_MODE_ID, billingModeId)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (BillingMode) query.getSingleResult();
    }
}
