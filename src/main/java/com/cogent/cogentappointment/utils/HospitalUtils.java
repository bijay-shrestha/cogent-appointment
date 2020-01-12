package com.cogent.cogentappointment.utils;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.request.hospital.HospitalRequestDTO;
import com.cogent.cogentappointment.dto.request.hospital.HospitalUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.model.Hospital;
import com.cogent.cogentappointment.model.HospitalContactNumber;
import com.cogent.cogentappointment.model.HospitalLogo;
import com.cogent.cogentappointment.utils.commons.MapperUtility;
import com.cogent.cogentappointment.utils.commons.RandomNumberGenerator;

import static com.cogent.cogentappointment.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti ON 12/01/2020
 */
public class HospitalUtils {

    public static Hospital convertDTOToHospital(HospitalRequestDTO hospitalRequestDTO) {
        Hospital hospital = MapperUtility.map(hospitalRequestDTO, Hospital.class);
        hospital.setName(toUpperCase(hospital.getName()));
        hospital.setCode(RandomNumberGenerator.generateRandomNumber(3));
        return hospital;
    }

    public static HospitalContactNumber parseToHospitalContactNumber(Long hospitalId, String contactNumber) {
        HospitalContactNumber hospitalContactNumber = new HospitalContactNumber();
        hospitalContactNumber.setHospitalId(hospitalId);
        hospitalContactNumber.setContactNumber(contactNumber);
        hospitalContactNumber.setStatus(ACTIVE);
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
        hospitalLogo.setIsDefaultImage(NO);
    }

    public static Hospital convertToUpdatedHospital(HospitalUpdateRequestDTO updateRequestDTO,
                                                    Hospital hospital) {

        hospital.setName(toUpperCase(updateRequestDTO.getName()));
        hospital.setStatus(updateRequestDTO.getStatus());
        hospital.setAddress(updateRequestDTO.getAddress());
        hospital.setRemarks(updateRequestDTO.getRemarks());
//        hospital.setCode(updateRequestDTO.getHospitalCode());
//        hospital.setHospitalPanNumber(updateRequestDTO.getHospitalPanNumber());
//        hospital.setHospitalLogo(updateRequestDTO.getHospitalLogo());
//        hospital.setHospitalPhone(updateRequestDTO.getHospitalPhone());


        return hospital;
    }

    public static void parseToDeletedHospital(Hospital hospital, DeleteRequestDTO deleteRequestDTO) {
        hospital.setStatus(deleteRequestDTO.getStatus());
        hospital.setRemarks(deleteRequestDTO.getRemarks());
    }

}
