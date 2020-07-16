package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.admin.*;
import com.cogent.cogentappointment.client.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.client.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.EmailConstants.*;
import static com.cogent.cogentappointment.client.constants.EmailTemplates.*;
import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.client.constants.StringConstant.*;
import static com.cogent.cogentappointment.client.utils.commons.NumberFormatterUtils.generateRandomToken;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;

/**
 * @author smriti on 2019-08-11
 */
public class AdminUtils {

    public static Admin parseAdminDetails(AdminRequestDTO adminRequestDTO,
                                          Gender gender,
                                          Profile profile) {
        Admin admin = new Admin();
        admin.setFullName(toNormalCase(adminRequestDTO.getFullName()));
        admin.setEmail(adminRequestDTO.getEmail());
        admin.setMobileNumber(adminRequestDTO.getMobileNumber());
        admin.setStatus(INACTIVE);
        admin.setHasMacBinding(adminRequestDTO.getHasMacBinding());

        parseAdminDetails(gender, profile, admin);
        return admin;
    }

    private static void parseAdminDetails(Gender gender, Profile profile, Admin admin) {
        admin.setGender(gender);
        admin.setProfileId(profile);
    }

    public static void convertAdminUpdateRequestDTOToAdmin(Admin admin,
                                                           Character status,
                                                           AdminUpdateRequestDTO adminRequestDTO,
                                                           Gender gender,
                                                           Profile profile) {

        admin.setEmail(adminRequestDTO.getEmail());
        admin.setFullName(toNormalCase(adminRequestDTO.getFullName()));
        admin.setMobileNumber(adminRequestDTO.getMobileNumber());
        admin.setStatus(status);
        admin.setHasMacBinding(adminRequestDTO.getHasMacBinding());
        admin.setRemarks(adminRequestDTO.getRemarks());

        parseAdminDetails(gender, profile, admin);
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
        adminMetaInfo.setMetaInfo(admin.getFullName() + OR + admin.getEmail() + OR + admin.getMobileNumber());
        adminMetaInfo.setStatus(admin.getStatus());
        return adminMetaInfo;
    }

    public static AdminMetaInfo parseMetaInfo(Admin admin, AdminMetaInfo adminMetaInfo) {
        adminMetaInfo.setMetaInfo(admin.getFullName() + OR + admin.getEmail() + OR + admin.getMobileNumber());
        adminMetaInfo.setStatus(admin.getStatus());
        adminMetaInfo.setRemarks(admin.getRemarks());

        return adminMetaInfo;
    }

    public static AdminMetaInfo deleteMetaInfo(AdminMetaInfo adminMetaInfo, DeleteRequestDTO requestDTO) {
        adminMetaInfo.setStatus(requestDTO.getStatus());
        adminMetaInfo.setRemarks(requestDTO.getRemarks());

        return adminMetaInfo;
    }

    public static AdminConfirmationToken parseInAdminConfirmationToken(Admin admin) {
        AdminConfirmationToken confirmationToken = new AdminConfirmationToken();
        confirmationToken.setAdmin(admin);
        confirmationToken.setStatus(ACTIVE);
        confirmationToken.setConfirmationToken(generateRandomToken());

        return confirmationToken;
    }

    public static EmailRequestDTO convertAdminRequestToEmailRequestDTO(AdminRequestDTO adminRequestDTO,
                                                                       String confirmationToken) {

//        String origin = httpServletRequest.getHeader("origin");
//        String confirmationUrl = origin + "/#" + "/savePassword" + "?token =" + confirmationToken;

        String confirmationUrl = adminRequestDTO.getBaseUrl() + "/#" + "/savePassword" + "?token =" + confirmationToken;

        return EmailRequestDTO.builder()
                .receiverEmailAddress(adminRequestDTO.getEmail())
                .subject(SUBJECT_FOR_ADMIN_VERIFICATION)
                .templateName(ADMIN_VERIFICATION)
                .paramValue(adminRequestDTO.getFullName() + COMMA_SEPARATED + confirmationUrl)
                .build();
    }

    public static EmailRequestDTO convertAdminUpdateRequestToEmailRequestDTO(AdminUpdateRequestDTO adminRequestDTO,
                                                                             String confirmationToken) {

//        String origin = httpServletRequest.getHeader("origin");
//        String confirmationUrl = origin + "/#" + "/savePassword" + "?token =" + confirmationToken;

        String confirmationUrl = adminRequestDTO.getBaseUrl() + "/#" + "/verify/email" + "?token =" + confirmationToken;

        return EmailRequestDTO.builder()
                .receiverEmailAddress(adminRequestDTO.getEmail())
                .subject(SUBJECT_FOR_EMAIL_VERIFICATION)
                .templateName(EMAIL_VERIFICATION)
                .paramValue(adminRequestDTO.getFullName() + COMMA_SEPARATED + confirmationUrl)
                .build();
    }

    public static AdminAvatar convertFileToAdminAvatar(AdminAvatar adminAvatar,
                                                       String avatar,
                                                       Admin admin) {

        adminAvatar.setAdmin(admin);
        adminAvatar.setFileUri(avatar);
        adminAvatar.setStatus(ACTIVE);
        return adminAvatar;
    }

