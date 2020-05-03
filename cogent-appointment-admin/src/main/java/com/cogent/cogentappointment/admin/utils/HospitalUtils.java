package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalContactNumberUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalContactNumberResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalResponseDTO;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalBanner;
import com.cogent.cogentappointment.persistence.model.HospitalContactNumber;
import com.cogent.cogentappointment.persistence.model.HospitalLogo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.*;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StringConstant.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.ALIAS_DUPLICATION_ERROR;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.NAME_DUPLICATION_ERROR;
import static com.cogent.cogentappointment.admin.utils.commons.SecurityContextUtils.getLoggedInCompanyId;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.convertToNormalCase;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.toUpperCase;
import static java.lang.reflect.Array.get;

/**
 * @author smriti ON 12/01/2020
 */
@Slf4j
public class HospitalUtils {

    public static void validateDuplicity(List<Object[]> objects,
                                         String requestedName,
                                         String requestedCode,
                                         String requestedAlias,
                                         String className) {
        final int NAME = 0;
        final int CODE = 1;
        final int ALIAS = 2;

        objects.forEach(object -> {
            boolean isNameExists = requestedName.equalsIgnoreCase((String) get(object, NAME));

            boolean isCodeExists = requestedCode.equalsIgnoreCase((String) get(object, CODE));

            boolean isAliasExists = requestedAlias.equalsIgnoreCase((String) get(object, ALIAS));

            if (isNameExists && isCodeExists && isAliasExists)
                throw new DataDuplicationException(
                        String.format(NAME_AND_CODE_DUPLICATION_MESSAGE, className, requestedName, requestedCode),
                        "name", requestedName, "code", requestedCode
                );

            validateName(isNameExists, requestedName, className);
            validateCode(isCodeExists, requestedCode, className);
            validateAlias(isAliasExists, requestedAlias, className);
        });
    }

    private static void validateName(boolean isNameExists, String name, String className) {
        if (isNameExists) {
            log.error(NAME_DUPLICATION_ERROR, className, name);
            throw new DataDuplicationException(String.format(NAME_DUPLICATION_MESSAGE, className, name), "name", name);
        }
    }

    private static void validateCode(boolean isCodeExists, String code, String className) {
        if (isCodeExists) {
            log.error(NAME_DUPLICATION_ERROR, className, code);
            throw new DataDuplicationException(String.format(CODE_DUPLICATION_MESSAGE, className, code), "code", code);
        }
    }

    private static void validateAlias(boolean isAliasExists, String alias, String className) {
        if (isAliasExists) {
            log.error(ALIAS_DUPLICATION_ERROR, className, alias);
            throw new DataDuplicationException(String.format(ALIAS_DUPLICATION_MESSAGE, className, alias), "alias", alias);
        }
    }

    public static Hospital convertDTOToHospital(HospitalRequestDTO hospitalRequestDTO) {
        Hospital hospital = new Hospital();
        hospital.setName(convertToNormalCase(hospitalRequestDTO.getName()));
        hospital.setCode(toUpperCase(hospitalRequestDTO.getHospitalCode()));
        hospital.setAddress(hospitalRequestDTO.getAddress());
        hospital.setPanNumber(hospitalRequestDTO.getPanNumber());
        hospital.setStatus(hospitalRequestDTO.getStatus());
        hospital.setIsCompany(N);
        hospital.setRefundPercentage(hospitalRequestDTO.getRefundPercentage());
        hospital.setNumberOfAdmins(hospitalRequestDTO.getNumberOfAdmins());
        hospital.setNumberOfFollowUps(hospitalRequestDTO.getNumberOfFollowUps());
        hospital.setFollowUpIntervalDays(hospitalRequestDTO.getFollowUpIntervalDays());
        hospital.setAlias(hospitalRequestDTO.getAlias());
        hospital.setCompanyId(getLoggedInCompanyId());
        return hospital;
    }

    public static HospitalContactNumber parseToHospitalContactNumber(Long hospitalId, String contactNumber) {
        HospitalContactNumber hospitalContactNumber = new HospitalContactNumber();
        parseToHospitalContactNumber(hospitalId, contactNumber, ACTIVE, hospitalContactNumber);
        return hospitalContactNumber;
    }

