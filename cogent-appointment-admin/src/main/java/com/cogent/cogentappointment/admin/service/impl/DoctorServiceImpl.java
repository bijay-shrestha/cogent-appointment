package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.*;
import com.cogent.cogentappointment.admin.dto.response.doctor.*;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.*;
import com.cogent.cogentappointment.admin.utils.GenderUtils;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.NAME_AND_MOBILE_NUMBER_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.*;
import static com.cogent.cogentappointment.admin.constants.StringConstant.SPACE;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.DoctorLog.*;
import static com.cogent.cogentappointment.admin.utils.DoctorUtils.*;
import static com.cogent.cogentappointment.admin.utils.SalutationUtils.parseToDoctorSalutation;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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

    private final MinioFileService minioFileService;

    private final DoctorAvatarRepository doctorAvatarRepository;

    private final SalutationRepository salutationRepository;

    private final DoctorSalutationRepository doctorSalutationRepository;

    private final Validator validator;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             DoctorSpecializationRepository doctorSpecializationRepository,
                             SpecializationService specializationService,
                             QualificationService qualificationService,
                             DoctorQualificationRepository doctorQualificationRepository,
                             HospitalService hospitalService,
                             DoctorAppointmentChargeRepository doctorAppointmentChargeRepository,
                             MinioFileService minioFileService,
                             DoctorAvatarRepository doctorAvatarRepository,
                             SalutationRepository salutationRepository,
                             DoctorSalutationRepository doctorSalutationRepository,
                             Validator validator) {
        this.doctorRepository = doctorRepository;
        this.doctorSpecializationRepository = doctorSpecializationRepository;
        this.specializationService = specializationService;
        this.qualificationService = qualificationService;
        this.doctorQualificationRepository = doctorQualificationRepository;
        this.hospitalService = hospitalService;
        this.doctorAppointmentChargeRepository = doctorAppointmentChargeRepository;
        this.minioFileService = minioFileService;
        this.doctorAvatarRepository = doctorAvatarRepository;
        this.salutationRepository = salutationRepository;
        this.doctorSalutationRepository = doctorSalutationRepository;
        this.validator = validator;
    }

    @Override
    public String save(DoctorRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR);

        Long doctorCount = doctorRepository.validateDoctorDuplicity(
                requestDTO.getName(), requestDTO.getMobileNumber(), requestDTO.getHospitalId());

        validateDoctor(doctorCount, requestDTO.getName(), requestDTO.getMobileNumber());

        Doctor doctor = parseDTOToDoctor(requestDTO,
                fetchGender(requestDTO.getGenderCode()),
                fetchHospitalById(requestDTO.getHospitalId()));

        saveDoctor(doctor);

        if (requestDTO.getSalutationIds().size() > 0) {
            String salutations = findDoctorSalutation(requestDTO.getSalutationIds());

            doctor.setSalutation(salutations);

            saveDoctorSalutation(doctor.getId(), requestDTO.getSalutationIds());
        }

        saveDoctorAppointmentCharge(doctor,
                requestDTO.getAppointmentCharge(),
                requestDTO.getAppointmentFollowUpCharge());

        saveDoctorSpecialization(doctor.getId(), requestDTO.getSpecializationIds());

        saveDoctorQualifications(doctor.getId(), requestDTO.getQualificationIds());

        saveDoctorAvatar(doctor, requestDTO.getAvatar());

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return doctor.getCode();
    }

    @Override
    public void update(DoctorUpdateRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR);

        Doctor doctor = findById(requestDTO.getDoctorInfo().getId());

        Long doctorCount = doctorRepository.validateDoctorDuplicityForUpdate(
                requestDTO.getDoctorInfo().getId(),
                requestDTO.getDoctorInfo().getName(),
                requestDTO.getDoctorInfo().getMobileNumber(),
                requestDTO.getDoctorInfo().getHospitalId());

        validateDoctor(doctorCount,
                requestDTO.getDoctorInfo().getName(),
                requestDTO.getDoctorInfo().getMobileNumber());

        if (requestDTO.getDoctorSalutationInfo().size() > 0) {
            String doctorSalutations = updateDoctorSalutations(requestDTO.getDoctorSalutationInfo(), doctor);
            doctor.setSalutation(doctorSalutations);
        }

        convertToUpdatedDoctor(
                requestDTO.getDoctorInfo(),
                doctor,
                fetchGender(requestDTO.getDoctorInfo().getGenderCode()),
                fetchHospitalById(requestDTO.getDoctorInfo().getHospitalId())
        );

        updateDoctorAppointmentCharge(
                doctor.getId(),
                requestDTO.getDoctorInfo().getAppointmentCharge(),
                requestDTO.getDoctorInfo().getAppointmentFollowUpCharge()
        );

        updateDoctorSpecialization(doctor.getId(), requestDTO.getDoctorSpecializationInfo());

        updateDoctorQualification(doctor.getId(), requestDTO.getDoctorQualificationInfo());

        if (requestDTO.getDoctorInfo().getIsAvatarUpdate().equals(YES))
            updateDoctorAvatar(doctor, requestDTO.getDoctorInfo().getAvatar());

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));
    }

    private String updateDoctorSalutations(List<DoctorSalutationUpdateDTO> updateDTOS, Doctor doctor) {

        List<String> salutationList = new ArrayList<>();
        if (doctor.getSalutation() != null) {
            salutationList.addAll(Arrays.asList(doctor.getSalutation().split("\\s+")));
        }

        List<DoctorSalutation> doctorSalutationListToUpdate = new ArrayList<>();

        updateDTOS.forEach(result -> {

            if (result.getDoctorSalutationId() == null) {

                Salutation salutation = findActiveSalutation(result.getSalutationId());
                doctorSalutationRepository.save(parseToDoctorSalutation(doctor, salutation));
                salutationList.add(salutation.getCode());

            } else {

                DoctorSalutation doctorSalutation = doctorSalutationRepository.
                        findDoctorSalutationById(result.getDoctorSalutationId())
                        .orElse(null);

                Salutation salutation = findActiveSalutation(doctorSalutation.getSalutationId());


                if (result.getStatus().equals(INACTIVE)) {
                    salutationList.remove(salutation.getCode());
                    doctorSalutation.setStatus(INACTIVE);

                }

                if (result.getStatus().equals(ACTIVE) && !salutationList.contains(salutation.getCode())) {
                    salutationList.add(salutation.getCode());
                    doctorSalutation.setStatus(ACTIVE);


                }

                doctorSalutationListToUpdate.add(doctorSalutation);

            }

        });

        if (salutationList.size() == 0) {
            return null;
        }

        if (salutationList.size() == 1) {
            return salutationList.stream()
                    .collect(Collectors.joining());
        }

        return salutationList.stream()
                .collect(Collectors.joining(SPACE));
    }


    private Salutation findActiveSalutation(Long salutationId) {

        return salutationRepository.fetchActiveSalutationById(salutationId)
                .orElseThrow(() -> new NoContentFoundException(Salutation.class));
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
    public List<DoctorDropdownDTO> fetchDoctorForDropdown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DOCTOR);

        List<DoctorDropdownDTO> responseDTOS = doctorRepository.fetchDoctorForDropdown();

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

        List<DoctorSalutationResponseDTO> salutationResponseDTOList = doctorSalutationRepository.fetchDoctorSalutationByDoctorId(id);
        responseDTO.setDoctorSalutationResponseDTOS(salutationResponseDTOList);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }


    @Override
    public Doctor fetchDoctorById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR);

        Doctor doctor = doctorRepository.findActiveDoctorById(id)
                .orElseThrow(() -> DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, doctor, getDifferenceBetweenTwoTime(startTime));

        return doctor;
    }

    @Override
    public List<DoctorDropdownDTO> fetchDoctorBySpecializationId(Long specializationId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DOCTOR);

        List<DoctorDropdownDTO> responseDTOS =
                doctorRepository.fetchDoctorBySpecializationId(specializationId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DoctorDropdownDTO> fetchDoctorByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DOCTOR);

        List<DoctorDropdownDTO> responseDTOS =
                doctorRepository.fetchDoctorByHospitalId(hospitalId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DoctorDropdownDTO> fetchMinDoctorByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DOCTOR);

        List<DoctorDropdownDTO> responseDTOS =
                doctorRepository.fetchMinDoctorByHospitalId(hospitalId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }


    private Gender fetchGender(Character genderCode) {
        return GenderUtils.fetchGenderByCode(genderCode);
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

    private void saveDoctorAvatar(Doctor doctor, String avatar) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR_AVATAR);

        if (!Objects.isNull(avatar))
            saveDoctorAvatar(convertFileToDoctorAvatar(new DoctorAvatar(), avatar, doctor));

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR_AVATAR, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveDoctorAppointmentCharge(Doctor doctor, Double appointmentCharge, Double appointmentFollowUpCharge) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR_APPOINTMENT_CHARGE);

        DoctorAppointmentCharge doctorAppointmentCharge =
                parseToDoctorAppointmentCharge(doctor, appointmentCharge, appointmentFollowUpCharge);

        doctorAppointmentChargeRepository.save(doctorAppointmentCharge);

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR_APPOINTMENT_CHARGE, getDifferenceBetweenTwoTime(startTime));
    }

