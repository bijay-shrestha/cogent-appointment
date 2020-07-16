package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.admin.*;
import com.cogent.cogentappointment.client.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminDetailResponseDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminLoggedInInfoResponseDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminMetaInfoResponseDTO;
import com.cogent.cogentappointment.client.dto.response.admin.AdminMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentServiceType.AppointmentServiceTypeDropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.ApiInfoResponseDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.ClientIntegrationResponseDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.FeatureIntegrationResponse;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.FeatureIntegrationResponseDTO;
import com.cogent.cogentappointment.client.dto.response.integration.IntegrationBodyAttributeResponse;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminModeFeatureIntegrationResponseDTO;
import com.cogent.cogentappointment.client.exception.BadRequestException;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.exception.OperationUnsuccessfulException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.AdminFeatureService;
import com.cogent.cogentappointment.client.service.AdminService;
import com.cogent.cogentappointment.client.service.EmailService;
import com.cogent.cogentappointment.client.service.ProfileService;
import com.cogent.cogentappointment.client.validator.LoginValidator;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import javax.validation.Validator;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.AdminServiceMessages.*;
import static com.cogent.cogentappointment.client.constants.IntegrationApiConstants.*;
import static com.cogent.cogentappointment.client.constants.StatusConstants.*;
import static com.cogent.cogentappointment.client.exception.utils.ValidationUtils.validateConstraintViolation;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AdminLog.*;
import static com.cogent.cogentappointment.client.utils.AdminUtils.*;
import static com.cogent.cogentappointment.client.utils.DashboardFeatureUtils.parseToAdminDashboardFeature;
import static com.cogent.cogentappointment.client.utils.DashboardFeatureUtils.parseToUpdateAdminDashboardFeature;
import static com.cogent.cogentappointment.client.utils.GenderUtils.fetchGenderByCode;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;
import static java.lang.reflect.Array.get;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;

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

    private final AdminConfirmationTokenRepository confirmationTokenRepository;

    private final DashboardFeatureRepository dashboardFeatureRepository;

    private final AdminDashboardFeatureRepository adminDashboardFeatureRepository;

    private final EmailService emailService;

    private final ProfileService profileService;

    private final AdminFeatureService adminFeatureService;

    private final IntegrationRepository integrationRepository;

    private final IntegrationRequestBodyParametersRepository requestBodyParametersRepository;

    private final AdminFavouriteRepository adminFavouriteRepository;

    private final AppointmentServiceTypeRepository appointmentServiceTypeRepository;

    private final AdminModeApiFeatureIntegrationRepository adminModeApiFeatureIntegrationRepository;

    private final AdminModeFeatureIntegrationRepository adminModeFeatureIntegrationRepository;

    private final HospitalRepository hospitalRepository;

    public AdminServiceImpl(Validator validator,
                            AdminRepository adminRepository,
                            AdminMacAddressInfoRepository adminMacAddressInfoRepository,
                            AdminMetaInfoRepository adminMetaInfoRepository,
                            AdminAvatarRepository adminAvatarRepository,
                            AdminConfirmationTokenRepository confirmationTokenRepository,
                            DashboardFeatureRepository dashboardFeatureRepository,
                            AdminDashboardFeatureRepository adminDashboardFeatureRepository,
                            EmailService emailService,
                            ProfileService profileService,
                            AdminFeatureService adminFeatureService,
                            IntegrationRepository integrationRepository,
                            IntegrationRequestBodyParametersRepository requestBodyParametersRepository,
                            AdminFavouriteRepository adminFavouriteRepository, HospitalRepository hospitalRepository,
                            AppointmentServiceTypeRepository appointmentServiceTypeRepository,
                            AdminModeApiFeatureIntegrationRepository adminModeApiFeatureIntegrationRepository,
                            AdminModeFeatureIntegrationRepository adminModeFeatureIntegrationRepository) {
        this.validator = validator;
        this.adminRepository = adminRepository;
        this.adminMacAddressInfoRepository = adminMacAddressInfoRepository;
        this.adminMetaInfoRepository = adminMetaInfoRepository;
        this.adminAvatarRepository = adminAvatarRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.dashboardFeatureRepository = dashboardFeatureRepository;
        this.adminDashboardFeatureRepository = adminDashboardFeatureRepository;
        this.emailService = emailService;
        this.profileService = profileService;
        this.adminFeatureService = adminFeatureService;
        this.integrationRepository = integrationRepository;
        this.requestBodyParametersRepository = requestBodyParametersRepository;
        this.adminFavouriteRepository = adminFavouriteRepository;
        this.appointmentServiceTypeRepository = appointmentServiceTypeRepository;
        this.adminModeApiFeatureIntegrationRepository = adminModeApiFeatureIntegrationRepository;
        this.adminModeFeatureIntegrationRepository = adminModeFeatureIntegrationRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public void save(AdminRequestDTO adminRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, ADMIN);

        Long hospitalId = getLoggedInHospitalId();

        validateConstraintViolation(validator.validate(adminRequestDTO));

        validateAdminCount(hospitalId);

        List<Object[]> admins = adminRepository.validateDuplicity(
                adminRequestDTO.getEmail(),
                adminRequestDTO.getMobileNumber()
        );

        validateAdminDuplicity(admins,
                adminRequestDTO.getEmail(),
                adminRequestDTO.getMobileNumber(),
                hospitalId
        );

        Admin admin = saveAdmin(adminRequestDTO, hospitalId);

        saveAdminAvatar(admin, adminRequestDTO.getAvatar());

        saveMacAddressInfo(admin, adminRequestDTO.getMacAddressInfo());

        saveAdminMetaInfo(admin);

        saveAdminFeature(admin);

        saveAllAdminDashboardFeature(adminRequestDTO.getAdminDashboardRequestDTOS(), admin);

        AdminConfirmationToken adminConfirmationToken =
                saveAdminConfirmationToken(parseInAdminConfirmationToken(admin));

        EmailRequestDTO emailRequestDTO = convertAdminRequestToEmailRequestDTO(adminRequestDTO,
                adminConfirmationToken.getConfirmationToken());

        saveEmailToSend(emailRequestDTO);

        log.info(SAVING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinAdmin() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, ADMIN);

        List<DropDownResponseDTO> activeMinAdmin = adminRepository.fetchActiveMinAdmin(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return activeMinAdmin;
    }

    @Override
    public List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, ADMIN);

        List<AdminMinimalResponseDTO> responseDTOS =
                adminRepository.search(searchRequestDTO, getLoggedInHospitalId(), pageable);

        log.info(SEARCHING_PROCESS_STARTED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public AdminDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, ADMIN);

        AdminDetailResponseDTO responseDTO = adminRepository.fetchDetailsById(id, getLoggedInHospitalId());

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, ADMIN);

        Admin admin = findAdminByIdAndHospitalId(deleteRequestDTO.getId(), getLoggedInHospitalId());

        if (admin.getProfileId().getIsSuperAdminProfile().equals(YES))
            throw new BadRequestException(INVALID_DELETE_REQUEST);

        deleteAdminMetaInfo(admin, deleteRequestDTO);

        saveAdmin(convertAdminToDeleted(admin, deleteRequestDTO));

        log.info(DELETING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void changePassword(AdminChangePasswordRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PASSWORD_PROCESS_STARTED);

        Admin admin = findAdminByIdAndHospitalId(requestDTO.getId(), getLoggedInHospitalId());

        validatePassword(admin, requestDTO);

        updateAdminPassword(requestDTO.getNewPassword(), requestDTO.getRemarks(), admin);

        log.info(UPDATING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void resetPassword(AdminResetPasswordRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PASSWORD_PROCESS_STARTED);

        Admin admin = findAdminByIdAndHospitalId(requestDTO.getId(), getLoggedInHospitalId());

        saveAdmin(updateAdminPassword(requestDTO.getPassword(), requestDTO.getRemarks(), admin));

        sendEmail(parseToResetPasswordEmailRequestDTO(requestDTO, admin.getEmail(), admin.getFullName()));

        log.info(UPDATING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void updateAvatar(AdminAvatarUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, ADMIN_AVATAR);

        Admin admin = findAdminByIdAndHospitalId(requestDTO.getAdminId(), getLoggedInHospitalId());

        updateAvatar(admin, requestDTO.getAvatar());

        log.info(UPDATING_PROCESS_STARTED, ADMIN_AVATAR, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(AdminUpdateRequestDTO updateRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, ADMIN);

        Long hospitalId = getLoggedInHospitalId();

        Admin admin = findAdminByIdAndHospitalId(updateRequestDTO.getId(), hospitalId);

        if (Objects.isNull(admin.getPassword()))
            throw new BadRequestException(BAD_UPDATE_MESSAGE, BAD_UPDATE_DEBUG_MESSAGE);

        List<Object[]> admins = adminRepository.validateDuplicity(updateRequestDTO);

        validateAdminDuplicity(admins,
                updateRequestDTO.getEmail(),
                updateRequestDTO.getMobileNumber(),
                hospitalId
        );

        emailIsNotUpdated(updateRequestDTO, admin, hospitalId);

        emailIsUpdated(updateRequestDTO, admin, hospitalId);

        log.info(UPDATING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));
    }

    private void emailIsNotUpdated(AdminUpdateRequestDTO updateRequestDTO,
                                   Admin admin, Long hospitalId) {

        if (updateRequestDTO.getEmail().equals(admin.getEmail())) {
            EmailRequestDTO emailRequestDTO = parseUpdatedInfo(updateRequestDTO, admin);

            update(updateRequestDTO, updateRequestDTO.getStatus(), admin, hospitalId);

            if (updateRequestDTO.getIsAvatarUpdate().equals(YES))
                updateAvatar(admin, updateRequestDTO.getAvatar());

            updateMacAddressInfo(updateRequestDTO.getMacAddressUpdateInfo(), admin);

            updateAdminDashboardFeature(updateRequestDTO.getAdminDashboardRequestDTOS(), admin);

            updateAdminMetaInfo(admin);

            saveEmailToSend(emailRequestDTO);
        }
    }

    private void emailIsUpdated(AdminUpdateRequestDTO updateRequestDTO,
                                Admin admin,
                                Long hospitalId) {

        if (!updateRequestDTO.getEmail().equals(admin.getEmail())) {

            EmailRequestDTO emailRequestDTO = parseUpdatedInfo(updateRequestDTO, admin);

            AdminConfirmationToken adminConfirmationToken =
                    saveAdminConfirmationToken(parseInAdminConfirmationToken(admin));

            EmailRequestDTO emailRequestDTOForNewEmail = convertAdminUpdateRequestToEmailRequestDTO(updateRequestDTO,
                    adminConfirmationToken.getConfirmationToken());

            update(updateRequestDTO, INACTIVE, admin, hospitalId);

            if (updateRequestDTO.getIsAvatarUpdate().equals(YES))
                updateAvatar(admin, updateRequestDTO.getAvatar());

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

        Admin admin = adminRepository.findAdminById(adminConfirmationToken.getAdmin().getId())
                .orElseThrow(() -> ADMIN_WITH_GIVEN_ID_NOT_FOUND.apply(adminConfirmationToken.getAdmin().getId()));

        admin.setStatus(ACTIVE);

        saveAdmin(admin);

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

        saveAdminPassword(requestDTO, adminConfirmationToken.getAdmin());

        adminConfirmationToken.setStatus(INACTIVE);

        log.info(SAVING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public AdminLoggedInInfoResponseDTO fetchLoggedInAdminInfo(AdminInfoRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN);

        Long hospitalId = getLoggedInHospitalId();

        AdminLoggedInInfoResponseDTO responseDTO =
                adminRepository.fetchLoggedInAdminInfo(requestDTO, hospitalId);

        List<IntegrationBodyAttributeResponse> responses =
                requestBodyParametersRepository.fetchRequestBodyAttributes();

        Map<String, String> map = new HashMap<>();

        if (responses != null) {
            responses.forEach(response -> {
                map.put(response.getName(), "");
            });
        }

        List<Long> favouriteUserMenuId = adminFavouriteRepository.
                findUserMenuIdByAdmin(getLoggedInHospitalId()).orElse(emptyList());

        responseDTO.setFavouriteUserMenuId(favouriteUserMenuId);
        responseDTO.setApiIntegration(getApiIntegrations());
        responseDTO.setRequestBody(map);

        responseDTO.setHospitalAppointmentServiceType(fetchAppointmentServiceType(hospitalId));

        log.info(FETCHING_PROCESS_COMPLETED, ADMIN, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    private Map<String, List<?>> getApiIntegrations() {

        List<AdminModeFeatureIntegrationResponseDTO> featureIntegrationResponseDTO =
                getAdminModeApiIntegration(getLoggedInHospitalId());

        List<ClientIntegrationResponseDTO> clientIntegrationResponseDTO =
                getHospitalApiIntegration(getLoggedInHospitalId());

        Map<String, List<?>> map = new HashMap();

        if (featureIntegrationResponseDTO != null) {
            map.put(KEY_APPOINTMENT_MODE_INTEGRATION, featureIntegrationResponseDTO);
        }

        if (clientIntegrationResponseDTO != null) {
            map.put(KEY_CLIENT_INTEGRATION, clientIntegrationResponseDTO);
        }

        return map;
    }

    private List<AdminModeFeatureIntegrationResponseDTO> getAdminModeApiIntegration(Long hospitalId) {

        List<AdminFeatureIntegrationResponse> integrationResponse = adminModeFeatureIntegrationRepository.
                fetchAdminModeIntegrationResponseDTO(hospitalId);


        Map<Long, List<AdminFeatureIntegrationResponse>> integrationResponseMap = integrationResponse.stream()
                .collect(groupingBy(AdminFeatureIntegrationResponse::getAppointmentModeId));

        List<AdminModeFeatureIntegrationResponseDTO> featureIntegrationResponseDTOS = new ArrayList<>();


        integrationResponseMap.entrySet().stream().forEach(responseMap -> {

            List<FeatureIntegrationResponseDTO> features = new ArrayList<>();


            responseMap.getValue().forEach(response -> {

                Map<String, String> requestHeaderResponseDTO = integrationRepository.
                        findAdminModeApiRequestHeaders(response.getApiIntegrationFormatId());

                Map<String, String> queryParametersResponseDTO = integrationRepository.
                        findAdminModeApiQueryParameters(response.getApiIntegrationFormatId());

                Object[] requestBody = getRequestBodyByFeature(response.getFeatureId(),
                        response.getRequestMethod());


                FeatureIntegrationResponseDTO featureIntegrationResponseDTO = new FeatureIntegrationResponseDTO();
                if (response.getIntegrationChannelCode().equalsIgnoreCase(FRONT_END_CODE)) {
                    ApiInfoResponseDTO apiInfoResponseDTO = new ApiInfoResponseDTO();

                    apiInfoResponseDTO.setUrl(response.getUrl());
                    apiInfoResponseDTO.setRequestMethod(response.getRequestMethod());
                    apiInfoResponseDTO.setRequestBody(requestBody);
                    apiInfoResponseDTO.setHeaders(requestHeaderResponseDTO);
                    apiInfoResponseDTO.setQueryParameters(queryParametersResponseDTO);

                    featureIntegrationResponseDTO.setApiInfo(apiInfoResponseDTO);
                }

                featureIntegrationResponseDTO.setFeatureCode(response.getFeatureCode());
                featureIntegrationResponseDTO.setIntegrationChannelCode(response.getIntegrationChannelCode());


                features.add(featureIntegrationResponseDTO);

            });

            AdminModeFeatureIntegrationResponseDTO adminModeFeatureIntegrationResponseDTO = new AdminModeFeatureIntegrationResponseDTO();
            adminModeFeatureIntegrationResponseDTO.setAppointmentModeId(responseMap.getKey());
            adminModeFeatureIntegrationResponseDTO.setFeatures(features);

            featureIntegrationResponseDTOS.add(adminModeFeatureIntegrationResponseDTO);

        });


        return featureIntegrationResponseDTOS;

    }

    private List<ClientIntegrationResponseDTO> getHospitalApiIntegration(Long hospitalId) {

        List<FeatureIntegrationResponse> integrationResponseDTOList = integrationRepository.
                fetchClientIntegrationResponseDTO(hospitalId);

        List<ClientIntegrationResponseDTO> clientIntegrationResponseDTOS = new ArrayList<>();
        List<FeatureIntegrationResponseDTO> features = new ArrayList<>();

        integrationResponseDTOList.forEach(response -> {

            Map<String, String> requestHeaderResponseDTO = integrationRepository.findApiRequestHeaders(response.getApiIntegrationFormatId());

            Map<String, String> queryParametersResponseDTO = integrationRepository.findApiQueryParameters(response.getApiIntegrationFormatId());

            Object[] requestBody = getRequestBodyByFeature(response.getFeatureId(), response.getRequestMethod());

            FeatureIntegrationResponseDTO featureIntegrationResponseDTO = new FeatureIntegrationResponseDTO();
            featureIntegrationResponseDTO.setFeatureCode(response.getFeatureCode());
            featureIntegrationResponseDTO.setIntegrationChannelCode(response.getIntegrationChannelCode());

            if (response.getIntegrationChannelCode().equalsIgnoreCase(FRONT_END_CODE)) {
                ApiInfoResponseDTO apiInfoResponseDTO = new ApiInfoResponseDTO();

                apiInfoResponseDTO.setUrl(response.getUrl());
                apiInfoResponseDTO.setRequestMethod(response.getRequestMethod());
                apiInfoResponseDTO.setRequestBody(requestBody);
                apiInfoResponseDTO.setHeaders(requestHeaderResponseDTO);
                apiInfoResponseDTO.setQueryParameters(queryParametersResponseDTO);

                featureIntegrationResponseDTO.setApiInfo(apiInfoResponseDTO);
            }


            features.add(featureIntegrationResponseDTO);

        });

        ClientIntegrationResponseDTO clientIntegrationResponseDTO = new ClientIntegrationResponseDTO();
        clientIntegrationResponseDTO.setFeatures(features);
        clientIntegrationResponseDTO.setClientId(hospitalId);

        clientIntegrationResponseDTOS.add(clientIntegrationResponseDTO);

        return clientIntegrationResponseDTOS;

    }


    private Object[] getRequestBodyByFeature(Long featureId, String requestMethod) {

        Object[] requestBody = new Object[0];
        if (requestMethod.equalsIgnoreCase("POST")) {
            List<IntegrationBodyAttributeResponse> responses = requestBodyParametersRepository.
                    fetchRequestBodyAttributeByFeatureId(featureId);

            if (responses != null) {
                requestBody = responses.stream()
                        .map(request -> request.getName())
                        .collect(Collectors.toList()).toArray();
            }

        }

        return requestBody;
    }

    @Override
    public List<AdminMetaInfoResponseDTO> fetchAdminMetaInfo() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN_META_INFO);

        List<AdminMetaInfoResponseDTO> metaInfoResponseDTOS =
                adminMetaInfoRepository.fetchAdminMetaInfo(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_COMPLETED, ADMIN_META_INFO, getDifferenceBetweenTwoTime(startTime));

        return metaInfoResponseDTOS;
    }

    private void updateAdminDashboardFeature(List<AdminDashboardRequestDTO> adminDashboardRequestDTOS,
                                             Admin admin) {

        List<AdminDashboardFeature> adminDashboardFeatureList = new ArrayList<>();
        adminDashboardRequestDTOS.forEach(result -> {

            AdminDashboardFeature adminDashboardFeature =
                    adminDashboardFeatureRepository.findAdminDashboardFeatureBydashboardFeatureId(
                            result.getId(), admin.getId());

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


    private void saveAllAdminDashboardFeature(List<AdminDashboardRequestDTO> dashboardRequestDTOList, Admin admin) {

        if (dashboardRequestDTOList.size() > 0) {
            List<DashboardFeature> dashboardFeatureList = findActiveDashboardFeatures(dashboardRequestDTOList);
            adminDashboardFeatureRepository.saveAll(parseToAdminDashboardFeature(dashboardFeatureList, admin));
        }
    }

    /*FETCH ALL ACTIVE DASHBOARD FEATURE BASED ON THE IDS IN REQUEST
     AND IF RESULT SIZE IS NOT EQUAL TO REQUEST SIZE THEN ANY OF THE REQUESTED ID IS INVALID AND THROW EXCEPTION*/
    private List<DashboardFeature> findActiveDashboardFeatures(List<AdminDashboardRequestDTO> adminDashboardRequestDTOS) {

        String ids = adminDashboardRequestDTOS.stream()
                .map(request -> request.getId().toString())
                .collect(Collectors.joining(","));

        List<DashboardFeature> dashboardFeatureList = validateDashboardFeature(ids);
        int requestCount = adminDashboardRequestDTOS.size();

        if ((dashboardFeatureList.size()) != requestCount)
            throw new NoContentFoundException(DashboardFeature.class);

        return dashboardFeatureList;
    }

    private List<DashboardFeature> validateDashboardFeature(String ids) {
        return dashboardFeatureRepository.validateDashboardFeatureCount(ids);
    }

    private void validateStatus(Object status) {
        if (status.equals(INACTIVE)) {
            log.error(ADMIN_REGISTERED);
            throw ADMIN_ALREADY_REGISTERED.get();
        }
    }

    /*CAN SAVE NUMBER OF ADMINS BASED ON HOSPITAL*/
    private void validateAdminCount(Long hospitalId) {

        Object[] objects = adminRepository.validateAdminCount(hospitalId);
        Long savedAdmin = (Long) get(objects, 0);
        Integer numberOfAdminsAllowed = (Integer) get(objects, 1);

        if (savedAdmin.intValue() == numberOfAdminsAllowed) {
            log.error(ADMIN_CANNOT_BE_REGISTERED_DEBUG_MESSAGE);
            throw new BadRequestException(ADMIN_CANNOT_BE_REGISTERED_MESSAGE, ADMIN_CANNOT_BE_REGISTERED_DEBUG_MESSAGE);
        }
    }

    private Admin saveAdmin(AdminRequestDTO adminRequestDTO, Long hospitalId) {
        Gender gender = fetchGender(adminRequestDTO.getGenderCode());

        Profile profile = fetchProfile(adminRequestDTO.getProfileId(), hospitalId);

        Admin admin = parseAdminDetails(adminRequestDTO, gender, profile);

        return saveAdmin(admin);
    }

    private void saveAdminAvatar(Admin admin, String avatar) {
        if (!Objects.isNull(avatar))
            saveAdminAvatar(convertFileToAdminAvatar(new AdminAvatar(), avatar, admin));
    }

    private void updateAdminAvatar(Admin admin,
                                   AdminAvatar adminAvatar,
                                   String avatar) {

        if (!Objects.isNull(avatar)) {
            convertFileToAdminAvatar(adminAvatar, avatar, admin);
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

    private Admin saveAdmin(Admin admin) {
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

    private Admin findAdminByIdAndHospitalId(Long adminId, Long hospitalId) {
        return adminRepository.findAdminByIdAndHospitalId(adminId, hospitalId)
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

    public void update(AdminUpdateRequestDTO adminRequestDTO, Character status, Admin admin, Long hospitalId) {
        Gender gender = fetchGender(adminRequestDTO.getGenderCode());

        Profile profile = fetchProfile(adminRequestDTO.getProfileId(), hospitalId);

        convertAdminUpdateRequestDTOToAdmin(admin, status, adminRequestDTO, gender, profile);
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

    private void validatePassword(Admin admin, AdminChangePasswordRequestDTO requestDTO) {

        if (!LoginValidator.checkPassword(requestDTO.getOldPassword(), admin.getPassword())) {
            log.error(PASSWORD_MISMATCH_MESSAGE);
            throw new OperationUnsuccessfulException(PASSWORD_MISMATCH_MESSAGE);
        }

        if (LoginValidator.checkPassword(requestDTO.getNewPassword(), admin.getPassword())) {
            log.error(DUPLICATE_PASSWORD_MESSAGE);
            throw new DataDuplicationException(DUPLICATE_PASSWORD_MESSAGE);
        }
    }

    private Supplier<DataDuplicationException> ADMIN_DUPLICATION = () ->
            new DataDuplicationException(ADMIN_DUPLICATION_MESSAGE);

    private Function<Long, NoContentFoundException> ADMIN_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, ADMIN, id);
        throw new NoContentFoundException(Admin.class, "id", id.toString());
    };

    private Supplier<BadRequestException> ADMIN_ALREADY_REGISTERED = () -> new BadRequestException(ADMIN_REGISTERED);

    private Function<String, NoContentFoundException> CONFIRMATION_TOKEN_NOT_FOUND = (confirmationToken) -> {
        log.error(INVALID_CONFIRMATION_TOKEN_ERROR, confirmationToken);
        throw new NoContentFoundException(INVALID_CONFIRMATION_TOKEN, "confirmationToken", confirmationToken);
    };

    private Gender fetchGender(Character genderCode) {
        return fetchGenderByCode(genderCode);
    }

    private Profile fetchProfile(Long profileId, Long hospitalId) {
        return profileService.findActiveProfileByIdAndHospitalId(profileId, hospitalId);
    }

    private List<AppointmentServiceTypeDropDownResponseDTO> fetchAppointmentServiceType(Long hospitalId) {
        return hospitalRepository.fetchAssignedAppointmentServiceType(hospitalId);
    }
}
