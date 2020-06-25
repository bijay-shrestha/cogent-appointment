package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.*;
import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.AdminDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.AdminDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.AdminMetaInfoResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.AdminMinimalResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.*;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.AdminServiceMessages.*;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.admin.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.AdminLog.*;
import static com.cogent.cogentappointment.admin.utils.AdminUtils.*;
import static com.cogent.cogentappointment.admin.utils.DashboardFeatureUtils.parseToAdminDashboardFeature;
import static com.cogent.cogentappointment.admin.utils.DashboardFeatureUtils.parseToUpdateAdminDashboardFeature;
import static com.cogent.cogentappointment.admin.utils.GenderUtils.fetchGenderByCode;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static java.lang.reflect.Array.get;

/**
 * @author smriti on 2019-08-05
 */
@Service
@Transactional
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final Validator validator;

    private final AdminRepository adminRepository;

    private final AdminMacAddressInfoRepository adminMacAddressInfoRepository;

    private final AdminMetaInfoRepository adminMetaInfoRepository;

    private final AdminAvatarRepository adminAvatarRepository;

    private final DashboardFeatureRepository dashboardFeatureRepository;

    private final AdminDashboardFeatureRepository adminDashboardFeatureRepository;

    private final AdminConfirmationTokenRepository confirmationTokenRepository;

    private final MinioFileService minioFileService;

    private final EmailService emailService;

    private final ProfileService profileService;

    private final AdminFeatureService adminFeatureService;

    public AdminServiceImpl(Validator validator,
                            AdminRepository adminRepository,
                            AdminMacAddressInfoRepository adminMacAddressInfoRepository,
                            AdminMetaInfoRepository adminMetaInfoRepository,
                            AdminAvatarRepository adminAvatarRepository,
                            DashboardFeatureRepository dashboardFeatureRepository,
                            AdminDashboardFeatureRepository adminDashboardFeatureRepository,
                            AdminConfirmationTokenRepository confirmationTokenRepository,
                            MinioFileService minioFileService, EmailService emailService,
                            ProfileService profileService,
                            AdminFeatureService adminFeatureService) {
        this.validator = validator;
        this.adminRepository = adminRepository;
        this.adminMacAddressInfoRepository = adminMacAddressInfoRepository;
        this.adminMetaInfoRepository = adminMetaInfoRepository;
        this.adminAvatarRepository = adminAvatarRepository;
        this.dashboardFeatureRepository = dashboardFeatureRepository;
        this.adminDashboardFeatureRepository = adminDashboardFeatureRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.minioFileService = minioFileService;
        this.emailService = emailService;
        this.profileService = profileService;
        this.adminFeatureService = adminFeatureService;
    }

    @Override
    public void save(AdminRequestDTO adminRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, ADMIN);

        validateAdminCount(adminRequestDTO.getHospitalId());

        List<Object[]> admins = adminRepository.validateDuplicity(
                adminRequestDTO.getEmail(),
                adminRequestDTO.getMobileNumber()
        );

        validateAdminDuplicity(admins,
                adminRequestDTO.getEmail(),
                adminRequestDTO.getMobileNumber(),
                adminRequestDTO.getHospitalId()
        );

        Admin admin = saveAdmin(adminRequestDTO);

        saveAdminAvatar(admin, adminRequestDTO.getAvatar());

        saveMacAddressInfo(admin, adminRequestDTO.getMacAddressInfo());

        saveAdminMetaInfo(admin);

        saveAdminFeature(admin);

        saveAllAdminDashboardFeature(adminRequestDTO.getAdminDashboardRequestDTOS(), admin);

        AdminConfirmationToken adminConfirmationToken = saveAdminConfirmationToken(admin);

        EmailRequestDTO emailRequestDTO = convertAdminRequestToEmailRequestDTO(adminRequestDTO,
                adminConfirmationToken.getConfirmationToken());

        saveEmailToSend(emailRequestDTO);

        log.info(SAVING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<AdminDropdownDTO> fetchActiveAdminsForDropdown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, ADMIN);

        List<AdminDropdownDTO> responseDTOS = adminRepository.fetchActiveAdminsForDropDown();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, ADMIN);

        List<AdminMinimalResponseDTO> responseDTOS = adminRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_STARTED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AdminDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, ADMIN);

        AdminDetailResponseDTO responseDTO = adminRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, ADMIN);

        Admin admin = findById(deleteRequestDTO.getId());

        if (admin.getProfileId().getIsSuperAdminProfile().equals(YES))
            throw new BadRequestException(INVALID_DELETE_REQUEST);

        deleteAdminMetaInfo(admin, deleteRequestDTO);

        save(convertAdminToDeleted(admin, deleteRequestDTO));

        log.info(DELETING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void resetPassword(AdminResetPasswordRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PASSWORD_PROCESS_STARTED);

        Admin admin = findById(requestDTO.getId());

        save(updateAdminPassword(requestDTO.getPassword(), requestDTO.getRemarks(), admin));

        sendEmail(parseToResetPasswordEmailRequestDTO(requestDTO, admin.getEmail(), admin.getFullName()));

        log.info(UPDATING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void updateAvatar(AdminAvatarUpdateRequestDTO updateRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, ADMIN_AVATAR);

        Admin admin = findById(updateRequestDTO.getAdminId());

        updateAvatar(admin, updateRequestDTO.getAvatar());

        log.info(UPDATING_PROCESS_STARTED, ADMIN_AVATAR, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(@Valid AdminUpdateRequestDTO updateRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, ADMIN);

        validateConstraintViolation(validator.validate(updateRequestDTO));

        Admin admin = findById(updateRequestDTO.getId());

        if (Objects.isNull(admin.getPassword()))
            throw new BadRequestException(BAD_UPDATE_MESSAGE, BAD_UPDATE_DEBUG_MESSAGE);

        List<Object[]> admins = adminRepository.validateDuplicity(updateRequestDTO);

        validateAdminDuplicity(admins,
                updateRequestDTO.getEmail(),
                updateRequestDTO.getMobileNumber(),
                updateRequestDTO.getHospitalId()
        );

        emailIsNotUpdated(updateRequestDTO, admin);

        emailIsUpdated(updateRequestDTO, admin);

        log.info(UPDATING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    private void emailIsNotUpdated(AdminUpdateRequestDTO updateRequestDTO,
                                   Admin admin) {

        if (updateRequestDTO.getEmail().equals(admin.getEmail())) {

            EmailRequestDTO emailRequestDTO = parseUpdatedInfo(updateRequestDTO, admin);

            update(updateRequestDTO, updateRequestDTO.getStatus(), admin);

            if (updateRequestDTO.getIsAvatarUpdate().equals(YES))
                updateAvatar(admin, updateRequestDTO.getAvatar());

            updateMacAddressInfo(updateRequestDTO.getMacAddressUpdateInfo(), admin);

            updateAdminDashboardFeature(updateRequestDTO.getAdminDashboardRequestDTOS(), admin);

            updateAdminMetaInfo(admin);

            saveEmailToSend(emailRequestDTO);
        }
    }

    private void emailIsUpdated(AdminUpdateRequestDTO updateRequestDTO,
                                Admin admin) {

        if (!updateRequestDTO.getEmail().equals(admin.getEmail())) {

            EmailRequestDTO emailRequestDTO = parseUpdatedInfo(updateRequestDTO, admin);

            AdminConfirmationToken adminConfirmationToken = saveAdminConfirmationToken(admin);

            EmailRequestDTO emailRequestDTOForNewEmail = convertAdminUpdateRequestToEmailRequestDTO(updateRequestDTO,
                    adminConfirmationToken.getConfirmationToken());

            update(updateRequestDTO, INACTIVE, admin);

            if (updateRequestDTO.getIsAvatarUpdate().equals(YES))
                updateAvatar(admin, updateRequestDTO.getAvatar());

            updateMacAddressInfo(updateRequestDTO.getMacAddressUpdateInfo(), admin);

            updateAdminDashboardFeature(updateRequestDTO.getAdminDashboardRequestDTOS(), admin);

            updateAdminMetaInfo(admin);

            saveEmailToSend(emailRequestDTOForNewEmail);

            saveEmailToSend(emailRequestDTO);
        }
    }

    private void updateAdminDashboardFeature(List<AdminDashboardRequestDTO> adminDashboardRequestDTOS, Admin admin) {

        List<AdminDashboardFeature> adminDashboardFeatureList = new ArrayList<>();
        adminDashboardRequestDTOS.forEach(result -> {

            AdminDashboardFeature adminDashboardFeature =
                    adminDashboardFeatureRepository.findAdminDashboardFeatureBydashboardFeatureId(
                            result.getId(),
                            admin.getId()
                    );

            if (adminDashboardFeature == null) {
                Character status = result.getStatus();
                updateAdminDashboardFeature(result.getId(), result.getStatus(), admin);
            }

            if (adminDashboardFeature != null) {
                adminDashboardFeature.setStatus(result.getStatus());
                adminDashboardFeatureList.add(adminDashboardFeature);
            }

        });

        adminDashboardFeatureRepository.saveAll(adminDashboardFeatureList);

    }

    private void saveAdminDashboardFeature(Long id, Admin admin) {

        DashboardFeature dashboardFeature = dashboardFeatureRepository.findActiveDashboardFeatureById(id)
                .orElseThrow(() -> new NoContentFoundException(DashboardFeature.class));


        List<DashboardFeature> dashboardFeatureList = Arrays.asList(dashboardFeature);
        adminDashboardFeatureRepository.saveAll(parseToAdminDashboardFeature(dashboardFeatureList, admin));

    }

    private void updateAdminDashboardFeature(Long id, Character status, Admin admin) {

        DashboardFeature dashboardFeature = dashboardFeatureRepository.findActiveDashboardFeatureById(id)
                .orElseThrow(() -> new NoContentFoundException(DashboardFeature.class));

        List<DashboardFeature> dashboardFeatureList = Arrays.asList(dashboardFeature);
        adminDashboardFeatureRepository.saveAll(parseToUpdateAdminDashboardFeature(dashboardFeatureList, status, admin));

    }

    @Override
    public List<AdminMetaInfoResponseDTO> fetchAdminMetaInfoResponseDto() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN_META_INFO);

        List<AdminMetaInfoResponseDTO> metaInfoResponseDTOS =
                adminMetaInfoRepository.fetchAdminMetaInfoResponseDTOS();

        log.info(SAVING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return metaInfoResponseDTOS;
    }

    @Override
    public List<AdminMetaInfoResponseDTO> fetchAdminMetaInfoByCompanyIdResponseDto(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN_META_INFO);

        List<AdminMetaInfoResponseDTO> metaInfoResponseDTOS =
                adminMetaInfoRepository.fetchAdminMetaInfoByCompanyIdResponseDTOS(id);

        log.info(SAVING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return metaInfoResponseDTOS;
    }

    @Override
    public List<AdminMetaInfoResponseDTO> fetchAdminMetaInfoByClientIdResponseDto(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN_META_INFO);

        List<AdminMetaInfoResponseDTO> metaInfoResponseDTOS =
                adminMetaInfoRepository.fetchAdminMetaInfoByClientIdResponseDTOS(id);

        log.info(SAVING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
        log.info(FETCHING_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return metaInfoResponseDTOS;
    }

    /*CAN SAVE NUMBER OF ADMINS BASED ON HOSPITAL*/
    private void validateAdminCount(Long hospitalId) {

        Object[] objects = adminRepository.validateAdminCount(hospitalId);
        Long savedAdmin = (Long) get(objects, 0);
        Integer numberOfAdminsAllowed = (Integer) get(objects, 1);

        if (savedAdmin.intValue() == numberOfAdminsAllowed) {
            log.error(ADMIN_CANNOT_BE_REGISTERED_DEBUG_MESSAGE);
            throw new BadRequestException(ADMIN_CANNOT_BE_REGISTERED_MESSAGE,
                    ADMIN_CANNOT_BE_REGISTERED_DEBUG_MESSAGE);
        }
    }

    private Admin saveAdmin(AdminRequestDTO adminRequestDTO) {
        Gender gender = fetchGender(adminRequestDTO.getGenderCode());

        Profile profile = fetchProfile(adminRequestDTO.getProfileId());

        Admin admin = parseAdminDetails(adminRequestDTO, gender, profile);

        return save(admin);
    }

    private void saveAdminAvatar(Admin admin, String avatar) {
        if (!Objects.isNull(avatar))
            saveAdminAvatar(convertFileToAdminAvatar(new AdminAvatar(), avatar, admin));
    }

    private void saveAdminAvatar(AdminAvatar adminAvatar) {
        adminAvatarRepository.save(adminAvatar);
    }

    private void saveMacAddressInfo(Admin admin, List<String> macAddresses) {
        if (admin.getHasMacBinding().equals(YES)) {
            validateMacAddressInfoSize.accept(macAddresses);

            List<AdminMacAddressInfo> adminMacAddressInfos = macAddresses
                    .stream().map(macAddress -> convertToMACAddressInfo(macAddress, admin))
                    .collect(Collectors.toList());

            saveMacAddressInfo(adminMacAddressInfos);
        }
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    private void saveMacAddressInfo(List<AdminMacAddressInfo> adminMacAddressInfos) {
        adminMacAddressInfoRepository.saveAll(adminMacAddressInfos);
    }

    private void saveAdminMetaInfo(Admin admin) {
        adminMetaInfoRepository.save(parseInAdminMetaInfo(admin));
    }

    private void saveAdminFeature(Admin admin) {
        adminFeatureService.save(admin);
    }

    private void updateAdminMetaInfo(Admin admin) {
        AdminMetaInfo adminMetaInfo = adminMetaInfoRepository.findAdminMetaInfoByAdminId(admin.getId())
                .orElseThrow(() -> new NoContentFoundException(AdminMetaInfo.class));

        adminMetaInfoRepository.save(parseMetaInfo(admin, adminMetaInfo));
    }

    private void deleteAdminMetaInfo(Admin admin, DeleteRequestDTO deleteRequestDTO) {
        AdminMetaInfo adminMetaInfo = adminMetaInfoRepository.findAdminMetaInfoByAdminId(admin.getId())
                .orElseThrow(() -> new NoContentFoundException(AdminMetaInfo.class));

        adminMetaInfoRepository.save(deleteMetaInfo(adminMetaInfo, deleteRequestDTO));
    }

    private AdminConfirmationToken saveAdminConfirmationToken(Admin admin) {
        AdminConfirmationToken adminConfirmationToken = parseInAdminConfirmationToken(admin);
        return confirmationTokenRepository.save(adminConfirmationToken);
    }

    private void saveAllAdminDashboardFeature(List<AdminDashboardRequestDTO> dashboardRequestDTOList, Admin admin) {

        if (dashboardRequestDTOList.size() > 0) {
            List<DashboardFeature> dashboardFeatureList = findActiveDashboardFeatures(dashboardRequestDTOList);
            adminDashboardFeatureRepository.saveAll(parseToAdminDashboardFeature(dashboardFeatureList, admin));
        }
    }

    private List<DashboardFeature> findActiveDashboardFeatures(List<AdminDashboardRequestDTO> adminDashboardRequestDTOS) {

        String ids = adminDashboardRequestDTOS.stream()
                .map(request -> request.getId().toString())
                .collect(Collectors.joining(COMMA_SEPARATED));

        List<DashboardFeature> dashboardFeatureList = validateDashboardFeature(ids);
        int requestCount = adminDashboardRequestDTOS.size();

        if ((dashboardFeatureList.size()) != requestCount) {
            log.error(CONTENT_NOT_FOUND, DashboardFeature.class.getSimpleName());
            throw new NoContentFoundException(DashboardFeature.class);
        }

        return dashboardFeatureList;
    }

    private List<DashboardFeature> validateDashboardFeature(String ids) {
        return dashboardFeatureRepository.validateDashboardFeatureCount(ids);
    }

    private void sendEmail(EmailRequestDTO emailRequestDTO) {
        emailService.sendEmail(emailRequestDTO);
    }

    private void saveEmailToSend(EmailRequestDTO emailRequestDTO) {
        emailService.saveEmailToSend(emailRequestDTO);
    }

    private Admin findById(Long adminId) {
        return adminRepository.findAdminById(adminId)
                .orElseThrow(() -> ADMIN_WITH_GIVEN_ID_NOT_FOUND.apply(adminId));
    }

    private void validateAdminDuplicity(List<Object[]> adminList, String requestEmail,
                                        String requestMobileNumber, Long requestedHospitalId) {

        final int EMAIL = 0;
        final int MOBILE_NUMBER = 1;
        final int HOSPITAL_ID = 2;

        adminList.forEach(admin -> {
            boolean isEmailExists = requestEmail.equalsIgnoreCase((String) get(admin, EMAIL));
            boolean isMobileNumberExists = requestMobileNumber.equalsIgnoreCase((String) get(admin, MOBILE_NUMBER));
            Long hospitalId = (Long) get(admin, HOSPITAL_ID);

              /*THIS MEANS ADMIN WITH SAME EMAIL/ MOBILE NUMBER ALREADY EXISTS IN REQUESTED HOSPITAL*/
            if (hospitalId.equals(requestedHospitalId)) {
                if (isEmailExists && isMobileNumberExists) {
                    log.error(String.format(ADMIN_DUPLICATION_MESSAGE, requestEmail, requestMobileNumber));
                    ADMIN_DUPLICATION(requestEmail, requestMobileNumber);
                }

                validateEmail(isEmailExists, requestEmail);
                validateMobileNumber(isMobileNumberExists, requestMobileNumber);
            }
              /*THIS MEANS ADMIN WITH SAME EMAIL/ MOBILE NUMBER ALREADY EXISTS IN ANOTHER HOSPITAL*/
            else {
                if (isEmailExists && isMobileNumberExists)
                    ADMIN_DUPLICATION_IN_DIFFERENT_HOSPITAL(requestEmail, requestMobileNumber);

                validateEmailInDifferentHospital(isEmailExists, requestEmail);
                validateMobileNumberInDifferentHospital(isMobileNumberExists, requestMobileNumber);
            }
        });
    }

    private void updateAvatar(Admin admin, String avatar) {
        AdminAvatar adminAvatar = adminAvatarRepository.findAdminAvatarByAdminId(admin.getId());

        if (Objects.isNull(adminAvatar)) saveAdminAvatar(admin, avatar);
        else updateAdminAvatar(admin, adminAvatar, avatar);
    }

    private void updateAdminAvatar(Admin admin,
                                   AdminAvatar adminAvatar,
                                   String avatar) {

        if (!Objects.isNull(avatar)) {
            convertFileToAdminAvatar(adminAvatar, avatar, admin);
        } else adminAvatar.setStatus(INACTIVE);

        saveAdminAvatar(adminAvatar);
    }

    public void update(AdminUpdateRequestDTO adminRequestDTO, Character status, Admin admin) {
        Gender gender = fetchGender(adminRequestDTO.getGenderCode());

        Profile profile = fetchProfile(adminRequestDTO.getProfileId());

        save(convertAdminUpdateRequestDTOToAdmin(admin, adminRequestDTO, gender, profile, status));
    }

    private void updateMacAddressInfo(List<AdminMacAddressInfoUpdateRequestDTO> adminMacAddressInfoUpdateRequestDTOS,
                                      Admin admin) {

        List<AdminMacAddressInfo> adminMacAddressInfos = convertToUpdatedMACAddressInfo(
                adminMacAddressInfoUpdateRequestDTOS, admin);

        saveMacAddressInfo(adminMacAddressInfos);
    }

    private EmailRequestDTO parseUpdatedInfo(AdminUpdateRequestDTO adminRequestDTO, Admin admin) {
        return parseToEmailRequestDTO(admin.getFullName(), adminRequestDTO,
                parseUpdatedValues(admin, adminRequestDTO), parseUpdatedMacAddress(adminRequestDTO));
    }

    private Consumer<List<String>> validateMacAddressInfoSize = (macInfos) -> {
        if (ObjectUtils.isEmpty(macInfos)) {
            log.error(CONTENT_NOT_FOUND, AdminMacAddressInfo.class.getSimpleName());
            throw new NoContentFoundException(AdminMacAddressInfo.class);
        }
    };

    private void ADMIN_DUPLICATION(String email, String mobileNumber) {
        throw new DataDuplicationException(String.format(ADMIN_DUPLICATION_MESSAGE, email, mobileNumber));
    }

    private void ADMIN_DUPLICATION_IN_DIFFERENT_HOSPITAL(String email, String mobileNumber) {
        throw new DataDuplicationException(String.format(ADMIN_DUPLICATION_IN_DIFFERENT_HOSPITAL_MESSAGE, email, mobileNumber));
    }

    private void validateEmail(boolean isEmailExists, String email) {
        if (isEmailExists) {
            log.error(DUPLICATION_ERROR, ADMIN, email);
            throw new DataDuplicationException(String.format(EMAIL_DUPLICATION_MESSAGE, email));
        }
    }

    private void validateEmailInDifferentHospital(boolean isEmailExists, String email) {
        if (isEmailExists) {
            log.error(DUPLICATION_ERROR, ADMIN, email);
            throw new DataDuplicationException(String.format(EMAIL_DUPLICATION_IN_DIFFERENT_HOSPITAL_MESSAGE, email));
        }
    }

    private void validateMobileNumber(boolean isMobileNumberExists, String mobileNumber) {
        if (isMobileNumberExists) {
            log.error(DUPLICATION_ERROR, ADMIN, mobileNumber);
            throw new DataDuplicationException(String.format(MOBILE_NUMBER_DUPLICATION_MESSAGE, mobileNumber));
        }
    }

    private void validateMobileNumberInDifferentHospital(boolean isMobileNumberExists, String mobileNumber) {
        if (isMobileNumberExists) {
            log.error(DUPLICATION_ERROR, ADMIN, mobileNumber);
            throw new DataDuplicationException(String.format(MOBILE_NUMBER_DUPLICATION_IN_DIFFERENT_HOSPITAL_MESSAGE, mobileNumber));
        }
    }

    private Function<Long, NoContentFoundException> ADMIN_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, ADMIN, id);
        throw new NoContentFoundException(Admin.class, "id", id.toString());
    };

    private Gender fetchGender(Character genderCode) {
        return fetchGenderByCode(genderCode);
    }

    private Profile fetchProfile(Long profileId) {
        return profileService.fetchActiveProfileById(profileId);
    }


}
