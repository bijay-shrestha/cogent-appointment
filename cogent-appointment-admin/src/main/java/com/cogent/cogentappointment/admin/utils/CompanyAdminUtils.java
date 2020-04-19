package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminMacAddressInfoUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.Profile;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.EmailConstants.SUBJECT_FOR_ADMIN_VERIFICATION;
import static com.cogent.cogentappointment.admin.constants.EmailConstants.SUBJECT_FOR_UPDATE_ADMIN;
import static com.cogent.cogentappointment.admin.constants.EmailTemplates.ADMIN_VERIFICATION;
import static com.cogent.cogentappointment.admin.constants.EmailTemplates.UPDATE_ADMIN;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.admin.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.admin.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.admin.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.convertToNormalCase;

/**
 * @author smriti on 2019-08-11
 */
public class CompanyAdminUtils {

    public static Admin parseCompanyAdminDetails(CompanyAdminRequestDTO requestDTO,
                                                 Gender gender,
                                                 Profile profile) {
        Admin admin = new Admin();
        admin.setUsername(getUsername(requestDTO.getEmail()));
        admin.setFullName(convertToNormalCase(requestDTO.getFullName()));
        admin.setEmail(requestDTO.getEmail());
        admin.setMobileNumber(requestDTO.getMobileNumber());
        admin.setStatus(requestDTO.getStatus());
        admin.setHasMacBinding(requestDTO.getHasMacBinding());
        admin.setIsAccountActivated(NO);

        parseCompanyAdminDetails(gender, profile, admin);
        return admin;
    }


    public static void convertCompanyAdminUpdateRequestDTOToAdmin(Admin admin,
                                                                  CompanyAdminUpdateRequestDTO updateRequestDTO,
                                                                  Gender gender,
                                                                  Profile profile) {

        admin.setEmail(updateRequestDTO.getEmail());
        admin.setUsername(getUsername(admin.getEmail()));
        admin.setFullName(convertToNormalCase(updateRequestDTO.getFullName()));
        admin.setMobileNumber(updateRequestDTO.getMobileNumber());
        admin.setStatus(updateRequestDTO.getStatus());
        admin.setHasMacBinding(updateRequestDTO.getHasMacBinding());
        admin.setRemarks(updateRequestDTO.getRemarks());

        parseCompanyAdminDetails(gender, profile, admin);
        /*MODIFIED DATE AND MODIFIED BY*/
    }

    private static void parseCompanyAdminDetails(Gender gender, Profile profile, Admin admin) {
        admin.setGender(gender);
        admin.setProfileId(profile);
    }

    public static String getUsername(String email){
        StringTokenizer token = new StringTokenizer(email, "@");
        return token.nextToken();
    }

    public static String parseUpdatedCompanyAdminValues(Admin admin,
                                                        CompanyAdminUpdateRequestDTO updateRequestDTO) {
        Map<String, String> params = new HashMap<>();

        if (!(updateRequestDTO.getFullName().equalsIgnoreCase(admin.getFullName())))
            params.put("Full Name ", convertToNormalCase(updateRequestDTO.getFullName()));

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

    public static String parseUpdatedMacAddressForCompanyAdmin(CompanyAdminUpdateRequestDTO updateRequestDTO) {

        List<String> macAddress = updateRequestDTO.getMacAddressUpdateInfo()
                .stream()
                .filter(requestDTO -> requestDTO.getStatus().equals(ACTIVE))
                .map(AdminMacAddressInfoUpdateRequestDTO::getMacAddress)
                .collect(Collectors.toList());

        return (updateRequestDTO.getHasMacBinding().equals(ACTIVE))
                ? StringUtils.join(macAddress, COMMA_SEPARATED) : "N/A";
    }

    public static EmailRequestDTO parseToEmailRequestDTOForCompanyAdmin(String username,
                                                                        CompanyAdminUpdateRequestDTO updateRequestDTO,
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

    public static EmailRequestDTO convertCompanyAdminRequestToEmailRequestDTO(CompanyAdminRequestDTO adminRequestDTO,
                                                                       Admin admin,
                                                                       String confirmationToken) {

//        String origin = httpServletRequest.getHeader("origin");
//        String confirmationUrl = origin + "/#" + "/savePassword" + "?token =" + confirmationToken;

        String confirmationUrl = adminRequestDTO.getBaseUrl() + "/#" + "/savePassword" + "?token =" + confirmationToken;

        return EmailRequestDTO.builder()
                .receiverEmailAddress(adminRequestDTO.getEmail())
                .subject(SUBJECT_FOR_ADMIN_VERIFICATION)
                .templateName(ADMIN_VERIFICATION)
                .paramValue(admin.getUsername() + COMMA_SEPARATED + confirmationUrl)
                .build();
    }


}
