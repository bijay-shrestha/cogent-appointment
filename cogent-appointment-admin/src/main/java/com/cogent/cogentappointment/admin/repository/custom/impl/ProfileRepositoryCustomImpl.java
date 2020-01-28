package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.constants.QueryConstants;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileMenuSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.*;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.model.Profile;
import com.cogent.cogentappointment.admin.model.ProfileMenu;
import com.cogent.cogentappointment.admin.query.ProfileQuery;
import com.cogent.cogentappointment.admin.repository.custom.ProfileRepositoryCustom;
import com.cogent.cogentappointment.admin.utils.ProfileUtils;
import com.cogent.cogentappointment.admin.utils.commons.PageableUtils;
import com.cogent.cogentappointment.admin.utils.commons.QueryUtils;
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

/**
 * @author smriti on 7/10/19
 */
@Service
@Transactional(readOnly = true)
public class ProfileRepositoryCustomImpl implements ProfileRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateDuplicity(String name, Long hospitalId) {
        Query query = QueryUtils.createQuery.apply(entityManager, ProfileQuery.QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(QueryConstants.NAME, name)
                .setParameter(QueryConstants.HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long validateDuplicityForUpdate(Long profileId, String name, Long hospitalId) {
        Query query = QueryUtils.createQuery.apply(entityManager, ProfileQuery.QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(QueryConstants.ID, profileId)
                .setParameter(QueryConstants.NAME, name)
                .setParameter(QueryConstants.HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<ProfileMinimalResponseDTO> search(ProfileSearchRequestDTO searchRequestDTO,
                                                  Pageable pageable) {

        Query query = QueryUtils.createQuery.apply(entityManager, ProfileQuery.QUERY_TO_SEARCH_PROFILE.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        PageableUtils.addPagination.accept(pageable, query);

        List<ProfileMinimalResponseDTO> results = QueryUtils.transformQueryToResultList(query, ProfileMinimalResponseDTO.class);

        if (results.isEmpty()) throw PROFILES_NOT_FOUND.get();
        else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public ProfileDetailResponseDTO fetchDetailsById(Long id) {
        return ProfileUtils.parseToProfileDetailResponseDTO(getProfileResponseDTO(id), getProfileMenuResponseDTO(id));
    }

    private ProfileResponseDTO getProfileResponseDTO(Long id) {
        Query query = QueryUtils.createQuery.apply(entityManager, ProfileQuery.QUERY_TO_FETCH_PROFILE_DETAILS)
                .setParameter(QueryConstants.ID, id);

        try {
            return QueryUtils.transformQueryToSingleResult(query, ProfileResponseDTO.class);
        } catch (NoResultException e) {
            throw PROFILE_WITH_GIVEN_ID_NOT_FOUND.apply(id);
        }
    }

    private List<ProfileMenuResponseDTO> getProfileMenuResponseDTO(Long id) {
        Query query = QueryUtils.createQuery.apply(entityManager, ProfileQuery.QUERY_TO_FETCH_PROFILE_MENU_DETAILS)
                .setParameter(QueryConstants.ID, id);

        return QueryUtils.transformQueryToResultList(query, ProfileMenuResponseDTO.class);
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveProfilesForDropDown() {
        Query query = QueryUtils.createQuery.apply(entityManager, ProfileQuery.QUERY_TO_FETCH_ACTIVE_PROFILES_FOR_DROPDOWN);

        List<DropDownResponseDTO> results = QueryUtils.transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw PROFILES_NOT_FOUND.get();
        else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchProfileByDepartmentId(Long departmentId) {
        Query query = QueryUtils.createQuery.apply(entityManager, ProfileQuery.QUERY_TO_FETCH_PROFILE_BY_DEPARTMENT_ID)
                .setParameter(QueryConstants.ID, departmentId);

        List<DropDownResponseDTO> results = QueryUtils.transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) throw PROFILES_NOT_FOUND.get();
        else return results;
    }

    @Override
    public AssignedProfileResponseDTO fetchAssignedProfileResponseDto(ProfileMenuSearchRequestDTO searchRequestDTO) {

        Query query = entityManager.createNativeQuery(ProfileQuery.QUERY_TO_FETCH_ASSIGNED_PROFILE_RESPONSE)
                .setParameter(QueryConstants.USERNAME, searchRequestDTO.getUsername())
                .setParameter(QueryConstants.EMAIL, searchRequestDTO.getUsername())
                .setParameter(QueryConstants.CODE, searchRequestDTO.getDepartmentCode());

        List<Object[]> results = query.getResultList();

        if (results.isEmpty())
            throw new NoContentFoundException(ProfileMenu.class);

        return ProfileUtils.parseToAssignedProfileMenuResponseDTO(results);
    }

    private Supplier<NoContentFoundException> PROFILES_NOT_FOUND = () -> new NoContentFoundException(Profile.class);

    private Function<Long, NoContentFoundException> PROFILE_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Profile.class, "id", id.toString());
    };
}
