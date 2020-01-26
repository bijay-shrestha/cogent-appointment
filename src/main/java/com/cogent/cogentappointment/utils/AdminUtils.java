package com.cogent.cogentappointment.utils;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.request.admin.*;
import com.cogent.cogentappointment.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.dto.response.admin.AdminDetailResponseDTO;
import com.cogent.cogentappointment.dto.response.admin.AdminInfoByUsernameResponseDTO;
import com.cogent.cogentappointment.dto.response.admin.AdminProfileResponseDTO;
import com.cogent.cogentappointment.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.model.Admin;
import com.cogent.cogentappointment.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.cogent.cogentappointment.constants.EmailConstants.*;
import static com.cogent.cogentappointment.constants.EmailTemplates.*;
import static com.cogent.cogentappointment.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.constants.StringConstant.*;
import static com.cogent.cogentappointment.utils.commons.NumberFormatterUtils.generateRandomToken;
import static com.cogent.cogentappointment.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti on 2019-08-11
 */
public class AdminUtils {

    public static Admin convertAdminRequestDTOToAdmin(AdminRequestDTO adminRequestDTO,
                                                      Hospital hospital) {
        Admin admin = new Admin();
        admin.setUsername(adminRequestDTO.getUsername());
        admin.setFullName(toUpperCase(adminRequestDTO.getFullName()));
        admin.setEmail(adminRequestDTO.getEmail());
        admin.setMobileNumber(adminRequestDTO.getMobileNumber());
        admin.setStatus(adminRequestDTO.getStatus());
        admin.setHasMacBinding(adminRequestDTO.getHasMacBinding());
        admin.setIsFirstLogin(YES);
        return admin;
    }

    public static void convertAdminUpdateRequestDTOToAdmin(Admin admin,
                                                           AdminUpdateRequestDTO adminRequestDTO,
                                                           Hospital hospital) {

        admin.setEmail(adminRequestDTO.getEmail());
        admin.setFullName(toUpperCase(adminRequestDTO.getFullName()));
        admin.setMobileNumber(adminRequestDTO.getMobileNumber());
        admin.setStatus(adminRequestDTO.getStatus());
        admin.setHasMacBinding(adminRequestDTO.getHasMacBinding());
        admin.setRemarks(adminRequestDTO.getRemarks());
        /*MODIFIED DATE AND MODIFIED BY*/
    }

    public static AdminProfile parseToAdminProfile(Long adminId, AdminProfileRequestDTO requestDTO) {
        AdminProfile adminProfile = new AdminProfile();
        parseToAdminProfile(adminProfile, adminId, requestDTO.getProfileId(), ACTIVE);
        return adminProfile;
    }

    public static AdminProfile parseToUpdatedAdminProfile(Long adminId,
                                                          AdminProfileUpdateRequestDTO updateDTO) {

        AdminProfile adminProfile = new AdminProfile();
        adminProfile.setId(updateDTO.getAdminProfileId());
        parseToAdminProfile(adminProfile, adminId, updateDTO.getProfileId(), updateDTO.getStatus());
        return adminProfile;
    }

    private static void parseToAdminProfile(AdminProfile adminProfile,
                                            Long adminId,
                                            Long profileId,
                                            Character status) {
        adminProfile.setAdminId(adminId);
        adminProfile.setProfileId(profileId);
        adminProfile.setStatus(status);
    }

    public static AdminMacAddressInfo convertToMACAddressInfo(String macAddress, Admin admin) {

        AdminMacAddressInfo macAddressInfo = new AdminMacAddressInfo();
        macAddressInfo.setMacAddress(macAddress);
        macAddressInfo.setStatus(ACTIVE);
        macAddressInfo.setAdmin(admin);
        return macAddressInfo;
    }

