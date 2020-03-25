package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.ProfileDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.ProfileMinimalResponseDTO;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.log.constants.ProfileLog;
import com.cogent.cogentappointment.admin.repository.CompanyProfileRepository;
import com.cogent.cogentappointment.admin.repository.ProfileMenuRepository;
import com.cogent.cogentappointment.admin.service.CompanyProfileService;
import com.cogent.cogentappointment.persistence.model.Profile;
import com.cogent.cogentappointment.persistence.model.ProfileMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.CompanyProfileLog.COMPANY_PROFILE;
import static com.cogent.cogentappointment.admin.utils.CompanyProfileUtils.convertDTOToProfile;
import static com.cogent.cogentappointment.admin.utils.ProfileMenuUtils.convertToProfileMenu;
import static com.cogent.cogentappointment.admin.utils.ProfileMenuUtils.convertToUpdatedProfileMenu;
import static com.cogent.cogentappointment.admin.utils.ProfileUtils.convertProfileToDeleted;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 7/2/19
 */
@Service
@Transactional
@Slf4j
public class CompanyProfileServiceImpl implements CompanyProfileService {

    private final CompanyProfileRepository companyProfileRepository;

    private final ProfileMenuRepository profileMenuRepository;

    public CompanyProfileServiceImpl(CompanyProfileRepository companyProfileRepository,
                                     ProfileMenuRepository profileMenuRepository) {
        this.companyProfileRepository = companyProfileRepository;
        this.profileMenuRepository = profileMenuRepository;
    }

    @Override
    public void save(CompanyProfileRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, COMPANY_PROFILE);

        Long profileCount = companyProfileRepository.validateDuplicity(
                requestDTO.getCompanyProfileDTO().getName(),
                requestDTO.getCompanyProfileDTO().getCompanyId()
        );

        validateName(profileCount, requestDTO.getCompanyProfileDTO().getName());

        Profile savedProfile = save(convertDTOToProfile(requestDTO.getCompanyProfileDTO()));

        saveProfileMenu(convertToProfileMenu(savedProfile, requestDTO.getProfileMenuRequestDTO()));

        log.info(SAVING_PROCESS_COMPLETED, COMPANY_PROFILE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(CompanyProfileUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, COMPANY_PROFILE);

        Profile profile = findById(requestDTO.getProfileDTO().getId());

        Long profileCount = companyProfileRepository.validateDuplicityForUpdate(
                requestDTO.getProfileDTO().getId(),
                requestDTO.getProfileDTO().getName(),
                requestDTO.getProfileDTO().getCompanyId());

        validateName(profileCount, requestDTO.getProfileDTO().getName());

//        save(convertToUpdatedProfile(requestDTO.getProfileDTO(), profile));

        saveProfileMenu(convertToUpdatedProfileMenu(profile, requestDTO.getProfileMenuRequestDTO()));

        log.info(UPDATING_PROCESS_COMPLETED, COMPANY_PROFILE, getDifferenceBetweenTwoTime(startTime));
    }

    private Profile findById(Long profileId) {
        return companyProfileRepository.findProfileById(profileId)
                .orElseThrow(() -> PROFILE_WITH_GIVEN_ID_NOT_FOUND.apply(profileId));
    }

    private void validateName(Long profileCount, String name) {
        if (profileCount.intValue() > 0)
            throw new DataDuplicationException(
                    String.format(ErrorMessageConstants.NAME_DUPLICATION_MESSAGE, Profile.class.getSimpleName(), name));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, ProfileLog.PROFILE);

        Profile profile = findById(deleteRequestDTO.getId());

        save(convertProfileToDeleted.apply(profile, deleteRequestDTO));

        log.info(DELETING_PROCESS_COMPLETED, ProfileLog.PROFILE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<ProfileMinimalResponseDTO> search(ProfileSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, ProfileLog.PROFILE);

//        List<ProfileMinimalResponseDTO> responseDTOS = profileRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, ProfileLog.PROFILE, getDifferenceBetweenTwoTime(startTime));

        return null;
    }

    @Override
    public ProfileDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, ProfileLog.PROFILE);

        ProfileDetailResponseDTO detailResponseDTO = companyProfileRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, ProfileLog.PROFILE, getDifferenceBetweenTwoTime(startTime));

        return detailResponseDTO;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveProfilesForDropdown() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, ProfileLog.PROFILE);

//        List<DropDownResponseDTO> responseDTOS = profileRepository.fetchActiveProfilesForDropDown();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, ProfileLog.PROFILE, getDifferenceBetweenTwoTime(startTime));

        return null;
    }

    @Override
    public Profile fetchActiveProfileById(Long id) {
//        return companyProfileRepository.findActiveProfileById(id)
//                .orElseThrow(() -> PROFILE_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        return null;
    }

    public Profile save(Profile profile) {
        return companyProfileRepository.save(profile);
    }

    private Function<Long, NoContentFoundException> PROFILE_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Profile.class, "id", id.toString());
    };

    private void saveProfileMenu(List<ProfileMenu> profileMenus) {
        profileMenuRepository.saveAll(profileMenus);
    }
}

