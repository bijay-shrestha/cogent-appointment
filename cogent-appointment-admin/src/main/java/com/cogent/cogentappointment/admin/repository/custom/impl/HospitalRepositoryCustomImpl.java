package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanySearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyDropdownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalDropdownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.HospitalRepositoryCustom;
import com.cogent.cogentappointment.admin.utils.commons.PageableUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.NO_RECORD_FOUND;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.HospitalLog.CLIENT;
import static com.cogent.cogentappointment.admin.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.admin.query.CompanyQuery.*;
import static com.cogent.cogentappointment.admin.query.HospitalBillingModeInfoQuery.QUERY_TO_GET_BILLING_MODE_DROP_DOWN_BY_HOSPITAL_ID;
import static com.cogent.cogentappointment.admin.query.HospitalQuery.*;
import static com.cogent.cogentappointment.admin.utils.CompanyUtils.parseToCompanyResponseDTO;
import static com.cogent.cogentappointment.admin.utils.HospitalUtils.parseToHospitalResponseDTO;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author smriti ON 12/01/2020
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalRepositoryCustomImpl implements HospitalRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> validateHospitalDuplicity(String name, String code, String alias) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(CODE, code)
                .setParameter(ALIAS, alias);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateCompanyDuplicity(String name, String code) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_COMPANY_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(CODE, code);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateHospitalDuplicityForUpdate(Long id, String name, String code, String alias) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, id)
                .setParameter(NAME, name)
                .setParameter(CODE, code)
                .setParameter(ALIAS, alias);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateCompanyDuplicityForUpdate(Long id, String name, String code) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_COMPANY_DUPLICITY_FOR_UPDATE)
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

        if (results.isEmpty())
            throw HOSPITAL_NOT_FOUND.get();

        results.get(0).setTotalItems(totalItems);
        return results;
    }

    @Override
    public List<CompanyMinimalResponseDTO> searchCompany(CompanySearchRequestDTO searchRequestDTO, Pageable pageable) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_SEARCH_COMPANY(searchRequestDTO));

        int totalItems = query.getResultList().size();

        PageableUtils.addPagination.accept(pageable, query);

        List<CompanyMinimalResponseDTO> results = transformNativeQueryToResultList(query, CompanyMinimalResponseDTO.class);

        if (results.isEmpty())
            throw HOSPITAL_NOT_FOUND.get();

        results.get(0).setTotalItems(totalItems);
        return results;
    }

    @Override
    public HospitalResponseDTO fetchDetailsById(Long id) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DETAILS)
                .setParameter(ID, id);

        Query billingModeQuery = createQuery.apply(entityManager, QUERY_TO_GET_BILLING_MODE_DROP_DOWN_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, id);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) {
            throw HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id);
        }

        HospitalResponseDTO responseDTO = parseToHospitalResponseDTO(results.get(0));

        responseDTO.setBillingMode(transformQueryToResultList(billingModeQuery, DropDownResponseDTO.class));

        return responseDTO;
    }

    @Override
    public CompanyResponseDTO fetchCompanyDetailsById(Long id) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_COMPANY_DETAILS)
                .setParameter(ID, id);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) throw HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id);
        return parseToCompanyResponseDTO(results.get(0));
    }

    @Override
    public Integer fetchHospitalFollowUpCount(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_COUNT)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Integer) query.getSingleResult();
    }

    @Override
    public Integer fetchHospitalFollowUpIntervalDays(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_INTERVAL_DAYS)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Integer) query.getSingleResult();
    }

    @Override
    public List<HospitalDropdownResponseDTO> fetchActiveHospitalForDropDown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_HOSPITAL_FOR_DROPDOWN);

        List<HospitalDropdownResponseDTO> results = transformQueryToResultList(query, HospitalDropdownResponseDTO.class);

        if (results.isEmpty())
            throw HOSPITAL_NOT_FOUND.get();

        else return results;
    }

    @Override
    public List<HospitalDropdownResponseDTO> fetchHospitalForDropDown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FOR_DROPDOWN);

        List<HospitalDropdownResponseDTO> results = transformQueryToResultList(query, HospitalDropdownResponseDTO.class);

        if (results.isEmpty())
            throw HOSPITAL_NOT_FOUND.get();

        else return results;
    }

    @Override
    public List<CompanyDropdownResponseDTO> fetchActiveCompanyForDropDown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_COMPANY_FOR_DROPDOWN);

        List<CompanyDropdownResponseDTO> results = transformQueryToResultList(query, CompanyDropdownResponseDTO.class);

        if (results.isEmpty())
            throw HOSPITAL_NOT_FOUND.get();

        else return results;
    }

    private Supplier<NoContentFoundException> HOSPITAL_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL);
        throw new NoContentFoundException(NO_RECORD_FOUND, CLIENT);
    };

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL, id);
        throw new NoContentFoundException(String.format(NO_RECORD_FOUND, CLIENT), "id", id.toString());
    };
}