    public static AdminMetaInfo parseInAdminMetaInfo(Admin admin) {
        AdminMetaInfo adminMetaInfo = new AdminMetaInfo();
        adminMetaInfo.setAdmin(admin);
        parseMetaInfo(admin, adminMetaInfo);
        return adminMetaInfo;
    }

    public static void parseMetaInfo(Admin admin,
                                     AdminMetaInfo adminMetaInfo) {
        adminMetaInfo.setMetaInfo(admin.getFullName() + OR + admin.getUsername() + OR + admin.getMobileNumber());
    }

    public static AdminConfirmationToken parseInAdminConfirmationToken(Admin admin) {
        AdminConfirmationToken confirmationToken = new AdminConfirmationToken();
        confirmationToken.setAdmin(admin);
        confirmationToken.setStatus(ACTIVE);
        confirmationToken.setConfirmationToken(generateRandomToken());

        return confirmationToken;
    }

    public static EmailRequestDTO convertAdminRequestToEmailRequestDTO(AdminRequestDTO adminRequestDTO,
                                                                       String confirmationToken,
                                                                       HttpServletRequest httpServletRequest) {

        String origin = httpServletRequest.getHeader("origin");
        String confirmationUrl = origin + "/#" + "/savePassword" + "?token =" + confirmationToken;

        return EmailRequestDTO.builder()
                .receiverEmailAddress(adminRequestDTO.getEmail())
                .subject(SUBJECT_FOR_ADMIN_VERIFICATION)
                .templateName(ADMIN_VERIFICATION)
                .paramValue(adminRequestDTO.getUsername() + COMMA_SEPARATED + confirmationUrl)
                .build();
    }

    public static AdminAvatar convertFileToAdminAvatar(FileUploadResponseDTO fileUploadResponseDTO,
                                                       Admin admin) {
        AdminAvatar adminAvatar = new AdminAvatar();
        setFileProperties(fileUploadResponseDTO, adminAvatar);
        adminAvatar.setAdmin(admin);
        return adminAvatar;
    }

    public static void setFileProperties(FileUploadResponseDTO fileUploadResponseDTO,
                                         AdminAvatar adminAvatar) {
        adminAvatar.setFileSize(fileUploadResponseDTO.getFileSize());
        adminAvatar.setFileUri(fileUploadResponseDTO.getFileUri());
        adminAvatar.setFileType(fileUploadResponseDTO.getFileType());
        adminAvatar.setStatus(ACTIVE);
    }

    public static void convertAdminToDeleted(Admin admin, DeleteRequestDTO deleteRequestDTO) {
        admin.setStatus(deleteRequestDTO.getStatus());
        admin.setRemarks(deleteRequestDTO.getRemarks());
    }

    public static void updateAdminPassword(String password, String remarks, Admin admin) {
        admin.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        admin.setRemarks(remarks);
    }

    private static BiFunction<MacAddressInfoUpdateRequestDTO, Admin, AdminMacAddressInfo> convertToUpdatedMACAddressInfo =
            (requestDTO, admin) -> {

                AdminMacAddressInfo macAddressInfo = new AdminMacAddressInfo();
                macAddressInfo.setId(requestDTO.getId());
                macAddressInfo.setMacAddress(requestDTO.getMacAddress());
                macAddressInfo.setStatus(requestDTO.getStatus());
                macAddressInfo.setAdmin(admin);

                return macAddressInfo;
            };

    public static List<AdminMacAddressInfo> convertToUpdatedMACAddressInfo(
            List<MacAddressInfoUpdateRequestDTO> macAddressInfoRequestDTOS, Admin admin) {

        return macAddressInfoRequestDTOS.stream()
                .map(macAddressInfo -> convertToUpdatedMACAddressInfo.apply(macAddressInfo, admin))
                .collect(Collectors.toList());
    }

