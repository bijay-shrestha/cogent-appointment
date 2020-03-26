package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.companyProfile.CompanyProfileDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyProfile.CompanyProfileMinimalResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.CompanyProfileRepository;
import com.cogent.cogentappointment.admin.repository.ProfileMenuRepository;
import com.cogent.cogentappointment.admin.service.CompanyProfileService;
import com.cogent.cogentappointment.admin.service.CompanyService;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Profile;
import com.cogent.cogentappointment.persistence.model.ProfileMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.NAME_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.ProfileServiceMessages.INVALID_DELETE_REQUEST;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.CompanyProfileLog.COMPANY_PROFILE;
import static com.cogent.cogentappointment.admin.utils.CompanyProfileUtils.*;
import static com.cogent.cogentappointment.admin.utils.ProfileMenuUtils.convertToProfileMenu;
import static com.cogent.cogentappointment.admin.utils.ProfileMenuUtils.convertToUpdatedProfileMenu;
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

    private final CompanyService companyService;

    public CompanyProfileServiceImpl(CompanyProfileRepository companyProfileRepository,
                                     ProfileMenuRepository profileMenuRepository,
                                     CompanyService companyService) {
        this.companyProfileRepository = companyProfileRepository;
        this.profileMenuRepository = profileMenuRepository;
        this.companyService = companyService;
    }

    @Override
    public void save(CompanyProfileRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, COMPANY_PROFILE);

        Long companyProfileCount = companyProfileRepository.validateDuplicity(
                requestDTO.getCompanyProfileInfo().getName(),
                requestDTO.getCompanyProfileInfo().getCompanyId()
        );

        validateName(companyProfileCount, requestDTO.getCompanyProfileInfo().getName());

        Hospital company = findCompanyById(requestDTO.getCompanyProfileInfo().getCompanyId());

        Profile savedCompanyProfile = save(
                convertDTOToCompanyProfile(requestDTO.getCompanyProfileInfo(), company));

        saveProfileMenu(convertToProfileMenu(savedCompanyProfile, requestDTO.getProfileMenuInfo()));

        log.info(SAVING_PROCESS_COMPLETED, COMPANY_PROFILE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(CompanyProfileUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, COMPANY_PROFILE);

        Profile companyProfile = findById(requestDTO.getCompanyProfileInfo().getId());

        Long companyProfileCount = companyProfileRepository.validateDuplicityForUpdate(
                requestDTO.getCompanyProfileInfo().getId(),
                requestDTO.getCompanyProfileInfo().getName(),
                requestDTO.getCompanyProfileInfo().getCompanyId());

        validateName(companyProfileCount, requestDTO.getCompanyProfileInfo().getName());

        Hospital company = findCompanyById(requestDTO.getCompanyProfileInfo().getCompanyId());

        convertToUpdatedCompanyProfile(requestDTO.getCompanyProfileInfo(), companyProfile, company);

        saveProfileMenu(convertToUpdatedProfileMenu(companyProfile, requestDTO.getProfileMenuInfo()));

        log.info(UPDATING_PROCESS_COMPLETED, COMPANY_PROFILE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, COMPANY_PROFILE);

        Profile companyProfile = findById(deleteRequestDTO.getId());

        if (companyProfile.getIsSuperAdminProfile().equals(YES))
            throw new BadRequestException(INVALID_DELETE_REQUEST);

        convertCompanyProfileToDeleted(companyProfile, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, COMPANY_PROFILE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<CompanyProfileMinimalResponseDTO> search(CompanyProfileSearchRequestDTO searchRequestDTO,
                                                         Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, COMPANY_PROFILE);

        List<CompanyProfileMinimalResponseDTO> minCompanyProfileInfo =
                companyProfileRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, COMPANY_PROFILE, getDifferenceBetweenTwoTime(startTime));

        return minCompanyProfileInfo;
    }

    @Override
    public CompanyProfileDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, COMPANY_PROFILE);

        CompanyProfileDetailResponseDTO companyProfileDetails =
                companyProfileRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, COMPANY_PROFILE, getDifferenceBetweenTwoTime(startTime));

        return companyProfileDetails;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinActiveCompanyProfile() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, COMPANY_PROFILE);

        List<DropDownResponseDTO> minInfo = companyProfileRepository.fetchMinActiveCompanyProfile();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, COMPANY_PROFILE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    private void validateName(Long profileCount, String name) {
        if (profileCount.intValue() > 0)
            throw new DataDuplicationException(
                    String.format(NAME_DUPLICATION_MESSAGE, Profile.class.getSimpleName(), name));
    }

    public Profile save(Profile profile) {
        return companyProfileRepository.save(profile);
    }

    private Profile findById(Long companyProfileId) {
        return companyProfileRepository.findCompanyProfileById(companyProfileId)
                .orElseThrow(() -> COMPANY_PROFILE_WITH_GIVEN_ID_NOT_FOUND.apply(companyProfileId));
    }

    private Hospital findCompanyById(Long companyId) {
        return companyService.findActiveCompanyById(companyId);
    }

    private Function<Long, NoContentFoundException> COMPANY_PROFILE_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Profile.class, "id", id.toString());
    };

    private void saveProfileMenu(List<ProfileMenu> profileMenus) {
        profileMenuRepository.saveAll(profileMenus);
    }
}

