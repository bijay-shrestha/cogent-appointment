package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.billingMode.BillingModeMinimalResponse;
import com.cogent.cogentappointment.admin.dto.response.billingMode.BillingModeMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.billingMode.BillingModeResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.BillingModeRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.BillingMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.BillingModeLog.BILLING_MODE;
import static com.cogent.cogentappointment.admin.query.BillingModeQuery.*;
import static com.cogent.cogentappointment.admin.query.HospitalBillingModeInfoQuery.QUERY_TO_GET_ACTIVE_BILLING_MODE_DROP_DOWN_BY_HOSPITAL_ID;
import static com.cogent.cogentappointment.admin.query.HospitalBillingModeInfoQuery.QUERY_TO_GET_BILLING_MODE_DROP_DOWN_BY_HOSPITAL_ID;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

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
    public List<Object[]> validateDuplicity(BillingModeRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getCode());

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(BillingModeUpdateRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, requestDTO.getId())
                .setParameter(NAME, requestDTO.getName())
                .setParameter(CODE, requestDTO.getCode());

        return query.getResultList();
    }

    @Override
    public BillingModeMinimalResponseDTO search(BillingModeSearchRequestDTO searchRequestDTO, Pageable pageable) {

        BillingModeMinimalResponseDTO minimalResponseDTO = new BillingModeMinimalResponseDTO();

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_BILLING_MODE.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<BillingModeMinimalResponse> results = transformQueryToResultList(
                query, BillingModeMinimalResponse.class);

        if (results.isEmpty()) throw BILLING_MODE_NOT_FOUND.get();
        else {

            minimalResponseDTO.setBillingModeList(results);

            minimalResponseDTO.setTotalItems(totalItems);

            return minimalResponseDTO;
        }
    }

    @Override
    public BillingModeResponseDTO fetchDetailsById(Long id) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BILLING_MODE_DETAILS)
                .setParameter(ID, id);

        try {
            return transformQueryToSingleResult(query, BillingModeResponseDTO.class);
        } catch (NoResultException e) {
            throw BILLING_MODE_NOT_FOUND.get();
        }
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinBillingMode() {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_BILLING_MODE_FOR_DROP_DOWN);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw BILLING_MODE_NOT_FOUND.get();
        else return results;

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
    public List<DropDownResponseDTO> fetchMinBillingMode() {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_BILLING_MODE_FOR_DROP_DOWN);

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

    @Override
    public BillingMode fetchBillingModeByHospitalId(Long hospitalId, Long billingModeId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_GET_ACTIVE_BILLING_MODE_BY_HOSPITAL_ID)
                .setParameter(BILLING_MODE_ID, billingModeId)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (BillingMode) query.getSingleResult();
    }

    private Supplier<NoContentFoundException> BILLING_MODE_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, BILLING_MODE);
        throw new NoContentFoundException(BillingMode.class);
    };
}
