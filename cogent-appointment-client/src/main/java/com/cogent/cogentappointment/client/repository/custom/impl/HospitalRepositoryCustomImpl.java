package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalDropdownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.model.Hospital;
import com.cogent.cogentappointment.client.repository.custom.HospitalRepositoryCustom;
import com.cogent.cogentappointment.client.utils.HospitalUtils;
import com.cogent.cogentappointment.client.utils.commons.PageableUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.query.HospitalQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

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
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(CODE, code);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateHospitalDuplicityForUpdate(Long id, String name, String code) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, id)
                .setParameter(NAME, name)
                .setParameter(CODE, code);

        return query.getResultList();
    }

    @Override
    public List<HospitalMinimalResponseDTO> search(HospitalSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_SEARCH_HOSPITAL(searchRequestDTO));

        int totalItems = query.getResultList().size();

        PageableUtils.addPagination.accept(pageable, query);

        List<HospitalMinimalResponseDTO> results = transformNativeQueryToResultList(query, HospitalMinimalResponseDTO.class);

        if (results.isEmpty()) throw HOSPITAL_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public HospitalResponseDTO fetchDetailsById(Long id) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DETAILS)
                .setParameter(ID, id);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) throw HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id);

        return HospitalUtils.parseToHospitalResponseDTO(results.get(0));
    }

    @Override
    public List<HospitalDropdownResponseDTO> fetchActiveHospitalForDropDown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FOR_DROPDOWN);

        List<HospitalDropdownResponseDTO> results = transformQueryToResultList(query, HospitalDropdownResponseDTO.class);

        if (results.isEmpty()) throw HOSPITAL_NOT_FOUND.get();
        else return results;
    }

    private Supplier<NoContentFoundException> HOSPITAL_NOT_FOUND = () -> new NoContentFoundException(Hospital.class);

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Hospital.class, "id", id.toString());
    };
}