    public static HospitalLogo convertFileToHospitalLogo(FileUploadResponseDTO fileUploadResponseDTO,
                                                         Hospital hospital) {
        HospitalLogo hospitalLogo = new HospitalLogo();
        setLogoFileProperties(fileUploadResponseDTO, hospitalLogo);
        hospitalLogo.setHospital(hospital);
        return hospitalLogo;
    }

    public static void setLogoFileProperties(FileUploadResponseDTO fileUploadResponseDTO,
                                             HospitalLogo hospitalLogo) {
        hospitalLogo.setFileSize(fileUploadResponseDTO.getFileSize());
        hospitalLogo.setFileUri(fileUploadResponseDTO.getFileUri());
        hospitalLogo.setFileType(fileUploadResponseDTO.getFileType());
        hospitalLogo.setStatus(ACTIVE);
    }

    public static HospitalBanner convertFileToHospitalHospitalBanner(FileUploadResponseDTO fileUploadResponseDTO,
                                                                     Hospital hospital) {
        HospitalBanner hospitalBanner = new HospitalBanner();
        setBannerFileProperties(fileUploadResponseDTO, hospitalBanner);
        hospitalBanner.setHospital(hospital);
        return hospitalBanner;
    }

    public static void setBannerFileProperties(FileUploadResponseDTO fileUploadResponseDTO,
                                               HospitalBanner hospitalBanner) {
        hospitalBanner.setFileSize(fileUploadResponseDTO.getFileSize());
        hospitalBanner.setFileUri(fileUploadResponseDTO.getFileUri());
        hospitalBanner.setFileType(fileUploadResponseDTO.getFileType());
        hospitalBanner.setStatus(ACTIVE);
    }

    public static void parseToUpdatedHospital(HospitalUpdateRequestDTO updateRequestDTO,
                                              Hospital hospital) {

        hospital.setName(convertToNormalCase(updateRequestDTO.getName()));
        hospital.setAddress(updateRequestDTO.getAddress());
        hospital.setPanNumber(updateRequestDTO.getPanNumber());
        hospital.setStatus(updateRequestDTO.getStatus());
        hospital.setRemarks(convertToNormalCase(updateRequestDTO.getRemarks()));
        hospital.setRefundPercentage(updateRequestDTO.getRefundPercentage());
        hospital.setNumberOfAdmins(updateRequestDTO.getNumberOfAdmins());
        hospital.setNumberOfFollowUps(updateRequestDTO.getNumberOfFollowUps());
        hospital.setFollowUpIntervalDays(updateRequestDTO.getFollowUpIntervalDays());
    }

    public static HospitalContactNumber parseToUpdatedHospitalContactNumber(
            Long hospitalId,
            HospitalContactNumberUpdateRequestDTO updateRequestDTO) {

        HospitalContactNumber hospitalContactNumber = new HospitalContactNumber();
        hospitalContactNumber.setId(updateRequestDTO.getHospitalContactNumberId());
        parseToHospitalContactNumber(hospitalId, updateRequestDTO.getContactNumber(),
                updateRequestDTO.getStatus(), hospitalContactNumber);
        return hospitalContactNumber;
    }

    private static void parseToHospitalContactNumber(Long hospitalId,
                                                     String contactNumber,
                                                     Character status,
                                                     HospitalContactNumber hospitalContactNumber) {
        hospitalContactNumber.setHospitalId(hospitalId);
        hospitalContactNumber.setContactNumber(contactNumber);
        hospitalContactNumber.setStatus(status);
    }

    public static Hospital parseToDeletedHospital(Hospital hospital, DeleteRequestDTO deleteRequestDTO) {
        hospital.setStatus(deleteRequestDTO.getStatus());
        hospital.setRemarks(deleteRequestDTO.getRemarks());

        return hospital;
    }

