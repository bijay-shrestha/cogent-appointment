package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.BillingModeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.BillingMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Objects;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.BILLING_MODE_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.BillingModeLog.BILLING_MODE;
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

        BillingMode billingMode=(BillingMode) query.getSingleResult();

        if(Objects.isNull(billingMode))
            throw BILLING_MODE_NOT_FOUND.get();

        return billingMode;
    }

    private Supplier<NoContentFoundException> BILLING_MODE_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, BILLING_MODE);
        throw new NoContentFoundException(BillingMode.class);
    };
}
