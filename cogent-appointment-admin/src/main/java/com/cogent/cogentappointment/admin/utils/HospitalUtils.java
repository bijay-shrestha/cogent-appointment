package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalContactNumberUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.*;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StringConstant.N;
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
                                         String requestedEsewaMerchantCode,
                                         String requestedAlias,
                                         String className) {
        final int NAME = 0;
        final int CODE = 1;
        final int ALIAS = 2;

        objects.forEach(object -> {
            boolean isNameExists = requestedName.equalsIgnoreCase((String) get(object, NAME));

            boolean isCodeExists = requestedEsewaMerchantCode.equalsIgnoreCase((String) get(object, CODE));

            boolean isAliasExists = requestedAlias.equalsIgnoreCase((String) get(object, ALIAS));

            if (isNameExists && isCodeExists && isAliasExists)
                throw new DataDuplicationException(
                        String.format(NAME_AND_CODE_DUPLICATION_MESSAGE, className, requestedName,
                                requestedEsewaMerchantCode),
                        "name", requestedName, "esewaMerchantCode", requestedEsewaMerchantCode
                );

            validateName(isNameExists, requestedName, className);
            validateCode(isCodeExists, requestedEsewaMerchantCode, className);
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
        hospital.setEsewaMerchantCode(toUpperCase(hospitalRequestDTO.getEsewaMerchantCode()));
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
        hospital.setCode(hospitalRequestDTO.getAlias());
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

    public static Hospital parseToUpdatedHospital(HospitalUpdateRequestDTO updateRequestDTO,
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

        return hospital;
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

    public static HospitalAppointmentServiceType parseToHospitalAppointmentServiceType(Hospital hospital,
                                                                                       AppointmentServiceType appointmentServiceType,
                                                                                       Character isPrimary) {

        HospitalAppointmentServiceType hospitalAppointmentServiceType = new HospitalAppointmentServiceType();
        hospitalAppointmentServiceType.setHospital(hospital);
        hospitalAppointmentServiceType.setAppointmentServiceType(appointmentServiceType);
        hospitalAppointmentServiceType.setIsPrimary(isPrimary);
        hospitalAppointmentServiceType.setStatus(ACTIVE);
        return hospitalAppointmentServiceType;
    }

    public static HospitalAppointmentServiceType updateHospitalAppointmentServiceTypeStatus(
            HospitalAppointmentServiceType hospitalAppointmentServiceType, Character status, Character isPrimary) {

        hospitalAppointmentServiceType.setStatus(status);
        return hospitalAppointmentServiceType;
    }
}
