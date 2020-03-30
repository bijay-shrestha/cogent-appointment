package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.CountryRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.ERROR_LOG;
import static com.cogent.cogentappointment.admin.log.constants.CountryLog.COUNTRY;
import static com.cogent.cogentappointment.admin.query.CountryQuery.QUERY_TO_FETCH_ACTIVE_COUNTRY;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author smriti on 08/11/2019
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class CountryRepositoryCustomImpl implements CountryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveCountry() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_COUNTRY);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw new NoContentFoundException(Country.class);
        } else return results;
    }

    private void error() {
        log.error(ERROR_LOG, COUNTRY);
    }
}
