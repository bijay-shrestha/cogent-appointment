package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.BillingModeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.BillingMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.BILLING_MODE_ID;
import static com.cogent.cogentappointment.client.constants.QueryConstants.HOSPITAL_ID;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.BillingModeLog.BILLING_MODE;
import static com.cogent.cogentappointment.client.query.BillingModeQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

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

    @Override
    public List<DropDownResponseDTO> fetchActiveMinBillingModeByHospitalId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_ACTIVE_BILLING_MODE_DROP_DOWN_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw BILLING_MODE_NOT_FOUND.get();
        else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinBillingModeByHospitalId(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_BILLING_MODE_DROP_DOWN_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw BILLING_MODE_NOT_FOUND.get();
        else return results;
    }

    private Supplier<NoContentFoundException> BILLING_MODE_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, BILLING_MODE);
        throw new NoContentFoundException(BillingMode.class);
    };
}
