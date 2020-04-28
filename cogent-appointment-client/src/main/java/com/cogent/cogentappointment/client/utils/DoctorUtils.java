package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctor.DoctorQualificationUpdateDTO;
import com.cogent.cogentappointment.client.dto.request.doctor.DoctorRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctor.DoctorSpecializationUpdateDTO;
import com.cogent.cogentappointment.client.dto.request.doctor.DoctorUpdateDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.DoctorRevenueResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.DoctorRevenueResponseListDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorQualificationResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorSpecializationResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorUpdateResponseDTO;
import com.cogent.cogentappointment.client.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.cogent.cogentappointment.client.constants.StatusConstants.ACTIVE;
import static com.cogent.cogentappointment.client.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.client.utils.commons.NumberFormatterUtils.formatDoubleTo2DecimalPlaces;
import static com.cogent.cogentappointment.client.utils.commons.NumberFormatterUtils.generateRandomNumber;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.*;

/**
 * @author smriti on 2019-09-29
 */
public class DoctorUtils {


    public static Doctor parseDTOToDoctor(DoctorRequestDTO requestDTO,
                                          Gender gender,
                                          Hospital hospital) {
        Doctor doctor = new Doctor();
        doctor.setName(toNormalCase(requestDTO.getName()));
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
        doctorSpecialization.setStatus(ACTIVE);
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
        doctorQualification.setStatus(ACTIVE);
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
        doctorAvatar.setStatus(ACTIVE);
    }

    public static void parseDoctorAppointmentChargeDetails(DoctorAppointmentCharge doctorAppointmentCharge,
                                                           Double appointmentCharge,
                                                           Double appointmentFollowUpCharge) {
        doctorAppointmentCharge.setAppointmentCharge(formatDoubleTo2DecimalPlaces(appointmentCharge));
        doctorAppointmentCharge.setAppointmentFollowUpCharge(formatDoubleTo2DecimalPlaces(appointmentFollowUpCharge));
    }

    public static void convertToUpdatedDoctor(DoctorUpdateDTO requestDTO,
                                              Doctor doctor,
                                              Gender gender) {

        doctor.setName(toNormalCase(requestDTO.getName()));
        doctor.setMobileNumber(requestDTO.getMobileNumber());
        doctor.setEmail(requestDTO.getEmail());
        doctor.setNmcNumber(requestDTO.getNmcNumber());
        doctor.setStatus(requestDTO.getStatus());
        doctor.setRemarks(requestDTO.getRemarks());
        doctor.setGender(gender);
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
        final int APPOINTMENT_CHARGE_INDEX = 9;
        final int APPOINTMENT_FOLLOW_UP_CHARGE = 10;
        final int FILE_URI_INDEX = 17;

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
                .appointmentCharge(Double.parseDouble(results[APPOINTMENT_CHARGE_INDEX].toString()))
                .appointmentFollowUpCharge((results[APPOINTMENT_FOLLOW_UP_CHARGE] != null) ?
                        Double.parseDouble(results[APPOINTMENT_FOLLOW_UP_CHARGE].toString()) : null)
                .doctorSpecializationResponseDTOS(parseToDoctorSpecialization(results))
                .doctorQualificationResponseDTOS(parseToDoctorQualification(results))
                .fileUri((results[FILE_URI_INDEX] != null) ?
                        results[FILE_URI_INDEX].toString() : null)
                .build();
    }

    private static List<DoctorSpecializationResponseDTO> parseToDoctorSpecialization(Object[] results) {
        final int DOCTOR_SPECIALIZATION_ID_INDEX = 11;
        final int SPECIALIZATION_ID_INDEX = 12;
        final int SPECIALIZATION_NAME_INDEX = 13;

        String[] doctorSpecializationIds = results[DOCTOR_SPECIALIZATION_ID_INDEX].toString().split(COMMA_SEPARATED);
        String[] specializationIds = results[SPECIALIZATION_ID_INDEX].toString().split(COMMA_SEPARATED);
        String[] specializationNames = results[SPECIALIZATION_NAME_INDEX].toString().split(COMMA_SEPARATED);

        return IntStream.range(0, doctorSpecializationIds.length)
                .mapToObj(i -> DoctorSpecializationResponseDTO.builder()
                        .doctorSpecializationId(Long.parseLong(doctorSpecializationIds[i]))
                        .specializationId(Long.parseLong(specializationIds[i]))
                        .specializationName(specializationNames[i])
                        .build())
                .collect(Collectors.toList());
    }

