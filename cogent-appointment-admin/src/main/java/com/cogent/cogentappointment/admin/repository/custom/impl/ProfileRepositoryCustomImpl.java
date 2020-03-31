package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileMenuSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.*;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.ProfileRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Profile;
import com.cogent.cogentappointment.persistence.model.ProfileMenu;
import lombok.extern.slf4j.Slf4j;
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
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.ProfileLog.PROFILE;
import static com.cogent.cogentappointment.admin.log.constants.ProfileLog.PROFILE_MENU;
import static com.cogent.cogentappointment.admin.query.ProfileQuery.*;
import static com.cogent.cogentappointment.admin.utils.ProfileUtils.parseToAssignedProfileMenuResponseDTO;
import static com.cogent.cogentappointment.admin.utils.ProfileUtils.parseToProfileDetailResponseDTO;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author smriti on 7/10/19
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class ProfileRepositoryCustomImpl implements ProfileRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateDuplicity(String name, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long validateDuplicityForUpdate(Long profileId, String name, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, profileId)
                .setParameter(NAME, name)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<ProfileMinimalResponseDTO> search(ProfileSearchRequestDTO searchRequestDTO,
                                                  Pageable pageable) {

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_PROFILE.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<ProfileMinimalResponseDTO> results = transformQueryToResultList(query, ProfileMinimalResponseDTO.class);

        if (results.isEmpty()) {
            error(PROFILE);
            throw PROFILES_NOT_FOUND.get();
        } else {
            results.get(0).setTotalItems(totalItems);
            return results;
        }
    }

    @Override
    public ProfileDetailResponseDTO fetchDetailsById(Long id) {
        return parseToProfileDetailResponseDTO(getProfileResponseDTO(id), getProfileMenuResponseDTO(id));
    }

    private ProfileResponseDTO getProfileResponseDTO(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PROFILE_DETAILS)
                .setParameter(ID, id);

        try {
            return transformQueryToSingleResult(query, ProfileResponseDTO.class);
        } catch (NoResultException e) {
            throw PROFILE_WITH_GIVEN_ID_NOT_FOUND.apply(id);
        }
    }

    private List<ProfileMenuResponseDTO> getProfileMenuResponseDTO(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PROFILE_MENU_DETAILS)
                .setParameter(ID, id);

        return transformQueryToResultList(query, ProfileMenuResponseDTO.class);
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveProfilesForDropDown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_PROFILES_FOR_DROPDOWN);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error(PROFILE);
            throw PROFILES_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchProfileByDepartmentId(Long departmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_PROFILE_BY_DEPARTMENT_ID)
                .setParameter(ID, departmentId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error(PROFILE);
            throw PROFILES_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public AssignedProfileResponseDTO fetchAssignedProfileResponseDto(ProfileMenuSearchRequestDTO searchRequestDTO) {

        Query query = entityManager.createNativeQuery(QUERY_TO_FETCH_ASSIGNED_PROFILE_RESPONSE)
                .setParameter(USERNAME, searchRequestDTO.getUsername());

        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) {
            error(PROFILE_MENU);
            throw new NoContentFoundException(ProfileMenu.class);
        }

        return parseToAssignedProfileMenuResponseDTO(results);
    }

    private Supplier<NoContentFoundException> PROFILES_NOT_FOUND = () -> new NoContentFoundException(Profile.class);

    private Function<Long, NoContentFoundException> PROFILE_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, PROFILE, id);
        throw new NoContentFoundException(Profile.class, "id", id.toString());
    };

    private void error(String name) {
        log.error(CONTENT_NOT_FOUND, name);
    }
}
