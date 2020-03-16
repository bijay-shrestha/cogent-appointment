package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.constants.StatusConstants;
import com.cogent.cogentappointment.admin.constants.StringConstant;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorQualificationUpdateDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorSpecializationUpdateDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.DoctorUpdateDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorQualificationResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorSpecializationResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorUpdateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.admin.utils.commons.StringUtil;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.cogent.cogentappointment.admin.utils.commons.NumberFormatterUtils.formatDoubleTo2DecimalPlaces;
import static com.cogent.cogentappointment.admin.utils.commons.NumberFormatterUtils.generateRandomNumber;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.convertToNormalCase;

/**
 * @author smriti on 2019-09-29
 */
public class DoctorUtils {

    public static Doctor parseDTOToDoctor(DoctorRequestDTO requestDTO,
                                          Gender gender,
                                          Hospital hospital) {
        Doctor doctor = new Doctor();
        doctor.setName(convertToNormalCase(requestDTO.getName()));
        doctor.setCode(generateRandomNumber(3));
        doctor.setMobileNumber(requestDTO.getMobileNumber());
        doctor.setEmail(requestDTO.getEmail());
        doctor.setNmcNumber(requestDTO.getNmcNumber());
        doctor.setStatus(requestDTO.getStatus());
        doctor.setGender(gender);
        doctor.setHospital(hospital);
        return doctor;
    }

    public static DoctorSpecialization parseToDoctorSpecialization(Long doctorId,
                                                                   Long specializationId) {
        DoctorSpecialization doctorSpecialization =
                convertToDoctorSpecialization(doctorId, specializationId);
        doctorSpecialization.setStatus(StatusConstants.ACTIVE);
        return doctorSpecialization;
    }

    private static DoctorSpecialization convertToDoctorSpecialization(Long doctorId,
                                                                      Long specializationId) {
        DoctorSpecialization doctorSpecialization = new DoctorSpecialization();
        doctorSpecialization.setDoctorId(doctorId);
        doctorSpecialization.setSpecializationId(specializationId);
        return doctorSpecialization;
    }

    public static DoctorQualification parseToDoctorQualification(Long doctorId,
                                                                 Long qualificationId) {
        DoctorQualification doctorQualification =
                convertToDoctorQualification(doctorId, qualificationId);
        doctorQualification.setStatus(StatusConstants.ACTIVE);
        return doctorQualification;
    }

    private static DoctorQualification convertToDoctorQualification(Long doctorId,
                                                                    Long qualificationId) {
        DoctorQualification doctorQualification = new DoctorQualification();
        doctorQualification.setDoctorId(doctorId);
        doctorQualification.setQualificationId(qualificationId);
        return doctorQualification;
    }

    public static DoctorAppointmentCharge parseToDoctorAppointmentCharge(Doctor doctor,
                                                                         Double appointmentCharge,
                                                                         Double appointmentFollowUpCharge) {
        DoctorAppointmentCharge doctorAppointmentCharge = new DoctorAppointmentCharge();
        doctorAppointmentCharge.setDoctorId(doctor);
        parseDoctorAppointmentChargeDetails(doctorAppointmentCharge, appointmentCharge, appointmentFollowUpCharge);
        return doctorAppointmentCharge;
    }

    public static DoctorAvatar convertFileToDoctorAvatar(FileUploadResponseDTO uploadResponseDTO,
                                                         Doctor doctor) {

        DoctorAvatar doctorAvatar = new DoctorAvatar();
        setAvatarFileProperties(uploadResponseDTO, doctorAvatar);
        doctorAvatar.setDoctorId(doctor);
        return doctorAvatar;
    }

    public static void setAvatarFileProperties(FileUploadResponseDTO uploadResponseDTO,
                                               DoctorAvatar doctorAvatar) {
        doctorAvatar.setFileSize(uploadResponseDTO.getFileSize());
        doctorAvatar.setFileUri(uploadResponseDTO.getFileUri());
        doctorAvatar.setFileType(uploadResponseDTO.getFileType());
        doctorAvatar.setStatus(StatusConstants.ACTIVE);
    }

    public static void parseDoctorAppointmentChargeDetails(DoctorAppointmentCharge doctorAppointmentCharge,
                                                           Double appointmentCharge,
                                                           Double appointmentFollowUpCharge) {
        doctorAppointmentCharge.setAppointmentCharge(formatDoubleTo2DecimalPlaces(appointmentCharge));
        doctorAppointmentCharge.setAppointmentFollowUpCharge(formatDoubleTo2DecimalPlaces(appointmentFollowUpCharge));
    }

    public static void convertToUpdatedDoctor(DoctorUpdateDTO requestDTO,
                                              Doctor doctor,
                                              Gender gender,
                                              Hospital hospital) {

        doctor.setName(convertToNormalCase(requestDTO.getName()));
        doctor.setMobileNumber(requestDTO.getMobileNumber());
        doctor.setEmail(requestDTO.getEmail());
        doctor.setNmcNumber(requestDTO.getNmcNumber());
        doctor.setStatus(requestDTO.getStatus());
        doctor.setRemarks(requestDTO.getRemarks());
        doctor.setGender(gender);
        doctor.setHospital(hospital);
    }

    public static DoctorSpecialization parseToUpdatedDoctorSpecialization(
            Long doctorId,
            DoctorSpecializationUpdateDTO updateRequestDTO) {

        DoctorSpecialization doctorSpecialization =
                convertToDoctorSpecialization(doctorId, updateRequestDTO.getSpecializationId());
        doctorSpecialization.setId(updateRequestDTO.getDoctorSpecializationId());
        doctorSpecialization.setStatus(updateRequestDTO.getStatus());
        return doctorSpecialization;
    }

