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
import com.cogent.cogentappointment.admin.dto.response.integration.ApiInfoResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationBodyAttributeResponse;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationRequestBodyAttributeResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminModeFeatureIntegrationResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.FeatureIntegrationResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientFeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientIntegrationResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.exception.OperationUnsuccessfulException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.repository.custom.AdminModeFeatureIntegrationRepository;
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
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.AdminServiceMessages.*;
import static com.cogent.cogentappointment.admin.constants.IntegrationApiConstants.*;
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
import static java.util.stream.Collectors.groupingBy;

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

    private final IntegrationRepository integrationRepository;
    private final IntegrationRequestBodyParametersRepository requestBodyParametersRepository;
    private final AppointmentModeHospitalInfoRepository appointmentModeHospitalInfoRepository;
    private final AdminModeApiFeatureIntegrationRepository adminModeApiFeatureIntegrationRepository;
    private final AdminModeFeatureIntegrationRepository adminModeFeatureIntegrationRepository;

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
                                   AdminFeatureService adminFeatureService,
                                   IntegrationRepository integrationRepository,
                                   IntegrationRequestBodyParametersRepository requestBodyParametersRepository,
                                   AppointmentModeHospitalInfoRepository appointmentModeHospitalInfoRepository,
                                   AdminModeApiFeatureIntegrationRepository adminModeApiFeatureIntegrationRepository,
                                   AdminModeFeatureIntegrationRepository adminModeFeatureIntegrationRepository) {
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
        this.integrationRepository = integrationRepository;
        this.requestBodyParametersRepository = requestBodyParametersRepository;
        this.appointmentModeHospitalInfoRepository = appointmentModeHospitalInfoRepository;
        this.adminModeApiFeatureIntegrationRepository = adminModeApiFeatureIntegrationRepository;
        this.adminModeFeatureIntegrationRepository = adminModeFeatureIntegrationRepository;
    }

    @Override
    public void save(@Valid CompanyAdminRequestDTO adminRequestDTO, MultipartFile files) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, ADMIN);

        validateConstraintViolation(validator.validate(adminRequestDTO));

        List<Object[]> admins = adminRepository.validateDuplicityForCompanyAdmin(
                adminRequestDTO.getEmail(),
                adminRequestDTO.getMobileNumber()
        );

        validateCompanyAdminDuplicity(admins,
                adminRequestDTO.getEmail(),
                adminRequestDTO.getMobileNumber(),
                adminRequestDTO.getCompanyId()
        );

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

        deleteAdminMetaInfo(admin, deleteRequestDTO);

        save(convertAdminToDeleted(admin, deleteRequestDTO));

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

        if (Objects.isNull(admin.getPassword()))
            throw new BadRequestException(BAD_UPDATE_MESSAGE, BAD_UPDATE_DEBUG_MESSAGE);

        List<Object[]> admins = adminRepository.validateCompanyAdminDuplicity(updateRequestDTO);

        validateCompanyAdminDuplicity(
                admins, updateRequestDTO.getEmail(),
                updateRequestDTO.getMobileNumber(),
                updateRequestDTO.getCompanyId());

        emailIsNotUpdated(updateRequestDTO, admin, files);

        emailIsUpdated(updateRequestDTO, admin, files);

        log.info(UPDATING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    private void emailIsNotUpdated(CompanyAdminUpdateRequestDTO updateRequestDTO,
                                   Admin admin, MultipartFile files) {
        if (updateRequestDTO.getEmail().equals(admin.getEmail())) {

            EmailRequestDTO emailRequestDTO = parseUpdatedInfo(updateRequestDTO, admin);

            updateCompanyAdmin(updateRequestDTO, updateRequestDTO.getStatus(), admin);

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

            saveEmailToSend(emailRequestDTOForNewEmail);

            saveEmailToSend(emailRequestDTO);
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

        save(saveAdminPassword(requestDTO, adminConfirmationToken.getAdmin()));

        adminConfirmationToken.setStatus(INACTIVE);

        saveAdminConfirmationToken(adminConfirmationToken);

        log.info(SAVING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public CompanyAdminLoggedInInfoResponseDTO fetchLoggedInCompanyAdminInfo(CompanyAdminInfoRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN);

        CompanyAdminLoggedInInfoResponseDTO responseDTO = adminRepository.fetchLoggedInCompanyAdminInfo(requestDTO);

        List<IntegrationBodyAttributeResponse> responses =
                requestBodyParametersRepository.fetchRequestBodyAttributes();

        Map<String, String> map = new HashMap<>();

        if(responses!=null) {
            responses.forEach(response -> {
                map.put(response.getName(), "");
            });
        }

        responseDTO.setApiIntegration(getApiIntegrations());
        responseDTO.setRequestBody(map);

        log.info(FETCHING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    private List<AdminModeFeatureIntegrationResponseDTO> getAdminModeApiIntegration() {

        Map<Long, List<AdminFeatureIntegrationResponse>> integrationResponseMap = adminModeFeatureIntegrationRepository.
                fetchAdminModeIntegrationResponseDTO().stream()
                .collect(groupingBy(AdminFeatureIntegrationResponse::getApiIntegrationFormatId));

        List<AdminModeFeatureIntegrationResponseDTO> adminModeFeatureIntegrationResponseDTOS = new ArrayList<>();

        integrationResponseMap.entrySet().stream().forEach(responseMap -> {

            List<FeatureIntegrationResponseDTO> features = new ArrayList<>();

            responseMap.getValue().forEach(responseDTO -> {

                Map<String, String> requestHeaderResponseDTO = integrationRepository.
                        findAdminModeApiRequestHeaders(responseDTO.getApiIntegrationFormatId());

                Map<String, String> queryParametersResponseDTO = integrationRepository.
                        findAdminModeApiQueryParameters(responseDTO.getApiIntegrationFormatId());

                Object[] requestBody = getRequestBodyByFeature(responseDTO.getFeatureId(),
                        responseDTO.getRequestMethod());

                FeatureIntegrationResponseDTO featureIntegrationResponseDTO =
                        convertToAdminApiResponseDTO(responseDTO,
                                requestBody,
                                requestHeaderResponseDTO,
                                queryParametersResponseDTO);

                features.add(featureIntegrationResponseDTO);

            });

            AdminModeFeatureIntegrationResponseDTO adminModeFeatureIntegrationResponseDTO = new AdminModeFeatureIntegrationResponseDTO();
            adminModeFeatureIntegrationResponseDTO.setFeatures(features);
            adminModeFeatureIntegrationResponseDTO.setAppointmentModeId(responseMap.getKey());

            adminModeFeatureIntegrationResponseDTOS.add(adminModeFeatureIntegrationResponseDTO);
        });

        return adminModeFeatureIntegrationResponseDTOS;

    }

    private List<ClientIntegrationResponseDTO> getHospitalApiIntegration() {

        List<ClientFeatureIntegrationResponse> integrationResponseDTOList = integrationRepository.
                fetchClientIntegrationResponseDTO();

        Map<Long, List<ClientFeatureIntegrationResponse>> integrationResponseMap = integrationResponseDTOList.stream()
                .collect(groupingBy(ClientFeatureIntegrationResponse::getHospitalId));

        List<ClientIntegrationResponseDTO> clientIntegrationResponseDTOS = new ArrayList<>();

        integrationResponseMap.entrySet().stream().forEach(responseMap -> {

            List<FeatureIntegrationResponseDTO> features = new ArrayList<>();

            integrationResponseDTOList.forEach(responseDTO -> {

                Map<String, String> requestHeaderResponseDTO = integrationRepository.
                        findApiRequestHeadersResponse(responseDTO.getApiIntegrationFormatId());

                Map<String, String> queryParametersResponseDTO = integrationRepository.
                        findApiQueryParametersResponse(responseDTO.getApiIntegrationFormatId());

                Object[] requestBody = getRequestBodyByFeature(responseDTO.getFeatureId(),
                        responseDTO.getRequestMethod());

                FeatureIntegrationResponseDTO featureIntegrationResponseDTO = convertToClientApiResponseDTO(responseDTO,
                        requestBody,
                        requestHeaderResponseDTO,
                        queryParametersResponseDTO);


                features.add(featureIntegrationResponseDTO);

            });

            ClientIntegrationResponseDTO clientIntegrationResponseDTO = new ClientIntegrationResponseDTO();
            clientIntegrationResponseDTO.setFeatures(features);
            clientIntegrationResponseDTO.setClientId(responseMap.getKey());

            clientIntegrationResponseDTOS.add(clientIntegrationResponseDTO);

        });


        return clientIntegrationResponseDTOS;

    }

    private Object[] getRequestBodyByFeature(Long featureId, String requestMethod) {

        Object[] requestBody = new Object[0];
        if (requestMethod.equalsIgnoreCase("POST")) {
            List<IntegrationRequestBodyAttributeResponse> responses = integrationRepository.
                    fetchRequestBodyAttributeByFeatureId(featureId);

            if (responses != null) {
                requestBody = responses.stream()
                        .map(request -> request.getName())
                        .collect(Collectors.toList()).toArray();
            }
        }

        return requestBody;
    }


    private FeatureIntegrationResponseDTO convertToAdminApiResponseDTO(AdminFeatureIntegrationResponse responseDTO,
                                                                       Object[] requestBody,
                                                                       Map<String, String> requestHeaderResponseDTO,
                                                                       Map<String, String> queryParametersResponseDTO) {

        FeatureIntegrationResponseDTO featureIntegrationResponseDTO =
                FeatureIntegrationResponseDTO.builder()
                        .featureCode(responseDTO.getFeatureCode())
                        .integrationChannelCode(responseDTO.getIntegrationChannelCode())
                        .build();

        if (responseDTO.getIntegrationChannelCode().equalsIgnoreCase(FRONT_END_CODE)) {


            ApiInfoResponseDTO apiInfoResponseDTO = new ApiInfoResponseDTO();

            apiInfoResponseDTO.setUrl(responseDTO.getUrl());
            apiInfoResponseDTO.setRequestBody(requestBody);
            apiInfoResponseDTO.setRequestMethod(responseDTO.getRequestMethod());
            apiInfoResponseDTO.setHeaders(requestHeaderResponseDTO);
            apiInfoResponseDTO.setQueryParameters(queryParametersResponseDTO);

            featureIntegrationResponseDTO.setApiInfo(apiInfoResponseDTO);
        }


        return featureIntegrationResponseDTO;
    }

    private FeatureIntegrationResponseDTO convertToClientApiResponseDTO(ClientFeatureIntegrationResponse responseDTO,
                                                                        Object[] requestBody,
                                                                        Map<String, String> requestHeaderResponseDTO,
                                                                        Map<String, String> queryParametersResponseDTO) {

        FeatureIntegrationResponseDTO featureIntegrationResponseDTO = new FeatureIntegrationResponseDTO();
        featureIntegrationResponseDTO.setFeatureCode(responseDTO.getFeatureCode());
        featureIntegrationResponseDTO.setIntegrationChannelCode(responseDTO.getIntegrationChannelCode());

        if (responseDTO.getIntegrationChannelCode().equalsIgnoreCase(FRONT_END_CODE)) {

            ApiInfoResponseDTO apiInfoResponseDTO = new ApiInfoResponseDTO();
            apiInfoResponseDTO.setUrl(responseDTO.getUrl());
            apiInfoResponseDTO.setRequestBody(requestBody);
            apiInfoResponseDTO.setRequestMethod(responseDTO.getRequestMethod());
            apiInfoResponseDTO.setHeaders(requestHeaderResponseDTO);
            apiInfoResponseDTO.setQueryParameters(queryParametersResponseDTO);

            featureIntegrationResponseDTO.setApiInfo(apiInfoResponseDTO);
        }


        return featureIntegrationResponseDTO;
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

            AdminDashboardFeature adminDashboardFeature = adminDashboardFeatureRepository.
                    findAdminDashboardFeatureBydashboardFeatureId(result.getId(), admin.getId());

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
                                               String requestMobileNumber, Long requestedCompanyId) {

        final int EMAIL = 0;
        final int MOBILE_NUMBER = 1;
        final int COMPANY_ID = 2;

        adminList.forEach(admin -> {
            boolean isEmailExists = requestEmail.equalsIgnoreCase((String) get(admin, EMAIL));
            boolean isMobileNumberExists = requestMobileNumber.equalsIgnoreCase((String) get(admin, MOBILE_NUMBER));
            Long companyId = (Long) get(admin, COMPANY_ID);

            /*THIS MEANS ADMIN WITH SAME EMAIL/ MOBILE NUMBER ALREADY EXISTS IN REQUESTED COMPANY*/
            if (companyId.equals(requestedCompanyId)) {
                if (isEmailExists && isMobileNumberExists)
                    ADMIN_DUPLICATION(requestEmail, requestMobileNumber);

                validateEmail(isEmailExists, requestEmail);
                validateMobileNumber(isMobileNumberExists, requestMobileNumber);
            }
            /*THIS MEANS ADMIN WITH SAME EMAIL/ MOBILE NUMBER ALREADY EXISTS IN ANOTHER COMPANY*/
            else {
                if (isEmailExists && isMobileNumberExists)
                    ADMIN_DUPLICATION_IN_DIFFERENT_HOSPITAL(requestEmail, requestMobileNumber);

                validateEmailInDifferentHospital(isEmailExists, requestEmail);
                validateMobileNumberInDifferentHospital(isMobileNumberExists, requestMobileNumber);
            }
        });
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

    private void deleteAdminMetaInfo(Admin admin, DeleteRequestDTO deleteRequestDTO) {
        AdminMetaInfo adminMetaInfo = adminMetaInfoRepository.findAdminMetaInfoByAdminId(admin.getId())
                .orElseThrow(() -> new NoContentFoundException(AdminMetaInfo.class));

        adminMetaInfoRepository.save(deleteMetaInfo(adminMetaInfo, deleteRequestDTO));
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

    private void validatePassword(Admin admin, AdminChangePasswordRequestDTO requestDTO) {

        if (!LoginValidator.checkPassword(requestDTO.getOldPassword(), admin.getPassword()))
            throw new OperationUnsuccessfulException(PASSWORD_MISMATCH_MESSAGE);

        if (LoginValidator.checkPassword(requestDTO.getNewPassword(), admin.getPassword()))
            throw new DataDuplicationException(DUPLICATE_PASSWORD_MESSAGE);
    }


    private Map<String, List<?>> getApiIntegrations() {

        List<AdminModeFeatureIntegrationResponseDTO> featureIntegrationResponseDTOList =
                getAdminModeApiIntegration();

        List<ClientIntegrationResponseDTO> clientIntegrationResponseDTOList =
                getHospitalApiIntegration();

        Map<String, List<?>> map = new HashMap();

        if (featureIntegrationResponseDTOList.size() != 0 || featureIntegrationResponseDTOList != null) {
            map.put(KEY_CLIENT_INTEGRATION, clientIntegrationResponseDTOList);
        }

        if (clientIntegrationResponseDTOList.size() != 0 || clientIntegrationResponseDTOList != null) {
            map.put(KEY_ADMIN_INTEGRATION, featureIntegrationResponseDTOList);
        }

        return map;
    }


    private Function<Long, NoContentFoundException> ADMIN_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Admin.class, "id", id.toString());
    };

    private Supplier<BadRequestException> ADMIN_ALREADY_REGISTERED = () -> new BadRequestException(ADMIN_REGISTERED);

    private Function<String, NoContentFoundException> CONFIRMATION_TOKEN_NOT_FOUND = (confirmationToken) -> {
        throw new NoContentFoundException(INVALID_CONFIRMATION_TOKEN, "confirmationToken", confirmationToken);
    };

    private Function<Long, NoContentFoundException> APPOINTMENT_MODE_HOSPITAL_INFO = (companyId) -> {
        throw new NoContentFoundException(AppointmentModeHospitalInfo.class);
    };

    private Gender fetchGender(Character genderCode) {
        return fetchGenderByCode(genderCode);
    }

    private Profile fetchProfile(Long profileId) {
        return profileService.fetchActiveProfileById(profileId);
    }
}
