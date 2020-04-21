package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminInfoRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.*;
import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminLoggedInInfoResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminMetaInfoResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.exception.OperationUnsuccessfulException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.*;
import com.cogent.cogentappointment.admin.validator.LoginValidator;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.AdminServiceMessages.*;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.*;
import static com.cogent.cogentappointment.admin.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.AdminLog.*;
import static com.cogent.cogentappointment.admin.utils.AdminUtils.*;
import static com.cogent.cogentappointment.admin.utils.CompanyAdminUtils.*;
import static com.cogent.cogentappointment.admin.utils.DashboardFeatureUtils.parseToAdminDashboardFeature;
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
public class CompanyAdminServiceImpl implements CompanyAdminService {

    private final Validator validator;

    private final AdminRepository adminRepository;

    private final AdminMacAddressInfoRepository adminMacAddressInfoRepository;

    private final AdminMetaInfoRepository adminMetaInfoRepository;

    private final AdminAvatarRepository adminAvatarRepository;

    private final AdminConfirmationTokenRepository confirmationTokenRepository;

    private final MinioFileService minioFileService;

    private final EmailService emailService;

    private final ProfileService profileService;

    private final DashboardFeatureRepository dashboardFeatureRepository;

    private final AdminDashboardFeatureRepository adminDashboardFeatureRepository;

    private final AdminFeatureService adminFeatureService;

    public CompanyAdminServiceImpl(Validator validator,
                                   AdminRepository adminRepository,
                                   AdminMacAddressInfoRepository adminMacAddressInfoRepository,
                                   AdminMetaInfoRepository adminMetaInfoRepository,
                                   AdminAvatarRepository adminAvatarRepository,
                                   AdminConfirmationTokenRepository confirmationTokenRepository,
                                   MinioFileService minioFileService, EmailService emailService,
                                   ProfileService profileService,
                                   DashboardFeatureRepository dashboardFeatureRepository,
                                   AdminDashboardFeatureRepository adminDashboardFeatureRepository,
                                   AdminFeatureService adminFeatureService) {
        this.validator = validator;
        this.adminRepository = adminRepository;
        this.adminMacAddressInfoRepository = adminMacAddressInfoRepository;
        this.adminMetaInfoRepository = adminMetaInfoRepository;
        this.adminAvatarRepository = adminAvatarRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.minioFileService = minioFileService;
        this.emailService = emailService;
        this.profileService = profileService;
        this.dashboardFeatureRepository = dashboardFeatureRepository;
        this.adminDashboardFeatureRepository = adminDashboardFeatureRepository;
        this.adminFeatureService = adminFeatureService;
    }