    public static HospitalResponseDTO parseToHospitalResponseDTO(Object[] results) {
        final int HOSPITAL_ID_INDEX = 0;
        final int NAME_INDEX = 1;
        final int STATUS_INDEX = 2;
        final int ADDRESS_INDEX = 3;
        final int PAN_NUMBER_INDEX = 4;
        final int REMARKS_INDEX = 5;
        final int HOSPITAL_LOGO_INDEX = 6;
        final int HOSPITAL_BANNER_INDEX = 7;
        final int HOSPITAL_CODE_INDEX = 8;
        final int CONTACT_DETAILS_INDEX = 9;
        final int REFUND_PERCENTAGE_INDEX = 10;
        final int NUMBER_OF_ADMINS_INDEX = 11;
        final int NUMBER_OF_FOLLOWUPS_INDEX = 12;
        final int FOLLOW_UP_INTERVAL_DAYS_INDEX = 13;
        final int IS_COMPANY_INDEX = 14;
        final int ALIAS_INDEX = 15;

        final int CREATED_BY_INDEX = 16;
        final int CREATED_DATE_INDEX = 17;
        final int LAST_MODIFIED_BY_INDEX = 18;
        final int LAST_MODIFIED_DATE_INDEX = 19;

        return HospitalResponseDTO.builder()
                .id(Long.parseLong(results[HOSPITAL_ID_INDEX].toString()))
                .name(results[NAME_INDEX].toString())
                .status(results[STATUS_INDEX].toString().charAt(0))
                .address(results[ADDRESS_INDEX].toString())
                .panNumber(results[PAN_NUMBER_INDEX].toString())
                .remarks(Objects.isNull(results[REMARKS_INDEX]) ? null : results[REMARKS_INDEX].toString())
                .hospitalLogo(Objects.isNull(results[HOSPITAL_LOGO_INDEX]) ? null : results[HOSPITAL_LOGO_INDEX].toString())
                .hospitalBanner(Objects.isNull(results[HOSPITAL_BANNER_INDEX]) ? null : results[HOSPITAL_BANNER_INDEX].toString())
                .hospitalCode(results[HOSPITAL_CODE_INDEX].toString())
                .contactNumberResponseDTOS(Objects.isNull(results[CONTACT_DETAILS_INDEX]) ?
                        new ArrayList<>() : parseToHospitalContactNumberResponseDTOS(results))
                .refundPercentage(Double.parseDouble(results[REFUND_PERCENTAGE_INDEX].toString()))
                .numberOfAdmins(Objects.isNull(results[NUMBER_OF_ADMINS_INDEX]) ? 0 :
                        Integer.parseInt(results[NUMBER_OF_ADMINS_INDEX].toString()))
                .numberOfFollowUps(Objects.isNull(results[NUMBER_OF_FOLLOWUPS_INDEX]) ?
                        0 : Integer.parseInt(results[NUMBER_OF_FOLLOWUPS_INDEX].toString()))
                .followUpIntervalDays(Objects.isNull(results[NUMBER_OF_FOLLOWUPS_INDEX]) ?
                        0 : Integer.parseInt(results[FOLLOW_UP_INTERVAL_DAYS_INDEX].toString()))
                .isCompany(Objects.isNull(results[IS_COMPANY_INDEX]) ? 'N' :
                        results[IS_COMPANY_INDEX].toString().charAt(0))
                .alias(Objects.isNull(results[ALIAS_INDEX]) ? null : results[ALIAS_INDEX].toString())
                .createdBy(results[CREATED_BY_INDEX].toString())
                .createdDate((Date) results[CREATED_DATE_INDEX])
                .lastModifiedBy(results[LAST_MODIFIED_BY_INDEX].toString())
                .lastModifiedDate((Date) results[LAST_MODIFIED_DATE_INDEX])
                .build();
    }

    private static List<HospitalContactNumberResponseDTO> parseToHospitalContactNumberResponseDTOS(Object[] results) {

        final int CONTACT_DETAILS_INDEX = 9;

        String[] contactWithIdAndNumber = results[CONTACT_DETAILS_INDEX].toString().split(COMMA_SEPARATED);

        return Arrays.stream(contactWithIdAndNumber)
                .map(contact -> contact.split(FORWARD_SLASH))
                .map(contactDetails -> HospitalContactNumberResponseDTO.builder()
                        .hospitalContactNumberId(Long.parseLong(contactDetails[0]))
                        .contactNumber(contactDetails[1])
                        .status(contactDetails[2].charAt(0))
                        .build())
                .collect(Collectors.toList());
    }
}
