package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanyContactNumberUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanyRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanyUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyContactNumberResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.admin.utils.commons.StringUtil;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalContactNumber;
import com.cogent.cogentappointment.persistence.model.HospitalLogo;

import java.util.*;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.admin.constants.StringConstant.*;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti ON 12/01/2020
 */
public class CompanyUtils {

    public static Hospital convertCompanyDTOToHospital(CompanyRequestDTO hospitalRequestDTO) {
        Hospital hospital = new Hospital();
        hospital.setName(hospitalRequestDTO.getName());
        hospital.setCode(toUpperCase(hospitalRequestDTO.getCompanyCode()));
        hospital.setAddress(hospitalRequestDTO.getAddress());
        hospital.setPanNumber(hospitalRequestDTO.getPanNumber());
        hospital.setStatus(hospitalRequestDTO.getStatus());
        hospital.setIsCompany(Y);
        hospital.setAlias(hospitalRequestDTO.getAlias());
        return hospital;
    }

    public static HospitalContactNumber parseToCompanyContactNumber(Long companyId, String contactNumber) {
        HospitalContactNumber companyContactNumber = new HospitalContactNumber();
        parseToCompanyContactNumber(companyId, contactNumber, ACTIVE, companyContactNumber);
        return companyContactNumber;
    }

    public static HospitalLogo convertFileToCompanyLogo(FileUploadResponseDTO fileUploadResponseDTO,
                                                        Hospital company) {
        HospitalLogo companyLogo = new HospitalLogo();
        setLogoFileProperties(fileUploadResponseDTO, companyLogo);
        companyLogo.setHospital(company);
        return companyLogo;
    }

    public static void setLogoFileProperties(FileUploadResponseDTO fileUploadResponseDTO,
                                             HospitalLogo companyLogo) {
        companyLogo.setFileSize(fileUploadResponseDTO.getFileSize());
        companyLogo.setFileUri(fileUploadResponseDTO.getFileUri());
        companyLogo.setFileType(fileUploadResponseDTO.getFileType());
        companyLogo.setStatus(ACTIVE);
    }

    public static Hospital parseToUpdatedCompany(CompanyUpdateRequestDTO updateRequestDTO,
                                             Hospital company) {

        company.setName(updateRequestDTO.getName());
        company.setAddress(updateRequestDTO.getAddress());
        company.setPanNumber(updateRequestDTO.getPanNumber());
        company.setStatus(updateRequestDTO.getStatus());
        company.setRemarks(StringUtil.convertToNormalCase(updateRequestDTO.getRemarks()));
        company.setIsCompany(company.getIsCompany());
        company.setAlias(updateRequestDTO.getAlias());

        return company;
    }

    public static HospitalContactNumber parseToUpdatedCompanyContactNumber(
            Long companyId,
            CompanyContactNumberUpdateRequestDTO updateRequestDTO) {

        HospitalContactNumber hospitalContactNumber = new HospitalContactNumber();
        hospitalContactNumber.setId(updateRequestDTO.getCompanyContactNumberId());
        parseToCompanyContactNumber(companyId, updateRequestDTO.getContactNumber(),
                updateRequestDTO.getStatus(), hospitalContactNumber);
        return hospitalContactNumber;
    }

    private static void parseToCompanyContactNumber(Long companyId,
                                                    String contactNumber,
                                                    Character status,
                                                    HospitalContactNumber companyContactNumber) {
        companyContactNumber.setHospitalId(companyId);
        companyContactNumber.setContactNumber(contactNumber);
        companyContactNumber.setStatus(status);
    }

    public static Hospital parseToDeletedCompany(Hospital company, DeleteRequestDTO deleteRequestDTO) {
        company.setStatus(deleteRequestDTO.getStatus());
        company.setRemarks(deleteRequestDTO.getRemarks());

        return company;
    }

    public static CompanyResponseDTO parseToCompanyResponseDTO(Object[] results) {
        final int COMPANY_ID_INDEX = 0;
        final int NAME_INDEX = 1;
        final int STATUS_INDEX = 2;
        final int ADDRESS_INDEX = 3;
        final int PAN_NUMBER_INDEX = 4;
        final int REMARKS_INDEX = 5;
        final int COMPANY_LOGO_INDEX = 6;
        final int COMPANY_CODE_INDEX = 7;
        final int CONTACT_DETAILS_INDEX = 8;
        final int IS_COMPANY_INDEX = 9;
        final int ALIAS_INDEX = 10;

        final int CREATED_BY_INDEX = 11;
        final int CREATED_DATE_INDEX = 12;
        final int LAST_MODIFIED_BY_INDEX=13;
        final int LAST_MODIFIED_DATE=14;

        return CompanyResponseDTO.builder()
                .id(Long.parseLong(results[COMPANY_ID_INDEX].toString()))
                .name(results[NAME_INDEX].toString())
                .status(results[STATUS_INDEX].toString().charAt(0))
                .address(results[ADDRESS_INDEX].toString())
                .panNumber(results[PAN_NUMBER_INDEX].toString())
                .remarks(Objects.isNull(results[REMARKS_INDEX]) ? null : results[REMARKS_INDEX].toString())
                .companyLogo(Objects.isNull(results[COMPANY_LOGO_INDEX]) ? null : results[COMPANY_LOGO_INDEX].toString())
                .companyCode(results[COMPANY_CODE_INDEX].toString())
                .contactNumberResponseDTOS(Objects.isNull(results[CONTACT_DETAILS_INDEX]) ?
                        new ArrayList<>() : parseToCompanyContactNumberResponseDTOS(results))
                .isCompany(results[IS_COMPANY_INDEX].toString().charAt(0))
                .alias(results[ALIAS_INDEX].toString())
                .createdBy(results[CREATED_BY_INDEX].toString())
                .createdDate((Date) results[CREATED_DATE_INDEX])
                .lastModifiedBy(results[LAST_MODIFIED_BY_INDEX].toString())
                .lastModifiedDate((Date) results[LAST_MODIFIED_DATE])
                .build();
    }

    private static List<CompanyContactNumberResponseDTO> parseToCompanyContactNumberResponseDTOS(Object[] results) {

        final int CONTACT_DETAILS_INDEX = 8;

        String[] contactWithIdAndNumber = results[CONTACT_DETAILS_INDEX].toString().split(COMMA_SEPARATED);

        return Arrays.stream(contactWithIdAndNumber)
                .map(contact -> contact.split(FORWARD_SLASH))
                .map(contactDetails -> CompanyContactNumberResponseDTO.builder()
                        .companyContactNumberId(Long.parseLong(contactDetails[0]))
                        .contactNumber(contactDetails[1])
                        .status(contactDetails[2].charAt(0))
                        .build())
                .collect(Collectors.toList());
    }
}
