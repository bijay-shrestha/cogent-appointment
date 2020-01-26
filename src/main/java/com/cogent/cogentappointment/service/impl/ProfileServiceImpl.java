package com.cogent.cogentappointment.service.impl;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.profile.ProfileMenuSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.profile.ProfileRequestDTO;
import com.cogent.cogentappointment.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.profile.ProfileUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.profile.AssignedProfileResponseDTO;
import com.cogent.cogentappointment.dto.response.profile.ProfileDetailResponseDTO;
import com.cogent.cogentappointment.dto.response.profile.ProfileMinimalResponseDTO;
import com.cogent.cogentappointment.exception.DataDuplicationException;
import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.model.Department;
import com.cogent.cogentappointment.model.Profile;
import com.cogent.cogentappointment.model.ProfileMenu;
import com.cogent.cogentappointment.repository.DepartmentRepository;
import com.cogent.cogentappointment.repository.ProfileMenuRepository;
import com.cogent.cogentappointment.repository.ProfileRepository;
import com.cogent.cogentappointment.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.constants.ErrorMessageConstants.NAME_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.log.constants.ProfileLog.PROFILE;
import static com.cogent.cogentappointment.utils.ProfileMenuUtils.convertToProfileMenu;
import static com.cogent.cogentappointment.utils.ProfileMenuUtils.convertToUpdatedProfileMenu;
import static com.cogent.cogentappointment.utils.ProfileUtils.*;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, PROFILE);

        validateName(profileRepository.findProfileCountByName(profileRequestDTO.getProfileDTO().getName()),
                profileRequestDTO.getProfileDTO().getName());

        Department department = findDepartmentById(profileRequestDTO.getProfileDTO().getDepartmentId());

        Profile savedProfile = save(convertDTOToProfile(profileRequestDTO.getProfileDTO(), department));

        saveProfileMenu(convertToProfileMenu(savedProfile, profileRequestDTO.getProfileMenuRequestDTO()));

        log.info(SAVING_PROCESS_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));
    }

    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    public void saveProfileMenu(List<ProfileMenu> profileMenus) {
        profileMenuRepository.saveAll(profileMenus);
    }

    @Override
    public void update(ProfileUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, PROFILE);

        Profile profile = findById(requestDTO.getProfileDTO().getId());

        validateName(profileRepository.findProfileCountByIdAndName(requestDTO.getProfileDTO().getId(),
                requestDTO.getProfileDTO().getName()), requestDTO.getProfileDTO().getName());

        Department department = findDepartmentById(requestDTO.getProfileDTO().getDepartmentId());

        save(convertToUpdatedProfile(requestDTO.getProfileDTO(), department, profile));

        saveProfileMenu(convertToUpdatedProfileMenu(profile, requestDTO.getProfileMenuRequestDTO()));

        log.info(UPDATING_PROCESS_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));
    }

    public Profile findById(Long profileId) {
        return profileRepository.findProfileById(profileId)
                .orElseThrow(() -> PROFILE_WITH_GIVEN_ID_NOT_FOUND.apply(profileId));
    }

    private void validateName(Long profileCount, String name) {
        if (profileCount.intValue() > 0)
            throw new DataDuplicationException(
                    String.format(NAME_DUPLICATION_MESSAGE, Profile.class.getSimpleName(), name));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, PROFILE);

        Profile profile = findById(deleteRequestDTO.getId());

        save(convertProfileToDeleted.apply(profile, deleteRequestDTO));

        log.info(DELETING_PROCESS_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<ProfileMinimalResponseDTO> search(ProfileSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PROFILE);

        List<ProfileMinimalResponseDTO> responseDTOS = profileRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public ProfileDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, PROFILE);

        ProfileDetailResponseDTO detailResponseDTO = profileRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));

        return detailResponseDTO;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveProfilesForDropdown() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, PROFILE);

        List<DropDownResponseDTO> responseDTOS = profileRepository.fetchActiveProfilesForDropDown();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Profile fetchActiveProfileById(Long id) {
        return profileRepository.findActiveProfileById(id)
                .orElseThrow(() -> PROFILE_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    @Override
    public List<DropDownResponseDTO> fetchProfileByDepartmentId(Long departmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, PROFILE);

        List<DropDownResponseDTO> responseDTOS = profileRepository.fetchProfileByDepartmentId(departmentId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AssignedProfileResponseDTO fetchAssignedProfileResponseDto(ProfileMenuSearchRequestDTO searchRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PROFILE);

        AssignedProfileResponseDTO responseDTO =
                profileRepository.fetchAssignedProfileResponseDto(searchRequestDTO);

        log.info(FETCHING_PROCESS_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));

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

