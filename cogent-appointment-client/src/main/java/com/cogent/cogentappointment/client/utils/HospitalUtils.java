package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospital.HospitalContactNumberUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospital.HospitalRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospital.HospitalUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalContactNumberResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalResponseDTO;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalContactNumber;
import com.cogent.cogentappointment.persistence.model.HospitalLogo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.client.constants.StringConstant.HYPHEN;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti ON 12/01/2020
 */
public class HospitalUtils {

    public static Hospital convertDTOToHospital(HospitalRequestDTO hospitalRequestDTO) {
        Hospital hospital = new Hospital();
        hospital.setName(toUpperCase(hospitalRequestDTO.getName()));
        hospital.setCode(toUpperCase(hospitalRequestDTO.getHospitalCode()));
        hospital.setAddress(hospitalRequestDTO.getAddress());
        hospital.setPanNumber(hospitalRequestDTO.getPanNumber());
        hospital.setStatus(hospitalRequestDTO.getStatus());
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
        setFileProperties(fileUploadResponseDTO, hospitalLogo);
        hospitalLogo.setHospital(hospital);
        return hospitalLogo;
    }

    public static void setFileProperties(FileUploadResponseDTO fileUploadResponseDTO,
                                         HospitalLogo hospitalLogo) {
        hospitalLogo.setFileSize(fileUploadResponseDTO.getFileSize());
        hospitalLogo.setFileUri(fileUploadResponseDTO.getFileUri());
        hospitalLogo.setFileType(fileUploadResponseDTO.getFileType());
        hospitalLogo.setStatus(ACTIVE);
    }

    public static void parseToUpdatedHospital(HospitalUpdateRequestDTO updateRequestDTO,
                                              Hospital hospital) {

        hospital.setName(toUpperCase(updateRequestDTO.getName()));
        hospital.setAddress(updateRequestDTO.getAddress());
        hospital.setPanNumber(updateRequestDTO.getPanNumber());
        hospital.setStatus(updateRequestDTO.getStatus());
        hospital.setRemarks(updateRequestDTO.getRemarks());
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
        final int FILE_URI_INDEX = 6;
        final int HOSPITAL_CODE_INDEX = 7;
        final int CONTACT_DETAILS_INDEX = 8;

        return HospitalResponseDTO.builder()
                .id(Long.parseLong(results[HOSPITAL_ID_INDEX].toString()))
                .name(results[NAME_INDEX].toString())
                .status(results[STATUS_INDEX].toString().charAt(0))
                .address(results[ADDRESS_INDEX].toString())
                .panNumber(results[PAN_NUMBER_INDEX].toString())
                .remarks(Objects.isNull(results[REMARKS_INDEX]) ? null : results[REMARKS_INDEX].toString())
                .fileUri(Objects.isNull(results[FILE_URI_INDEX]) ? null : results[FILE_URI_INDEX].toString())
                .contactNumberResponseDTOS(Objects.isNull(results[CONTACT_DETAILS_INDEX]) ?
                        new ArrayList<>() : parseToHospitalContactNumberResponseDTOS(results))
                .hospitalCode(results[HOSPITAL_CODE_INDEX].toString())
                .build();
    }

    private static List<HospitalContactNumberResponseDTO> parseToHospitalContactNumberResponseDTOS(Object[] results) {

        final int CONTACT_DETAILS_INDEX = 8;

        String[] contactWithIdAndNumber = results[CONTACT_DETAILS_INDEX].toString().split(COMMA_SEPARATED);

        return Arrays.stream(contactWithIdAndNumber)
                .map(contact -> contact.split(HYPHEN))
                .map(contactDetails -> HospitalContactNumberResponseDTO.builder()
                        .hospitalContactNumberId(Long.parseLong(contactDetails[0]))
                        .contactNumber(contactDetails[1])
                        .build())
                .collect(Collectors.toList());
    }
}
