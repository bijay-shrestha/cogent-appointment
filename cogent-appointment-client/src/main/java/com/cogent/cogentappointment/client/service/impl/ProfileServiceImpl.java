package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileMenuSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileRequestDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.profile.AssignedProfileResponseDTO;
import com.cogent.cogentappointment.client.dto.response.profile.ProfileDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.profile.ProfileMinimalResponseDTO;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.log.CommonLogConstant;
import com.cogent.cogentappointment.client.model.Department;
import com.cogent.cogentappointment.client.model.Profile;
import com.cogent.cogentappointment.client.model.ProfileMenu;
import com.cogent.cogentappointment.client.repository.DepartmentRepository;
import com.cogent.cogentappointment.client.repository.ProfileMenuRepository;
import com.cogent.cogentappointment.client.repository.ProfileRepository;
import com.cogent.cogentappointment.client.service.ProfileService;
import com.cogent.cogentappointment.client.utils.ProfileMenuUtils;
import com.cogent.cogentappointment.client.utils.ProfileUtils;
import com.cogent.cogentappointment.client.utils.commons.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.log.constants.ProfileLog.PROFILE;

/**
 * @author smriti on 7/2/19
 */
@Service
@Transactional
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    private final ProfileMenuRepository profileMenuRepository;

    private final DepartmentRepository departmentRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository,
                              ProfileMenuRepository profileMenuRepository,
                              DepartmentRepository departmentRepository) {
        this.profileRepository = profileRepository;
        this.profileMenuRepository = profileMenuRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void save(ProfileRequestDTO profileRequestDTO) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, PROFILE);

        Long profileCount = profileRepository.validateDuplicity(profileRequestDTO.getProfileDTO().getName(),
                profileRequestDTO.getProfileDTO().getHospitalId());

        validateName(profileCount, profileRequestDTO.getProfileDTO().getName());

        Department department = findDepartmentById(profileRequestDTO.getProfileDTO().getDepartmentId());

        Profile savedProfile = save(ProfileUtils.convertDTOToProfile(profileRequestDTO.getProfileDTO(), department));

        saveProfileMenu(ProfileMenuUtils.convertToProfileMenu(savedProfile, profileRequestDTO.getProfileMenuRequestDTO()));

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, PROFILE, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    public void saveProfileMenu(List<ProfileMenu> profileMenus) {
        profileMenuRepository.saveAll(profileMenus);
    }

    @Override
    public void update(ProfileUpdateRequestDTO requestDTO) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, PROFILE);

        Profile profile = findById(requestDTO.getProfileDTO().getId());

        Long profileCount = profileRepository.validateDuplicityForUpdate(
                requestDTO.getProfileDTO().getId(),
                requestDTO.getProfileDTO().getName(),
                requestDTO.getProfileDTO().getHospitalId());

        validateName(profileCount, requestDTO.getProfileDTO().getName());

        Department department = findDepartmentById(requestDTO.getProfileDTO().getDepartmentId());

        save(ProfileUtils.convertToUpdatedProfile(requestDTO.getProfileDTO(), department, profile));

        saveProfileMenu(ProfileMenuUtils.convertToUpdatedProfileMenu(profile, requestDTO.getProfileMenuRequestDTO()));

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, PROFILE, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    public Profile findById(Long profileId) {
        return profileRepository.findProfileById(profileId)
                .orElseThrow(() -> PROFILE_WITH_GIVEN_ID_NOT_FOUND.apply(profileId));
    }

    private void validateName(Long profileCount, String name) {
        if (profileCount.intValue() > 0)
            throw new DataDuplicationException(
                    String.format(ErrorMessageConstants.NAME_DUPLICATION_MESSAGE, Profile.class.getSimpleName(), name));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.DELETING_PROCESS_STARTED, PROFILE);

        Profile profile = findById(deleteRequestDTO.getId());

        save(ProfileUtils.convertProfileToDeleted.apply(profile, deleteRequestDTO));

        log.info(CommonLogConstant.DELETING_PROCESS_COMPLETED, PROFILE, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<ProfileMinimalResponseDTO> search(ProfileSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SEARCHING_PROCESS_STARTED, PROFILE);

        List<ProfileMinimalResponseDTO> responseDTOS = profileRepository.search(searchRequestDTO, pageable);

        log.info(CommonLogConstant.SEARCHING_PROCESS_COMPLETED, PROFILE, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public ProfileDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_STARTED, PROFILE);

        ProfileDetailResponseDTO detailResponseDTO = profileRepository.fetchDetailsById(id);

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_COMPLETED, PROFILE, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return detailResponseDTO;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveProfilesForDropdown() {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, PROFILE);

        List<DropDownResponseDTO> responseDTOS = profileRepository.fetchActiveProfilesForDropDown();

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, PROFILE, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Profile fetchActiveProfileById(Long id) {
        return profileRepository.findActiveProfileById(id)
                .orElseThrow(() -> PROFILE_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    @Override
    public List<DropDownResponseDTO> fetchProfileByDepartmentId(Long departmentId) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, PROFILE);

        List<DropDownResponseDTO> responseDTOS = profileRepository.fetchProfileByDepartmentId(departmentId);

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, PROFILE, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AssignedProfileResponseDTO fetchAssignedProfileResponseDto(ProfileMenuSearchRequestDTO searchRequestDTO) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED, PROFILE);

        AssignedProfileResponseDTO responseDTO =
                profileRepository.fetchAssignedProfileResponseDto(searchRequestDTO);

        log.info(CommonLogConstant.FETCHING_PROCESS_COMPLETED, PROFILE, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    private Function<Long, NoContentFoundException> PROFILE_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Profile.class, "id", id.toString());
    };

    public Department findDepartmentById(Long id) {
        return departmentRepository.findActiveDepartmentById(id)
                .orElseThrow(() -> new NoContentFoundException(Department.class, "id", id.toString()));
    }
}

