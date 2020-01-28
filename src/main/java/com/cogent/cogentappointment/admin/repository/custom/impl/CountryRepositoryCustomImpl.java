package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.repository.custom.CountryRepositoryCustom;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.utils.commons.QueryUtils;
import com.cogent.cogentappointment.admin.model.Country;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.admin.query.CountryQuery.QUERY_TO_FETCH_ACTIVE_COUNTRY;

/**
 * @author smriti on 08/11/2019
 */
@Repository
@Transactional(readOnly = true)
public class CountryRepositoryCustomImpl implements CountryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DropDownResponseDTO> fetchActiveCountry() {
        Query query = QueryUtils.createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_COUNTRY);

        List<DropDownResponseDTO> results = QueryUtils.transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw new NoContentFoundException(Country.class);
        else return results;
    }
}