    public static DoctorQualification parseToUpdatedDoctorQualification(
            Long doctorId,
            DoctorQualificationUpdateDTO updateRequestDTO) {

        DoctorQualification doctorQualification =
                convertToDoctorQualification(doctorId, updateRequestDTO.getQualificationId());
        doctorQualification.setId(updateRequestDTO.getDoctorQualificationId());
        doctorQualification.setStatus(updateRequestDTO.getStatus());
        return doctorQualification;
    }

    public static void convertToDeletedDoctor(Doctor doctor,
                                              DeleteRequestDTO deleteRequestDTO) {
        doctor.setStatus(deleteRequestDTO.getStatus());
        doctor.setRemarks(deleteRequestDTO.getRemarks());
    }

    public static DoctorUpdateResponseDTO parseToDoctorUpdateResponseDTO(Object[] results) {

        final int DOCTOR_ID_INDEX = 0;
        final int NAME_INDEX = 1;
        final int MOBILE_NUMBER_INDEX = 2;
        final int STATUS_INDEX = 3;
        final int CODE_INDEX = 4;
        final int EMAIL_INDEX = 5;
        final int NMC_NUMBER_INDEX = 6;
        final int REMARKS_INDEX = 7;
        final int GENDER_INDEX = 8;
        final int HOSPITAL_NAME_INDEX = 9;
        final int APPOINTMENT_CHARGE_INDEX = 10;
        final int APPOINTMENT_FOLLOW_UP_CHARGE = 11;
        final int HOSPITAL_ID_INDEX = 12;
        final int FILE_URI_INDEX = 19;

        return DoctorUpdateResponseDTO.builder()
                .doctorId(Long.parseLong(results[DOCTOR_ID_INDEX].toString()))
                .doctorName(results[NAME_INDEX].toString())
                .mobileNumber(results[MOBILE_NUMBER_INDEX].toString())
                .status(results[STATUS_INDEX].toString().charAt(0))
                .code(results[CODE_INDEX].toString())
                .email(results[EMAIL_INDEX].toString())
                .nmcNumber(results[NMC_NUMBER_INDEX].toString())
                .remarks(Objects.isNull(results[REMARKS_INDEX]) ? null : results[REMARKS_INDEX].toString())
                .gender(results[GENDER_INDEX].toString())
                .hospitalId(results[HOSPITAL_ID_INDEX].toString())
                .hospitalName(results[HOSPITAL_NAME_INDEX].toString())
                .appointmentCharge(Double.parseDouble(results[APPOINTMENT_CHARGE_INDEX].toString()))
                .appointmentFollowUpCharge(Double.parseDouble(results[APPOINTMENT_FOLLOW_UP_CHARGE].toString()))
                .doctorSpecializationResponseDTOS(parseToDoctorSpecialization(results))
                .doctorQualificationResponseDTOS(parseToDoctorQualification(results))
                .fileUri(results[FILE_URI_INDEX].toString())
                .build();
    }

    private static List<DoctorSpecializationResponseDTO> parseToDoctorSpecialization(Object[] results) {
        final int DOCTOR_SPECIALIZATION_ID_INDEX = 13;
        final int SPECIALIZATION_ID_INDEX = 14;
        final int SPECIALIZATION_NAME_INDEX = 15;

        String[] doctorSpecializationIds = results[DOCTOR_SPECIALIZATION_ID_INDEX].toString().split(StringConstant.COMMA_SEPARATED);
        String[] specializationIds = results[SPECIALIZATION_ID_INDEX].toString().split(StringConstant.COMMA_SEPARATED);
        String[] specializationNames = results[SPECIALIZATION_NAME_INDEX].toString().split(StringConstant.COMMA_SEPARATED);

        return IntStream.range(0, doctorSpecializationIds.length)
                .mapToObj(i -> DoctorSpecializationResponseDTO.builder()
                        .doctorSpecializationId(Long.parseLong(doctorSpecializationIds[i]))
                        .specializationId(Long.parseLong(specializationIds[i]))
                        .specializationName(specializationNames[i])
                        .build())
                .collect(Collectors.toList());
    }

    private static List<DoctorQualificationResponseDTO> parseToDoctorQualification(Object[] results) {

        final int DOCTOR_QUALIFICATION_ID_INDEX = 16;
        final int QUALIFICATION_ID_INDEX = 17;
        final int QUALIFICATION_NAME_INDEX = 18;

        String[] doctorQualificationIds = results[DOCTOR_QUALIFICATION_ID_INDEX].toString().split(StringConstant.COMMA_SEPARATED);
        String[] qualificationIds = results[QUALIFICATION_ID_INDEX].toString().split(StringConstant.COMMA_SEPARATED);
        String[] qualificationNames = results[QUALIFICATION_NAME_INDEX].toString().split(StringConstant.COMMA_SEPARATED);

        return IntStream.range(0, doctorQualificationIds.length)
                .mapToObj(i -> DoctorQualificationResponseDTO.builder()
                        .doctorQualificationId(Long.parseLong(doctorQualificationIds[i]))
                        .qualificationId(Long.parseLong(qualificationIds[i]))
                        .qualificationName(qualificationNames[i])
                        .build())
                .collect(Collectors.toList());
    }


}