//    private void saveDoctorSalutation(Long doctorId, List<Long> salutationIds) {
//
//        Long startTime = getTimeInMillisecondsFromLocalDate();
//
//        log.info(SAVING_PROCESS_STARTED, DOCTOR_SALUTATION);
//
////        List<DoctorSalutation> doctorSalutations = salutationIds.stream()
////                .map(salutationId -> {
////                    fetchSalutationById(salutationId);
////                    return parseToDoctorSalutation(doctorId, salutationId);
////                }).collect(Collectors.toList());
//
//        List<DoctorSalutation> doctorSalutations = salutationIds.stream()
//                .map(salutationId -> {
//                    fetchSalutationById(salutationId);
//                    return parseToDoctorSalutation(doctorId, salutationId);
//                }).collect(Collectors.toList());
//
//
//        log.info(SAVING_PROCESS_COMPLETED, DOCTOR_SALUTATION, getDifferenceBetweenTwoTime(startTime));
//
//    }


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
                                               Double appointmentCharge,
                                               Double appointmentFollowUpCharge) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR_APPOINTMENT_CHARGE);

        DoctorAppointmentCharge doctorAppointmentCharge =
                doctorAppointmentChargeRepository.findByDoctorId(doctorId)
                        .orElseThrow(() -> DOCTOR_APPOINTMENT_CHARGE_WITH_GIVEN_DOCTOR_ID_NOT_FOUND.apply(doctorId));

        parseDoctorAppointmentChargeDetails(doctorAppointmentCharge, appointmentCharge, appointmentFollowUpCharge);

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR_APPOINTMENT_CHARGE, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorAvatar(Doctor doctor, String avatar) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR_AVATAR);

        DoctorAvatar doctorAvatar = doctorAvatarRepository.findByDoctorId(doctor.getId());

        if (Objects.isNull(doctorAvatar)) saveDoctorAvatar(doctor, avatar);
        else updateDoctorAvatar(doctor, doctorAvatar, avatar);

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR_AVATAR, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorAvatar(Doctor doctor,
                                    DoctorAvatar doctorAvatar,
                                    String avatar) {

        if (!Objects.isNull(avatar)) {
            convertFileToDoctorAvatar(doctorAvatar, avatar, doctor);
        } else doctorAvatar.setStatus(INACTIVE);

        saveDoctorAvatar(doctorAvatar);
    }

    private void validateDoctor(Long doctorCount, String name, String mobileNumber) {

        if (doctorCount.intValue() > 0) {
            log.error(NAME_AND_MOBILE_NUMBER_DUPLICATION_ERROR, DOCTOR, name, mobileNumber);
            throw new DataDuplicationException(
                    String.format(NAME_AND_MOBILE_NUMBER_DUPLICATION_MESSAGE, Doctor.class.getSimpleName(), name, mobileNumber),
                    "name", name, "mobileNumber", mobileNumber
            );
        }
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

    private void saveDoctorAvatar(DoctorAvatar doctorAvatar) {
        doctorAvatarRepository.save(doctorAvatar);
    }

    private Doctor findById(Long doctorId) {
        return doctorRepository.findDoctorById(doctorId)
                .orElseThrow(() -> DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(doctorId));
    }

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, DOCTOR, id);
        throw new NoContentFoundException(Doctor.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> DOCTOR_APPOINTMENT_CHARGE_WITH_GIVEN_DOCTOR_ID_NOT_FOUND = (doctorId) -> {
        log.error(DOCTOR_APPOINTMENT_CHARGE_NOT_FOUND, DoctorAppointmentCharge.class.getSimpleName(), doctorId);
        throw new NoContentFoundException(DoctorAppointmentCharge.class, "doctorId", doctorId.toString());
    };

    private void saveDoctorSalutation(Long doctorId, List<Long> salutationIds) {

        doctorSalutationRepository.saveAll(parseToDoctorSalutation(doctorId, salutationIds));

    }

    private String findDoctorSalutation(List<Long> salutationIds) {

        String salutations = "";
        if (salutationIds.size() > 0) {
            List<Salutation> salutationList = findActiveSalutations(salutationIds);
            if (salutationList.size() == 1) {
                salutations = salutationList.stream()
                        .map(Salutation::getCode).collect(Collectors.joining());
            }

            if (salutationList.size() > 1) {
                salutations = salutationList.stream()
                        .map(Salutation::getCode)
                        .collect(Collectors.joining(" "));
            }
        }

        return salutations;
    }

    private List<Salutation> findActiveSalutations(List<Long> salutationIds) {

        String ids = salutationIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        List<Salutation> salutationList = validateSalutations(ids);
        int requestCount = salutationIds.size();

        if ((salutationList.size()) != requestCount) {
            throw new NoContentFoundException(Salutation.class);
        }

        return salutationList;
    }

    private List<Salutation> validateSalutations(String ids) {
        return salutationRepository.validateSalutationCount(ids);
    }

}


