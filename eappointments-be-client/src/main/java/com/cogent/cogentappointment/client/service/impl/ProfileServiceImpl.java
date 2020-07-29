package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileMenuSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileRequestDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.profile.AssignedProfileResponseDTO;
import com.cogent.cogentappointment.client.dto.response.profile.ProfileDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.profile.ProfileMinimalResponseDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.DepartmentRepository;
import com.cogent.cogentappointment.client.repository.ProfileMenuRepository;
import com.cogent.cogentappointment.client.repository.ProfileRepository;
import com.cogent.cogentappointment.client.service.ProfileService;
import com.cogent.cogentappointment.persistence.model.Department;
import com.cogent.cogentappointment.persistence.model.Profile;
import com.cogent.cogentappointment.persistence.model.ProfileMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.NAME_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.ProfileServiceMessages.INVALID_DELETE_REQUEST;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.DepartmentLog.DEPARTMENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.constants.ProfileLog.PROFILE;
import static com.cogent.cogentappointment.client.utils.ProfileMenuUtils.convertToProfileMenu;
import static com.cogent.cogentappointment.client.utils.ProfileMenuUtils.convertToUpdatedProfileMenu;
import static com.cogent.cogentappointment.client.utils.ProfileUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

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

        Long hospitalId = getLoggedInHospitalId();

        Long profileCount = profileRepository.validateDuplicity(
                profileRequestDTO.getProfileDTO().getName(), hospitalId
        );

        validateName(profileCount, profileRequestDTO.getProfileDTO().getName());

        Department department = findDepartmentByIdAndHospitalId(
                profileRequestDTO.getProfileDTO().getDepartmentId(), hospitalId
        );

        Profile savedProfile = save(convertDTOToProfile(profileRequestDTO.getProfileDTO(), department));

        saveProfileMenu(convertToProfileMenu(savedProfile, profileRequestDTO.getProfileMenuRequestDTO()));

        log.info(SAVING_PROCESS_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(ProfileUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, PROFILE);

        Long hospitalId = getLoggedInHospitalId();

        Profile profile = findByIdAndHospitalId(requestDTO.getProfileDTO().getId(), hospitalId);

        Long profileCount = profileRepository.validateDuplicityForUpdate(
                requestDTO.getProfileDTO().getId(),
                requestDTO.getProfileDTO().getName(),
                hospitalId);

        validateName(profileCount, requestDTO.getProfileDTO().getName());

        Department department = findDepartmentByIdAndHospitalId(
                requestDTO.getProfileDTO().getDepartmentId(),
                hospitalId
        );

        convertToUpdatedProfile(requestDTO.getProfileDTO(), department, profile);

        saveProfileMenu(convertToUpdatedProfileMenu(profile, requestDTO.getProfileMenuRequestDTO()));

        log.info(UPDATING_PROCESS_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, PROFILE);

        Profile profile = findByIdAndHospitalId(deleteRequestDTO.getId(), getLoggedInHospitalId());

        if (profile.getIsSuperAdminProfile().equals(YES))
            throw new BadRequestException(INVALID_DELETE_REQUEST);

        convertProfileToDeleted.apply(profile, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<ProfileMinimalResponseDTO> search(ProfileSearchRequestDTO searchRequestDTO,
                                                  Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PROFILE);

        Long hospitalId = getLoggedInHospitalId();

        List<ProfileMinimalResponseDTO> responseDTOS =
                profileRepository.search(searchRequestDTO, hospitalId, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public ProfileDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, PROFILE);

        ProfileDetailResponseDTO detailResponseDTO = profileRepository.fetchDetailsById(id, getLoggedInHospitalId());

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));

        return detailResponseDTO;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinProfile() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_ACTIVE_DROPDOWN, PROFILE);

        List<DropDownResponseDTO> responseDTOS = profileRepository.fetchActiveMinProfile(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_ACTIVE_DROPDOWN_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinProfile() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, PROFILE);

        List<DropDownResponseDTO> responseDTOS = profileRepository.fetchMinProfile(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Profile findActiveProfileByIdAndHospitalId(Long id, Long hospitalId) {
        return profileRepository.findActiveProfileByIdAndHospitalId(id, hospitalId)
                .orElseThrow(() -> PROFILE_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveProfileByDepartmentId(Long departmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, PROFILE);

        List<DropDownResponseDTO> responseDTOS =
                profileRepository.fetchActiveProfileByDepartmentAndHospitalId(departmentId, getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AssignedProfileResponseDTO fetchAssignedProfile(ProfileMenuSearchRequestDTO searchRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PROFILE);

        AssignedProfileResponseDTO responseDTO =
                profileRepository.fetchAssignedProfile(searchRequestDTO);

        log.info(FETCHING_PROCESS_COMPLETED, PROFILE, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    private void validateName(Long profileCount, String name) {
        if (profileCount.intValue() > 0) {
            log.error(NAME_DUPLICATION_ERROR, PROFILE, name);
            throw new DataDuplicationException(
                    String.format(NAME_DUPLICATION_MESSAGE, Profile.class.getSimpleName(), name));
        }
    }

    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    private void saveProfileMenu(List<ProfileMenu> profileMenus) {
        profileMenuRepository.saveAll(profileMenus);
    }

    private Profile findByIdAndHospitalId(Long profileId, Long hospitalId) {
        return profileRepository.findProfileByIdAndHospitalId(profileId, hospitalId)
                .orElseThrow(() -> PROFILE_WITH_GIVEN_ID_NOT_FOUND.apply(profileId));
    }

    private Function<Long, NoContentFoundException> PROFILE_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, PROFILE, id);
        throw new NoContentFoundException(Profile.class, "id", id.toString());
    };

    private Department findDepartmentByIdAndHospitalId(Long id, Long hospitalId) {
        return departmentRepository.findActiveDepartmentByIdAndHospitalId(id, hospitalId)
                .orElseThrow(() -> DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(id, hospitalId));
    }

    private BiFunction<Long, Long, NoContentFoundException> DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND = (id, hospitalId) -> {
        log.error(DEPARTMENT_NOT_FOUND_BY_ID, id, hospitalId);
        throw new NoContentFoundException(Department.class, "id", id.toString());
    };
}

