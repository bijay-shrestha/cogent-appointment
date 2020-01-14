package com.cogent.cogentappointment.service.impl;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.doctor.*;
import com.cogent.cogentappointment.dto.response.doctor.DoctorDetailResponseDTO;
import com.cogent.cogentappointment.dto.response.doctor.DoctorMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.doctor.DoctorUpdateResponseDTO;
import com.cogent.cogentappointment.enums.Gender;
import com.cogent.cogentappointment.exception.DataDuplicationException;
import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.model.*;
import com.cogent.cogentappointment.repository.DoctorAppointmentChargeRepository;
import com.cogent.cogentappointment.repository.DoctorQualificationRepository;
import com.cogent.cogentappointment.repository.DoctorRepository;
import com.cogent.cogentappointment.repository.DoctorSpecializationRepository;
import com.cogent.cogentappointment.service.DoctorService;
import com.cogent.cogentappointment.service.HospitalService;
import com.cogent.cogentappointment.service.QualificationService;
import com.cogent.cogentappointment.service.SpecializationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.constants.ErrorMessageConstants.NAME_AND_MOBILE_NUMBER_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.log.constants.DoctorLog.*;
import static com.cogent.cogentappointment.utils.DoctorUtils.*;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.utils.commons.GenderUtils.fetchGenderByCode;

/**
 * @author smriti on 2019-09-29
 */