    public static String parseUpdatedValues(Admin admin,
                                            AdminUpdateRequestDTO updateRequestDTO) {
        Map<String, String> params = new HashMap<>();

        if (!(updateRequestDTO.getFullName().equalsIgnoreCase(admin.getFullName())))
            params.put("Full Name ", updateRequestDTO.getFullName());

        if (!(updateRequestDTO.getEmail().equalsIgnoreCase(admin.getEmail())))
            params.put("Email Address ", updateRequestDTO.getEmail());

        if (!(updateRequestDTO.getMobileNumber().equalsIgnoreCase(admin.getMobileNumber())))
            params.put("Mobile Number ", updateRequestDTO.getMobileNumber());

//        if (!(updateRequestDTO.getProfileId().equals(admin.getProfile().getId())))
//            params.put("Profile ", admin.getProfile().getName());

        if (!(updateRequestDTO.getStatus().equals(admin.getStatus()))) {
            String status = "";
            switch (admin.getStatus()) {
                case 'Y':
                    status = "Active";
                    break;
                case 'N':
                    status = "Inactive";
                    break;
                case 'D':
                    status = "Deleted";
                    break;
            }

            params.put("Status ", status);
        }

        params.put("Remarks ", updateRequestDTO.getRemarks());

        return params.toString();
    }

    public static String parseUpdatedMacAddress(AdminUpdateRequestDTO updateRequestDTO) {

        List<String> macAddress = updateRequestDTO.getMacAddressInfoUpdateRequestDTOS()
                .stream()
                .filter(requestDTO -> requestDTO.getStatus().equals(ACTIVE))
                .map(MacAddressInfoUpdateRequestDTO::getMacAddress)
                .collect(Collectors.toList());

        return (updateRequestDTO.getHasMacBinding().equals(ACTIVE))
                ? StringUtils.join(macAddress, COMMA_SEPARATED) : "N/A";
    }

    public static EmailRequestDTO parseToEmailRequestDTO(String username,
                                                         AdminUpdateRequestDTO updateRequestDTO,
                                                         String paramValues,
                                                         String updatedMacAddress) {
        return EmailRequestDTO.builder()
                .receiverEmailAddress(updateRequestDTO.getEmail())
                .subject(SUBJECT_FOR_UPDATE_ADMIN)
                .templateName(UPDATE_ADMIN)
                .paramValue(username + HYPHEN + paramValues + HYPHEN +
                        updateRequestDTO.getHasMacBinding() + HYPHEN + updatedMacAddress)
                .build();
    }

    public static Admin saveAdminPassword(AdminPasswordRequestDTO requestDTO,
                                          AdminConfirmationToken confirmationToken) {
        Admin admin = confirmationToken.getAdmin();
        admin.setPassword(BCrypt.hashpw(requestDTO.getPassword(), BCrypt.gensalt()));
        return admin;
    }

    public static Function<Object[], AdminDetailResponseDTO> parseToAdminDetailResponseDTO = (objects) -> {

        final int ADMIN_ID_INDEX = 0;
        final int FULL_NAME_INDEX = 1;
        final int USERNAME_INDEX = 2;
        final int EMAIL_INDEX = 3;
        final int MOBILE_NUMBER_INDEX = 4;
        final int STATUS_INDEX = 5;
        final int HAS_MAC_BINDING_INDEX = 6;
        final int ADMIN_CATEGORY_NAME_INDEX = 7;
        final int ADMIN_CATEGORY_ID_INDEX = 8;
        final int REMARKS_INDEX = 9;
        final int HOSPITAL_NAME_INDEX = 10;
        final int HOSPITAL_ID_INDEX = 11;
        final int ADMIN_AVATAR_FILE_URI_INDEX = 12;

        return AdminDetailResponseDTO.builder()
                .id(Long.parseLong(objects[ADMIN_ID_INDEX].toString()))
                .fullName(objects[FULL_NAME_INDEX].toString())
                .username(objects[USERNAME_INDEX].toString())
                .email(objects[EMAIL_INDEX].toString())
                .mobileNumber(objects[MOBILE_NUMBER_INDEX].toString())
                .status(objects[STATUS_INDEX].toString().charAt(0))
                .hasMacBinding(objects[HAS_MAC_BINDING_INDEX].toString().charAt(0))
                .adminCategoryName(objects[ADMIN_CATEGORY_NAME_INDEX].toString())
                .adminCategoryId(Long.parseLong(objects[ADMIN_CATEGORY_ID_INDEX].toString()))
                .hospitalName(objects[HOSPITAL_NAME_INDEX].toString())
                .hospitalId(Long.parseLong(objects[HOSPITAL_ID_INDEX].toString()))
                .remarks(Objects.isNull(objects[REMARKS_INDEX]) ? null : objects[REMARKS_INDEX].toString())
                .fileUri(Objects.isNull(objects[ADMIN_AVATAR_FILE_URI_INDEX]) ? null :
                        objects[ADMIN_AVATAR_FILE_URI_INDEX].toString())
                .adminProfileResponseDTOS(getAdminProfileResponseDTOS(objects))
                .build();
    };