    private static List<DoctorQualificationResponseDTO> parseToDoctorQualification(Object[] results) {

        final int DOCTOR_QUALIFICATION_ID_INDEX = 14;
        final int QUALIFICATION_ID_INDEX = 15;
        final int QUALIFICATION_NAME_INDEX = 16;

        String[] doctorQualificationIds = results[DOCTOR_QUALIFICATION_ID_INDEX].toString().split(COMMA_SEPARATED);
        String[] qualificationIds = results[QUALIFICATION_ID_INDEX].toString().split(COMMA_SEPARATED);
        String[] qualificationNames = results[QUALIFICATION_NAME_INDEX].toString().split(COMMA_SEPARATED);

        return IntStream.range(0, doctorQualificationIds.length)
                .mapToObj(i -> DoctorQualificationResponseDTO.builder()
                        .doctorQualificationId(Long.parseLong(doctorQualificationIds[i]))
                        .qualificationId(Long.parseLong(qualificationIds[i]))
                        .qualificationName(qualificationNames[i])
                        .build())
                .collect(Collectors.toList());
    }


    public static DoctorRevenueResponseListDTO parseTodoctorRevenueResponseListDTO(List<Object[]> results) {

        DoctorRevenueResponseListDTO doctorRevenueResponseListDTO = new DoctorRevenueResponseListDTO();

        List<DoctorRevenueResponseDTO> doctorRevenueResponseDTOS = new ArrayList<>();

        AtomicReference<Double> totalRefundAmount = new AtomicReference<>(0D);
        AtomicReference<Long> totalAppointmentCount = new AtomicReference<>(0L);

        results.forEach(result -> {
            final int DOCTOR_ID_INDEX = 0;
            final int DOCTOR_NAME_INDEX = 1;
            final int FILE_URI_INDEX = 2;
            final int SPECIALIZATION_NAME_INDEX = 3;
            final int TOTAL_APPOINTMENT_COUNT_INDEX = 4;
            final int REVENUE_AMOUNT_INDEX = 5;

            Double refundAmount = Objects.isNull(result[REVENUE_AMOUNT_INDEX]) ?
                    0D : Double.parseDouble(result[REVENUE_AMOUNT_INDEX].toString());

            Long appointmentCount = Objects.isNull(result[TOTAL_APPOINTMENT_COUNT_INDEX]) ?
                    0L : Long.parseLong(result[TOTAL_APPOINTMENT_COUNT_INDEX].toString());

            DoctorRevenueResponseDTO doctorRevenueResponseDTO =
                    DoctorRevenueResponseDTO.builder()
                            .doctorId(Long.parseLong(result[DOCTOR_ID_INDEX].toString()))
                            .doctorName(result[DOCTOR_NAME_INDEX].toString())
                            .fileUri(result[FILE_URI_INDEX].toString())
                            .specialization(result[SPECIALIZATION_NAME_INDEX].toString())
                            .totalAppointmentCount(Long.parseLong(result[TOTAL_APPOINTMENT_COUNT_INDEX].toString()))
                            .revenueAmount(refundAmount)
                            .build();

            doctorRevenueResponseDTOS.add(doctorRevenueResponseDTO);

            totalRefundAmount.updateAndGet(v -> v + refundAmount);
//            totalAppointmentCount.updateAndGet(v -> v + appointmentCount);

        });

        doctorRevenueResponseListDTO.setDoctorRevenueResponseDTOList(doctorRevenueResponseDTOS);
        doctorRevenueResponseListDTO.setTotalItems(doctorRevenueResponseDTOS.size());

        doctorRevenueResponseListDTO.setTotalRevenueAmount(totalRefundAmount.get());
//        doctorRevenueResponseListDTO.setOverallAppointmentCount(totalAppointmentCount.get());

        return doctorRevenueResponseListDTO;
    }


}