    @Override
    public void save(@Valid CompanyAdminRequestDTO adminRequestDTO, MultipartFile files) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, ADMIN);

        validateConstraintViolation(validator.validate(adminRequestDTO));

        List<Object[]> admins = adminRepository.validateDuplicityForCompanyAdmin(adminRequestDTO.getEmail(),
                adminRequestDTO.getMobileNumber());

        validateCompanyAdminDuplicity(admins, adminRequestDTO.getEmail(),
                adminRequestDTO.getMobileNumber());

        Admin admin = save(adminRequestDTO);

        saveAdminAvatar(admin, files);

        saveMacAddressInfo(admin, adminRequestDTO.getMacAddressInfo());

        saveAdminMetaInfo(admin);

        saveAdminFeature(admin);

        saveAllAdminDashboardFeature(adminRequestDTO.getAdminDashboardRequestDTOS(), admin);

        AdminConfirmationToken adminConfirmationToken =
                saveAdminConfirmationToken(parseInAdminConfirmationToken(admin));

        EmailRequestDTO emailRequestDTO = convertCompanyAdminRequestToEmailRequestDTO(adminRequestDTO, admin,
                adminConfirmationToken.getConfirmationToken());

        saveEmailToSend(emailRequestDTO);

        log.info(SAVING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveCompanyAdminsForDropdown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, ADMIN);

        List<DropDownResponseDTO> responseDTOS = adminRepository.fetchActiveCompanyAdminsForDropDown();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<CompanyAdminMinimalResponseDTO> search(CompanyAdminSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, ADMIN);

        List<CompanyAdminMinimalResponseDTO> responseDTOS = adminRepository.searchCompanyAdmin(searchRequestDTO,
                pageable);

        log.info(SEARCHING_PROCESS_STARTED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public CompanyAdminDetailResponseDTO fetchCompanyAdminDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, ADMIN);

        CompanyAdminDetailResponseDTO responseDTO = adminRepository.fetchCompanyAdminDetailsById(id);

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

        convertAdminToDeleted(admin, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void changePassword(AdminChangePasswordRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PASSWORD_PROCESS_STARTED);

        Admin admin = findById(requestDTO.getId());

        validatePassword(admin, requestDTO);

        save(updateAdminPassword(requestDTO.getNewPassword(), requestDTO.getRemarks(), admin));

        log.info(UPDATING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
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
    public void updateAvatar(MultipartFile files, Long adminId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, ADMIN_AVATAR);

        Admin admin = findById(adminId);

        updateAvatar(admin, files);

        log.info(UPDATING_PROCESS_STARTED, ADMIN_AVATAR, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(@Valid CompanyAdminUpdateRequestDTO updateRequestDTO, MultipartFile files) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, ADMIN);

        validateConstraintViolation(validator.validate(updateRequestDTO));

        Admin admin = findById(updateRequestDTO.getId());

        List<Object[]> admins = adminRepository.validateCompanyAdminDuplicity(updateRequestDTO);

        validateCompanyAdminDuplicity(admins, updateRequestDTO.getEmail(),
                updateRequestDTO.getMobileNumber());

        emailIsNotUpdated(updateRequestDTO, admin, files);

        emailIsUpdated(updateRequestDTO, admin, files);

        log.info(UPDATING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    private void emailIsNotUpdated(CompanyAdminUpdateRequestDTO updateRequestDTO,
                                   Admin admin, MultipartFile files) {
        if (updateRequestDTO.getEmail().equals(admin.getEmail())) {

            EmailRequestDTO emailRequestDTO = parseUpdatedInfo(updateRequestDTO, admin);

            updateCompanyAdmin(updateRequestDTO, ACTIVE, admin);

            if (updateRequestDTO.getIsAvatarUpdate().equals(YES))
                updateAvatar(admin, files);

            updateMacAddressInfo(updateRequestDTO.getMacAddressUpdateInfo(), admin);

            updateAdminDashboardFeature(updateRequestDTO.getAdminDashboardRequestDTOS(), admin);

            updateAdminMetaInfo(admin);

            saveEmailToSend(emailRequestDTO);
        }
    }

    private void emailIsUpdated(CompanyAdminUpdateRequestDTO updateRequestDTO,
                                Admin admin, MultipartFile files) {
        if (!updateRequestDTO.getEmail().equals(admin.getEmail())) {

            EmailRequestDTO emailRequestDTO = parseUpdatedInfo(updateRequestDTO, admin);

            AdminConfirmationToken adminConfirmationToken =
                    saveAdminConfirmationToken(parseInAdminConfirmationToken(admin));

            EmailRequestDTO emailRequestDTOForNewEmail = convertCompanyAdminUpdateRequestToEmailRequestDTO(updateRequestDTO,
                    adminConfirmationToken.getConfirmationToken());

            updateCompanyAdmin(updateRequestDTO, INACTIVE, admin);

            if (updateRequestDTO.getIsAvatarUpdate().equals(YES))
                updateAvatar(admin, files);

            updateMacAddressInfo(updateRequestDTO.getMacAddressUpdateInfo(), admin);

            updateAdminDashboardFeature(updateRequestDTO.getAdminDashboardRequestDTOS(), admin);

            updateAdminMetaInfo(admin);

            sendEmail(emailRequestDTOForNewEmail);

            sendEmail(emailRequestDTO);
        }
    }

    @Override
    public void verifyConfirmationToken(String token) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(VERIFY_CONFIRMATION_TOKEN_PROCESS_STARTED);

        Object status = confirmationTokenRepository.findByConfirmationToken(token);

        validateStatus(status);

        log.info(VERIFY_CONFIRMATION_TOKEN_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void verifyConfirmationTokenForEmail(String token) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(VERIFY_CONFIRMATION_TOKEN_FOR_EMAIL_PROCESS_STARTED);

        AdminConfirmationToken adminConfirmationToken =
                confirmationTokenRepository.findAdminConfirmationTokenByToken(token)
                        .orElseThrow(() -> CONFIRMATION_TOKEN_NOT_FOUND.apply(token));

        validateStatus(adminConfirmationToken.getStatus());

        Admin admin = adminRepository.findAdminByIdForEmailVerification(adminConfirmationToken.getAdmin().getId());

        admin.setStatus(ACTIVE);

        save(admin);

        adminConfirmationToken.setStatus(INACTIVE);

        saveAdminConfirmationToken(adminConfirmationToken);

        log.info(VERIFY_CONFIRMATION_TOKEN_FOR_EMAIL_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void savePassword(AdminPasswordRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PASSWORD_PROCESS_STARTED);

        AdminConfirmationToken adminConfirmationToken =
                confirmationTokenRepository.findAdminConfirmationTokenByToken(requestDTO.getToken())
                        .orElseThrow(() -> CONFIRMATION_TOKEN_NOT_FOUND.apply(requestDTO.getToken()));

        save(saveAdminPassword(requestDTO, adminConfirmationToken));

        adminConfirmationToken.setStatus(INACTIVE);

        saveAdminConfirmationToken(adminConfirmationToken);

        log.info(SAVING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public CompanyAdminLoggedInInfoResponseDTO fetchLoggedInCompanyAdminInfo(CompanyAdminInfoRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN);

        CompanyAdminLoggedInInfoResponseDTO responseDTO = adminRepository.fetchLoggedInCompanyAdminInfo(requestDTO);

        log.info(FETCHING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<CompanyAdminMetaInfoResponseDTO> fetchCompanyAdminMetaInfoResponseDto() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN_META_INFO);

        List<CompanyAdminMetaInfoResponseDTO> metaInfoResponseDTOS =
                adminMetaInfoRepository.fetchCompanyAdminMetaInfoResponseDTOS();

        log.info(SAVING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return metaInfoResponseDTOS;
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
                .collect(Collectors.joining(","));

        List<DashboardFeature> dashboardFeatureList = validateDashboardFeature(ids);
        int requestCount = adminDashboardRequestDTOS.size();

        if ((dashboardFeatureList.size()) != requestCount) {
            throw new NoContentFoundException(DashboardFeature.class);
        }

        return dashboardFeatureList;

    }

    private List<DashboardFeature> validateDashboardFeature(String ids) {
        return dashboardFeatureRepository.validateDashboardFeatureCount(ids);
    }


    private void updateAdminDashboardFeature(List<AdminDashboardRequestDTO> adminDashboardRequestDTOS, Admin admin) {

        List<AdminDashboardFeature> adminDashboardFeatureList = new ArrayList<>();
        adminDashboardRequestDTOS.forEach(result -> {

            AdminDashboardFeature adminDashboardFeature = adminDashboardFeatureRepository.findAdminDashboardFeatureBydashboardFeatureId(result.getId(), admin.getId());

            if (adminDashboardFeature == null) {
                saveAdminDashboardFeature(result.getId(), admin);
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

    private void validateStatus(Object status) {
        if (status.equals(INACTIVE)) {
            log.error(ADMIN_REGISTERED);
            throw ADMIN_ALREADY_REGISTERED.get();
        }
    }

    private void validateCompanyAdminDuplicity(List<Object[]> adminList, String requestEmail,
                                               String requestMobileNumber) {

        final int EMAIL = 0;
        final int MOBILE_NUMBER = 1;

        adminList.forEach(admin -> {
            boolean isEmailExists = requestEmail.equalsIgnoreCase((String) get(admin, EMAIL));
            boolean isMobileNumberExists = requestMobileNumber.equalsIgnoreCase((String) get(admin, MOBILE_NUMBER));

            if (isEmailExists && isMobileNumberExists) {
                log.error(ADMIN_DUPLICATION_MESSAGE);
                throw ADMIN_DUPLICATION.get();
            }

            validateEmail(isEmailExists, requestEmail);
            validateMobileNumber(isMobileNumberExists, requestMobileNumber);
        });
    }

    private void validateEmail(boolean isEmailExists, String email) {
        if (isEmailExists) {
            log.error(EMAIL_DUPLICATION_MESSAGE);
            throw new DataDuplicationException(
                    String.format(EMAIL_DUPLICATION_MESSAGE, Admin.class.getSimpleName(), email));
        }
    }

    private void validateMobileNumber(boolean isMobileNumberExists, String mobileNumber) {
        if (isMobileNumberExists) {
            log.error(MOBILE_NUMBER_DUPLICATION_MESSAGE);
            throw new DataDuplicationException(
                    String.format(MOBILE_NUMBER_DUPLICATION_MESSAGE, Admin.class.getSimpleName(), mobileNumber));
        }
    }

    private Admin save(CompanyAdminRequestDTO adminRequestDTO) {
        Gender gender = fetchGender(adminRequestDTO.getGenderCode());

        Profile profile = fetchProfile(adminRequestDTO.getProfileId());

        Admin admin = parseCompanyAdminDetails(adminRequestDTO, gender, profile);

        return save(admin);
    }

    private void saveAdminAvatar(Admin admin, MultipartFile files) {
        if (!Objects.isNull(files)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(admin, new MultipartFile[]{files});
            saveAdminAvatar(convertFileToAdminAvatar(responseList.get(0), admin));
        }
    }

    private List<FileUploadResponseDTO> uploadFiles(Admin admin, MultipartFile[] files) {
        String subDirectory = admin.getEmail();

        return minioFileService.addAttachmentIntoSubDirectory(subDirectory, files);
    }

    private void updateAdminAvatar(Admin admin, AdminAvatar adminAvatar, MultipartFile files) {
        if (!Objects.isNull(files)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(admin, new MultipartFile[]{files});
            setFileProperties(responseList.get(0), adminAvatar);
        } else adminAvatar.setStatus(INACTIVE);

        saveAdminAvatar(adminAvatar);
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

    private AdminConfirmationToken saveAdminConfirmationToken(AdminConfirmationToken adminConfirmationToken) {
        return confirmationTokenRepository.save(adminConfirmationToken);
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

    private void updateAvatar(Admin admin, MultipartFile files) {
        AdminAvatar adminAvatar = adminAvatarRepository.findAdminAvatarByAdminId(admin.getId());

        if (Objects.isNull(adminAvatar)) saveAdminAvatar(admin, files);
        else updateAdminAvatar(admin, adminAvatar, files);
    }

    private void updateCompanyAdmin(CompanyAdminUpdateRequestDTO adminRequestDTO, Character status, Admin admin) {
        Gender gender = fetchGender(adminRequestDTO.getGenderCode());

        Profile profile = fetchProfile(adminRequestDTO.getProfileId());

        convertCompanyAdminUpdateRequestDTOToAdmin(admin, adminRequestDTO, gender, profile, status);
    }

    private void updateMacAddressInfo(List<AdminMacAddressInfoUpdateRequestDTO> adminMacAddressInfoUpdateRequestDTOS,
                                      Admin admin) {

        List<AdminMacAddressInfo> adminMacAddressInfos = convertToUpdatedMACAddressInfo(
                adminMacAddressInfoUpdateRequestDTOS, admin);

        saveMacAddressInfo(adminMacAddressInfos);
    }

    private EmailRequestDTO parseUpdatedInfo(CompanyAdminUpdateRequestDTO adminRequestDTO, Admin admin) {
        return parseToEmailRequestDTOForCompanyAdmin(admin.getFullName(), adminRequestDTO,
                parseUpdatedCompanyAdminValues(admin, adminRequestDTO),
                parseUpdatedMacAddressForCompanyAdmin(adminRequestDTO));
    }

    private Consumer<List<String>> validateMacAddressInfoSize = (macInfos) -> {
        if (ObjectUtils.isEmpty(macInfos))
            throw new NoContentFoundException(AdminMacAddressInfo.class);
    };

    private void validatePassword(Admin admin, AdminChangePasswordRequestDTO requestDTO) {

        if (!LoginValidator.checkPassword(requestDTO.getOldPassword(), admin.getPassword()))
            throw new OperationUnsuccessfulException(PASSWORD_MISMATCH_MESSAGE);

        if (LoginValidator.checkPassword(requestDTO.getNewPassword(), admin.getPassword()))
            throw new DataDuplicationException(DUPLICATE_PASSWORD_MESSAGE);
    }

    private Supplier<DataDuplicationException> ADMIN_DUPLICATION = () ->
            new DataDuplicationException(ADMIN_DUPLICATION_MESSAGE);

    private Function<Long, NoContentFoundException> ADMIN_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Admin.class, "id", id.toString());
    };

    private Supplier<BadRequestException> ADMIN_ALREADY_REGISTERED = () -> new BadRequestException(ADMIN_REGISTERED);

    private Function<String, NoContentFoundException> CONFIRMATION_TOKEN_NOT_FOUND = (confirmationToken) -> {
        throw new NoContentFoundException(INVALID_CONFIRMATION_TOKEN, "confirmationToken", confirmationToken);
    };

    private Gender fetchGender(Character genderCode) {
        return fetchGenderByCode(genderCode);
    }

    private Profile fetchProfile(Long profileId) {
        return profileService.fetchActiveProfileById(profileId);
    }
}
