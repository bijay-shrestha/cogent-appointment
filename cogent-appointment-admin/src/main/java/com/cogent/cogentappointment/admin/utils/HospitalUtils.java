package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.constants.StringConstant;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalContactNumberUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalContactNumberResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalResponseDTO;
import com.cogent.cogentappointment.admin.utils.commons.StringUtil;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalBanner;
import com.cogent.cogentappointment.persistence.model.HospitalContactNumber;
import com.cogent.cogentappointment.persistence.model.HospitalLogo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.ACTIVE;

/**
 * @author smriti ON 12/01/2020
 */
public class HospitalUtils {

    public static Hospital convertDTOToHospital(HospitalRequestDTO hospitalRequestDTO) {
        Hospital hospital = new Hospital();
        hospital.setName(StringUtil.toUpperCase(hospitalRequestDTO.getName()));
        hospital.setCode(StringUtil.toUpperCase(hospitalRequestDTO.getHospitalCode()));
        hospital.setAddress(hospitalRequestDTO.getAddress());
        hospital.setPanNumber(hospitalRequestDTO.getPanNumber());
        hospital.setStatus(hospitalRequestDTO.getStatus());
        hospital.setIsCogentAdmin(hospitalRequestDTO.getIsCogentAdmin());
        hospital.setRefundPercentage(hospitalRequestDTO.getRefundPercentage());
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

        hospital.setName(StringUtil.toUpperCase(updateRequestDTO.getName()));
        hospital.setAddress(updateRequestDTO.getAddress());
        hospital.setPanNumber(updateRequestDTO.getPanNumber());
        hospital.setStatus(updateRequestDTO.getStatus());
        hospital.setRemarks(updateRequestDTO.getRemarks());
        hospital.setIsCogentAdmin(updateRequestDTO.getIsHospital());
        hospital.setRefundPercentage(updateRequestDTO.getRefundPercentage());
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

    public static void parseToDeletedHospital(Hospital hospital, DeleteRequestDTO deleteRequestDTO) {
        hospital.setStatus(deleteRequestDTO.getStatus());
        hospital.setRemarks(deleteRequestDTO.getRemarks());
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
        final int REFUND_PERCENTAGE_INDEX =10;

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
                .build();
    }

    private static List<HospitalContactNumberResponseDTO> parseToHospitalContactNumberResponseDTOS(Object[] results) {

        final int CONTACT_DETAILS_INDEX = 9;

        String[] contactWithIdAndNumber = results[CONTACT_DETAILS_INDEX].toString().split(StringConstant.COMMA_SEPARATED);

        return Arrays.stream(contactWithIdAndNumber)
                .map(contact -> contact.split(StringConstant.HYPHEN))
                .map(contactDetails -> HospitalContactNumberResponseDTO.builder()
                        .hospitalContactNumberId(Long.parseLong(contactDetails[0]))
                        .contactNumber((contactDetails[1]==null)? null:contactDetails[1])
                        .status((contactDetails[2].charAt(0)==' ')? null:(contactDetails[2].charAt(0)))
                        .build())
                .collect(Collectors.toList());
    }
}