    public static void setFileProperties(FileUploadResponseDTO fileUploadResponseDTO,
                                         AdminAvatar adminAvatar) {
        adminAvatar.setFileSize(fileUploadResponseDTO.getFileSize());
        adminAvatar.setFileUri(fileUploadResponseDTO.getFileUri());
        adminAvatar.setFileType(fileUploadResponseDTO.getFileType());
        adminAvatar.setStatus(ACTIVE);
    }

    public static Admin convertAdminToDeleted(Admin admin, DeleteRequestDTO deleteRequestDTO) {
        admin.setStatus(deleteRequestDTO.getStatus());
        admin.setRemarks(deleteRequestDTO.getRemarks());

        return admin;
    }

    public static Admin updateAdminPassword(String password, String remarks, Admin admin) {
        admin.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        admin.setRemarks(remarks);
        admin.setStatus(ACTIVE);
        return admin;
    }

    private static BiFunction<AdminMacAddressInfoUpdateRequestDTO, Admin, AdminMacAddressInfo> convertToUpdatedMACAddressInfo =
            (requestDTO, admin) -> {

                AdminMacAddressInfo macAddressInfo = new AdminMacAddressInfo();
                macAddressInfo.setId(requestDTO.getId());
                macAddressInfo.setMacAddress(requestDTO.getMacAddress());
                macAddressInfo.setStatus(requestDTO.getStatus());
                macAddressInfo.setAdmin(admin);

                return macAddressInfo;
            };

    public static List<AdminMacAddressInfo> convertToUpdatedMACAddressInfo(
            List<AdminMacAddressInfoUpdateRequestDTO> macAddressInfoRequestDTOS, Admin admin) {

        return macAddressInfoRequestDTOS.stream()
                .map(macAddressInfo -> convertToUpdatedMACAddressInfo.apply(macAddressInfo, admin))
                .collect(Collectors.toList());
    }

    public static String parseUpdatedValues(Admin admin,
                                            AdminUpdateRequestDTO updateRequestDTO) {
        Map<String, String> params = new HashMap<>();

        if (!(updateRequestDTO.getFullName().equalsIgnoreCase(admin.getFullName())))
            params.put("Full Name ", toNormalCase(updateRequestDTO.getFullName()));

        if (!(updateRequestDTO.getEmail().equalsIgnoreCase(admin.getEmail())))
            params.put("Email Address ", updateRequestDTO.getEmail());

        if (!(updateRequestDTO.getMobileNumber().equalsIgnoreCase(admin.getMobileNumber())))
            params.put("Mobile Number ", updateRequestDTO.getMobileNumber());

        if (!(updateRequestDTO.getProfileId().equals(admin.getProfileId().getId())))
            params.put("Profile ", admin.getProfileId().getName());

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

        List<String> macAddress = updateRequestDTO.getMacAddressUpdateInfo()
                .stream()
                .filter(requestDTO -> requestDTO.getStatus().equals(ACTIVE))
                .map(AdminMacAddressInfoUpdateRequestDTO::getMacAddress)
                .collect(Collectors.toList());

        return (updateRequestDTO.getHasMacBinding().equals(ACTIVE))
                ? StringUtils.join(macAddress, COMMA_SEPARATED) : "N/A";
    }

    public static EmailRequestDTO parseToEmailRequestDTO(String fullname,
                                                         AdminUpdateRequestDTO updateRequestDTO,
                                                         String paramValues,
                                                         String updatedMacAddress) {
        return EmailRequestDTO.builder()
                .receiverEmailAddress(updateRequestDTO.getEmail())
                .subject(SUBJECT_FOR_UPDATE_ADMIN)
                .templateName(UPDATE_ADMIN)
                .paramValue(fullname + HYPHEN + paramValues + HYPHEN +
                        updateRequestDTO.getHasMacBinding() + HYPHEN + updatedMacAddress)
                .build();
    }

    public static void saveAdminPassword(AdminPasswordRequestDTO requestDTO,
                                         Admin admin) {
        admin.setPassword(BCrypt.hashpw(requestDTO.getPassword(), BCrypt.gensalt()));
        admin.setStatus(ACTIVE);
    }

    public static EmailRequestDTO parseToResetPasswordEmailRequestDTO(AdminResetPasswordRequestDTO requestDTO,
                                                                      String emailAddress,
                                                                      String fullName) {

        return EmailRequestDTO.builder()
                .receiverEmailAddress(emailAddress)
                .subject(SUBJECT_FOR_ADMIN_RESET_PASSWORD)
                .templateName(ADMIN_RESET_PASSWORD)
                .paramValue(fullName + COMMA_SEPARATED
                        + requestDTO.getPassword() + COMMA_SEPARATED + requestDTO.getRemarks())
                .build();
    }

    public static AdminFavourite parseToSaveFavourtie(Long userMenuId, Admin admin) {

        AdminFavourite adminFavourite = new AdminFavourite();
        adminFavourite.setUserMenuId(userMenuId);
        adminFavourite.setAdminId(admin);
        adminFavourite.setStatus(ACTIVE);

        return adminFavourite;
    }
}