@Service
@Transactional
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    private final DoctorSpecializationRepository doctorSpecializationRepository;

    private final SpecializationService specializationService;

    private final QualificationService qualificationService;

    private final DoctorQualificationRepository doctorQualificationRepository;

    private final HospitalService hospitalService;

    private final DoctorAppointmentChargeRepository doctorAppointmentChargeRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             DoctorSpecializationRepository doctorSpecializationRepository,
                             SpecializationService specializationService,
                             QualificationService qualificationService,
                             DoctorQualificationRepository doctorQualificationRepository,
                             HospitalService hospitalService,
                             DoctorAppointmentChargeRepository doctorAppointmentChargeRepository) {
        this.doctorRepository = doctorRepository;
        this.doctorSpecializationRepository = doctorSpecializationRepository;
        this.specializationService = specializationService;
        this.qualificationService = qualificationService;
        this.doctorQualificationRepository = doctorQualificationRepository;
        this.hospitalService = hospitalService;
        this.doctorAppointmentChargeRepository = doctorAppointmentChargeRepository;
    }

    @Override
    public String save(DoctorRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR);

        Long doctorCount = doctorRepository.validateDoctorDuplicity(requestDTO.getName(), requestDTO.getMobileNumber());

        validateDoctor(doctorCount, requestDTO.getName(), requestDTO.getMobileNumber());

        Doctor doctor = parseDTOToDoctor(requestDTO,
                fetchGender(requestDTO.getGenderCode()),
                fetchHospitalById(requestDTO.getHospitalId()));

        saveDoctor(doctor);

        saveDoctorAppointmentCharge(doctor, requestDTO.getAppointmentCharge());

        saveDoctorSpecialization(doctor.getId(), requestDTO.getSpecializationIds());

        saveDoctorQualifications(doctor.getId(), requestDTO.getQualificationIds());

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return doctor.getCode();
    }

    @Override
    public void update(DoctorUpdateRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR);

        Doctor doctor = findById(requestDTO.getUpdateDTO().getId());

        Long doctorCount = doctorRepository.validateDoctorDuplicityForUpdate(
                requestDTO.getUpdateDTO().getId(),
                requestDTO.getUpdateDTO().getName(),
                requestDTO.getUpdateDTO().getMobileNumber());

        validateDoctor(doctorCount,
                requestDTO.getUpdateDTO().getName(),
                requestDTO.getUpdateDTO().getMobileNumber());

        convertToUpdatedDoctor(
                requestDTO.getUpdateDTO(),
                doctor,
                fetchGender(requestDTO.getUpdateDTO().getGenderCode()),
                fetchHospitalById(requestDTO.getUpdateDTO().getHospitalId()));

        updateDoctorAppointmentCharge(doctor.getId(), requestDTO.getUpdateDTO().getAppointmentCharge());

        updateDoctorSpecialization(doctor.getId(), requestDTO.getSpecializationUpdateRequestDTOS());

        updateDoctorQualification(doctor.getId(), requestDTO.getDoctorQualificationUpdateDTOS());

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, DOCTOR);

        Doctor doctor = findById(deleteRequestDTO.getId());

        convertToDeletedDoctor(doctor, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DoctorMinimalResponseDTO> search(DoctorSearchRequestDTO searchRequestDTO,
                                                 Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, DOCTOR);

        List<DoctorMinimalResponseDTO> responseDTOS = doctorRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchDoctorForDropdown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DOCTOR);

        List<DropDownResponseDTO> responseDTOS = doctorRepository.fetchDoctorForDropdown();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public DoctorDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, DOCTOR);

        DoctorDetailResponseDTO responseDTO = doctorRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public DoctorUpdateResponseDTO fetchDetailsForUpdate(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, DOCTOR);

        DoctorUpdateResponseDTO responseDTO = doctorRepository.fetchDetailsForUpdate(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }


    @Override
    public Doctor fetchDoctorById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR);

        Doctor doctor = doctorRepository.findActiveDoctorById(id)
                .orElseThrow(() -> new NoContentFoundException(Doctor.class, "id", id.toString()));

        log.info(FETCHING_PROCESS_COMPLETED, doctor, getDifferenceBetweenTwoTime(startTime));

        return doctor;
    }

    @Override
    public List<DropDownResponseDTO> fetchDoctorBySpecializationId(Long specializationId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DOCTOR);

        List<DropDownResponseDTO> responseDTOS =
                doctorRepository.fetchDoctorBySpecializationId(specializationId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    private Gender fetchGender(Character genderCode) {
        return fetchGenderByCode(genderCode);
    }

    private Hospital fetchHospitalById(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    private void findSpecializationById(Long specializationId) {
        specializationService.fetchActiveSpecializationById(specializationId);
    }

    private void fetchQualificationById(Long qualificationId) {
        qualificationService.fetchQualificationById(qualificationId);
    }

    private void saveDoctorSpecialization(Long doctorId, List<Long> specializationIds) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR_SPECIALIZATION);

        List<DoctorSpecialization> doctorSpecializations = specializationIds.stream()
                .map(specializationId -> {
                    /*VALIDATE IF THE SPECIALIZATION IS ACTIVE*/
                    findSpecializationById(specializationId);
                    return parseToDoctorSpecialization(doctorId, specializationId);
                }).collect(Collectors.toList());

        saveDoctorSpecialization(doctorSpecializations);

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR_SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveDoctorQualifications(Long doctorId, List<Long> qualificationIds) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR_QUALIFICATION);

        List<DoctorQualification> doctorQualifications = qualificationIds.stream()
                .map(qualificationId -> {
                    /*VALIDATE IF QUALIFICATION IS ACTIVE*/
                    fetchQualificationById(qualificationId);
                    return parseToDoctorQualification(doctorId, qualificationId);
                }).collect(Collectors.toList());

        saveDoctorQualification(doctorQualifications);

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR_QUALIFICATION, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveDoctorAppointmentCharge(Doctor doctor, Double appointmentCharge) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR_APPOINTMENT_CHARGE);

        DoctorAppointmentCharge doctorAppointmentCharge = parseToDoctorAppointmentCharge(doctor, appointmentCharge);

        doctorAppointmentChargeRepository.save(doctorAppointmentCharge);

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR_APPOINTMENT_CHARGE, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorSpecialization(Long doctorId,
                                            List<DoctorSpecializationUpdateDTO> specializationUpdateRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR_SPECIALIZATION);

        List<DoctorSpecialization> doctorSpecializations = specializationUpdateRequestDTO.stream()
                .map(requestDTO -> {
                    /*VALIDATE IF THE SPECIALIZATION IS ACTIVE*/
                    findSpecializationById(requestDTO.getSpecializationId());

                    return parseToUpdatedDoctorSpecialization(doctorId, requestDTO);
                }).collect(Collectors.toList());

        saveDoctorSpecialization(doctorSpecializations);

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR_SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorQualification(Long doctorId,
                                           List<DoctorQualificationUpdateDTO> doctorQualificationUpdateDTOS) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR_QUALIFICATION);

        List<DoctorQualification> doctorQualifications = doctorQualificationUpdateDTOS.stream()
                .map(requestDTO -> {
                    /*VALIDATE IF QUALIFICATION IS ACTIVE*/
                    fetchQualificationById(requestDTO.getQualificationId());
                    return parseToUpdatedDoctorQualification(doctorId, requestDTO);
                }).collect(Collectors.toList());

        saveDoctorQualification(doctorQualifications);

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR_QUALIFICATION, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorAppointmentCharge(Long doctorId,
                                               Double appointmentCharge) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR_APPOINTMENT_CHARGE);

        DoctorAppointmentCharge doctorAppointmentCharge =
                doctorAppointmentChargeRepository.findByDoctorId(doctorId)
                        .orElseThrow(() -> new NoContentFoundException(DoctorAppointmentCharge.class));

        updateAppointmentCharge(doctorAppointmentCharge, appointmentCharge);

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR_APPOINTMENT_CHARGE, getDifferenceBetweenTwoTime(startTime));
    }

    private void validateDoctor(Long doctorCount, String name, String mobileNumber) {

        if (doctorCount.intValue() > 0)
            throw new DataDuplicationException(
                    String.format(NAME_AND_MOBILE_NUMBER_DUPLICATION_MESSAGE, Doctor.class.getSimpleName(), name, mobileNumber),
                    "name", name, "mobileNumber", mobileNumber
            );
    }

    private void saveDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    private void saveDoctorSpecialization(List<DoctorSpecialization> doctorSpecialization) {
        doctorSpecializationRepository.saveAll(doctorSpecialization);
    }

    private void saveDoctorQualification(List<DoctorQualification> doctorQualifications) {
        doctorQualificationRepository.saveAll(doctorQualifications);
    }

    public Doctor findById(Long doctorId) {
        return doctorRepository.findDoctorById(doctorId)
                .orElseThrow(() -> DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(doctorId));
    }

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Doctor.class, "id", id.toString());
    };
}


