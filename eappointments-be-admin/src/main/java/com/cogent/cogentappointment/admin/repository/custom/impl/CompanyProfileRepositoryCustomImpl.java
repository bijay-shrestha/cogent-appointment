package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.companyProfile.CompanyProfileDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyProfile.CompanyProfileMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyProfile.CompanyProfileResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.ProfileMenuResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.CompanyProfileRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.query.CompanyProfileQuery.*;
import static com.cogent.cogentappointment.admin.utils.CompanyProfileUtils.parseToCompanyProfileDetailResponseDTO;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author smriti on 7/10/19
 */
@Service
@Transactional(readOnly = true)
public class CompanyProfileRepositoryCustomImpl implements CompanyProfileRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateDuplicity(String name, Long companyId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(COMPANY_ID, companyId);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long validateDuplicityForUpdate(Long profileId, String name, Long companyId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, profileId)
                .setParameter(NAME, name)
                .setParameter(COMPANY_ID, companyId);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<CompanyProfileMinimalResponseDTO> search(CompanyProfileSearchRequestDTO searchRequestDTO,
                                                         Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_COMPANY_PROFILE.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<CompanyProfileMinimalResponseDTO> results =
                transformQueryToResultList(query, CompanyProfileMinimalResponseDTO.class);

        if (results.isEmpty()) throw COMPANY_PROFILES_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public CompanyProfileDetailResponseDTO fetchDetailsById(Long id) {
        return parseToCompanyProfileDetailResponseDTO(getCompanyProfileDetail(id), getProfileMenuDetail(id));
    }

    private CompanyProfileResponseDTO getCompanyProfileDetail(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_COMPANY_PROFILE_DETAILS)
                .setParameter(ID, id);

        try {
            return transformQueryToSingleResult(query, CompanyProfileResponseDTO.class);
        } catch (NoResultException e) {
            throw COMPANY_PROFILE_WITH_GIVEN_ID_NOT_FOUND.apply(id);
        }
    }

    private List<ProfileMenuResponseDTO> getProfileMenuDetail(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_COMPANY_PROFILE_MENU_DETAILS)
                .setParameter(ID, id);

        return transformQueryToResultList(query, ProfileMenuResponseDTO.class);
    }

    @Override
    public List<DropDownResponseDTO> fetchMinActiveCompanyProfile() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_COMPANY_PROFILES_FOR_DROPDOWN);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw COMPANY_PROFILES_NOT_FOUND.get();
        else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinCompanyProfile() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_COMPANY_PROFILES_FOR_DROPDOWN);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw COMPANY_PROFILES_NOT_FOUND.get();
        else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinActiveCompanyProfileByCompanyId(Long companyId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_COMPANY_PROFILES_BY_COMPANY_ID)
                .setParameter(COMPANY_ID, companyId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw COMPANY_PROFILES_NOT_FOUND.get();
        else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinCompanyProfileByCompanyId(Long companyId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_COMPANY_PROFILES_BY_COMPANY_ID)
                .setParameter(COMPANY_ID, companyId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw COMPANY_PROFILES_NOT_FOUND.get();
        else return results;
    }


    private Supplier<NoContentFoundException> COMPANY_PROFILES_NOT_FOUND =
            () -> new NoContentFoundException(Profile.class);

    private Function<Long, NoContentFoundException> COMPANY_PROFILE_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Profile.class, "id", id.toString());
    };
}
