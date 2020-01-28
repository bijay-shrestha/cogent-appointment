package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.constants.QueryConstants;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.admin.model.Hospital;
import com.cogent.cogentappointment.admin.query.HospitalQuery;
import com.cogent.cogentappointment.admin.repository.custom.HospitalRepositoryCustom;
import com.cogent.cogentappointment.admin.utils.HospitalUtils;
import com.cogent.cogentappointment.admin.utils.commons.PageableUtils;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalDropdownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.utils.commons.QueryUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author smriti ON 12/01/2020
 */
@Repository
@Transactional(readOnly = true)
public class HospitalRepositoryCustomImpl implements HospitalRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> validateHospitalDuplicity(String name, String code) {
        Query query = QueryUtils.createQuery.apply(entityManager, HospitalQuery.QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(QueryConstants.NAME, name)
                .setParameter(QueryConstants.CODE, code);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateHospitalDuplicityForUpdate(Long id, String name, String code) {
        Query query = QueryUtils.createQuery.apply(entityManager, HospitalQuery.QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(QueryConstants.ID, id)
                .setParameter(QueryConstants.NAME, name)
                .setParameter(QueryConstants.CODE, code);

        return query.getResultList();
    }

    @Override
    public List<HospitalMinimalResponseDTO> search(HospitalSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Query query = QueryUtils.createNativeQuery.apply(entityManager, HospitalQuery.QUERY_TO_SEARCH_HOSPITAL(searchRequestDTO));

        int totalItems = query.getResultList().size();

        PageableUtils.addPagination.accept(pageable, query);

        List<HospitalMinimalResponseDTO> results = QueryUtils.transformNativeQueryToResultList(query, HospitalMinimalResponseDTO.class);

        if (results.isEmpty()) throw HOSPITAL_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public HospitalResponseDTO fetchDetailsById(Long id) {
        Query query = QueryUtils.createNativeQuery.apply(entityManager, HospitalQuery.QUERY_TO_FETCH_HOSPITAL_DETAILS)
                .setParameter(QueryConstants.ID, id);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) throw HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id);

        return HospitalUtils.parseToHospitalResponseDTO(results.get(0));
    }

    @Override
    public List<HospitalDropdownResponseDTO> fetchActiveHospitalForDropDown() {
        Query query = QueryUtils.createQuery.apply(entityManager, HospitalQuery.QUERY_TO_FETCH_HOSPITAL_FOR_DROPDOWN);

        List<HospitalDropdownResponseDTO> results = QueryUtils.transformQueryToResultList(query, HospitalDropdownResponseDTO.class);

        if (results.isEmpty()) throw HOSPITAL_NOT_FOUND.get();
        else return results;
    }

    private Supplier<NoContentFoundException> HOSPITAL_NOT_FOUND = () -> new NoContentFoundException(Hospital.class);

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Hospital.class, "id", id.toString());
    };
}