    private static List<AdminProfileResponseDTO> getAdminProfileResponseDTOS(Object[] object) {

        final int ADMIN_PROFILE_ID_INDEX = 13;
        final int PROFILE_ID_INDEX = 14;
        final int PROFILE_NAME_INDEX = 15;
        final int APPLICATION_MODULE_ID_INDEX = 16;
        final int APPLICATION_MODULE_NAME_INDEX = 17;

        String[] adminProfileIds = object[ADMIN_PROFILE_ID_INDEX].toString().split(COMMA_SEPARATED);
        String[] profileIds = object[PROFILE_ID_INDEX].toString().split(COMMA_SEPARATED);
        String[] profileNames = object[PROFILE_NAME_INDEX].toString().split(COMMA_SEPARATED);
        String[] applicationModuleIds = object[APPLICATION_MODULE_ID_INDEX].toString().split(COMMA_SEPARATED);
        String[] applicationModuleNames = object[APPLICATION_MODULE_NAME_INDEX].toString().split(COMMA_SEPARATED);

        return IntStream.range(0, adminProfileIds.length)
                .mapToObj(i -> AdminProfileResponseDTO.builder()
                        .adminProfileId(Long.parseLong(adminProfileIds[i]))
                        .profileId(Long.parseLong(profileIds[i]))
                        .profileName(profileNames[i])
                        .applicationModuleId(Long.parseLong(applicationModuleIds[i]))
                        .applicationModuleName(applicationModuleNames[i])
                        .build())
                .collect(Collectors.toList());
    }

    public static AdminInfoByUsernameResponseDTO parseToAdminInfoByUsernameResponseDTO(Object[] queryResult) {

        final int ASSIGNED_SUB_DEPARTMENT_CODES_INDEX = 0;
        final int PASSWORD_INDEX = 1;

        List<String> subDepartmentCodes = new ArrayList<>(Arrays.asList(
                queryResult[ASSIGNED_SUB_DEPARTMENT_CODES_INDEX].toString().split(COMMA_SEPARATED)));

        return AdminInfoByUsernameResponseDTO.builder()
                .assignedApplicationModuleCodes(subDepartmentCodes)
                .password(queryResult[PASSWORD_INDEX].toString())
                .build();
    }

    public static EmailRequestDTO parseToResetPasswordEmailRequestDTO(AdminResetPasswordRequestDTO requestDTO,
                                                                      String emailAddress) {

        return EmailRequestDTO.builder()
                .receiverEmailAddress(emailAddress)
                .subject(SUBJECT_FOR_ADMIN_RESET_PASSWORD)
                .templateName(ADMIN_RESET_PASSWORD)
                .paramValue(requestDTO.getUsername() + COMMA_SEPARATED
                        + requestDTO.getPassword() + COMMA_SEPARATED + requestDTO.getRemarks())
                .build();
    }
}
