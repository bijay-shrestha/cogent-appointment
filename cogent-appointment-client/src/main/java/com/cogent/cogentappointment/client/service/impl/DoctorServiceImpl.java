package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctor.*;
import com.cogent.cogentappointment.client.dto.response.doctor.*;
import com.cogent.cogentappointment.client.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.*;
import com.cogent.cogentappointment.persistence.enums.Gender;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.NAME_AND_MOBILE_NUMBER_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.client.constants.StatusConstants.*;
import static com.cogent.cogentappointment.client.constants.StringConstant.SPACE;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.DoctorLog.*;
import static com.cogent.cogentappointment.client.log.constants.SalutationLog.SALUTATION;
import static com.cogent.cogentappointment.client.utils.DoctorUtils.*;
import static com.cogent.cogentappointment.client.utils.GenderUtils.fetchGenderByCode;
import static com.cogent.cogentappointment.client.utils.SalutationUtils.parseToDoctorSalutation;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author smriti on 2019-09-29
 */
@Service
@Transactional
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    private final DoctorSalutationRepository doctorSalutationRepository;

    private final DoctorSpecializationRepository doctorSpecializationRepository;

    private final SpecializationService specializationService;

    private final QualificationService qualificationService;

    private final DoctorQualificationRepository doctorQualificationRepository;

    private final HospitalService hospitalService;

    private final DoctorAppointmentChargeRepository doctorAppointmentChargeRepository;

    private final MinioFileService minioFileService;

    private final DoctorAvatarRepository doctorAvatarRepository;

    private final SalutationRepository salutationRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             DoctorSalutationRepository doctorSalutationRepository, DoctorSpecializationRepository doctorSpecializationRepository,
                             SpecializationService specializationService,
                             QualificationService qualificationService,
                             DoctorQualificationRepository doctorQualificationRepository,
                             HospitalService hospitalService,
                             DoctorAppointmentChargeRepository doctorAppointmentChargeRepository,
                             MinioFileService minioFileService,
                             DoctorAvatarRepository doctorAvatarRepository, SalutationRepository salutationRepository) {
        this.doctorRepository = doctorRepository;
        this.doctorSalutationRepository = doctorSalutationRepository;
        this.doctorSpecializationRepository = doctorSpecializationRepository;
        this.specializationService = specializationService;
        this.qualificationService = qualificationService;
        this.doctorQualificationRepository = doctorQualificationRepository;
        this.hospitalService = hospitalService;
        this.doctorAppointmentChargeRepository = doctorAppointmentChargeRepository;
        this.minioFileService = minioFileService;
        this.doctorAvatarRepository = doctorAvatarRepository;
        this.salutationRepository = salutationRepository;
    }

    @Override
    public String save(DoctorRequestDTO requestDTO, MultipartFile avatar) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR);

        Long hospitalId = getLoggedInHospitalId();

        Long doctorCount = doctorRepository.validateDoctorDuplicity(
                requestDTO.getName(), requestDTO.getMobileNumber(), hospitalId);

        validateDoctor(doctorCount, requestDTO.getName(), requestDTO.getMobileNumber());

        Doctor doctor = parseDTOToDoctor(requestDTO,
                fetchGender(requestDTO.getGenderCode()),
                fetchHospitalById(hospitalId));


        saveDoctor(doctor);

        if (requestDTO.getSalutationIds().size() > 0) {
            String salutations = findDoctorSalutation(requestDTO.getSalutationIds());
            doctor.setSalutation(salutations);

            saveDoctorSalutation(doctor.getId(), requestDTO.getSalutationIds());
        }

        saveDoctorAppointmentCharge(doctor, requestDTO.getAppointmentCharge(), requestDTO.getAppointmentFollowUpCharge());

        saveDoctorSpecialization(doctor.getId(), requestDTO.getSpecializationIds(), hospitalId);

        saveDoctorQualifications(doctor.getId(), requestDTO.getQualificationIds());

        saveDoctorAvatar(doctor, avatar);

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return doctor.getCode();
    }

    private void saveDoctorSalutation(Long doctorId, List<Long> salutationIds) {

        doctorSalutationRepository.saveAll(parseToDoctorSalutation(doctorId, salutationIds));

    }


    @Override
    public void update(DoctorUpdateRequestDTO requestDTO, MultipartFile avatar) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR);

        Long hospitalId = getLoggedInHospitalId();

        Doctor doctor = findByIdAndHospitalId(requestDTO.getDoctorInfo().getId(), hospitalId);

        Long doctorCount = doctorRepository.validateDoctorDuplicityForUpdate(
                requestDTO.getDoctorInfo().getId(),
                requestDTO.getDoctorInfo().getName(),
                requestDTO.getDoctorInfo().getMobileNumber(),
                hospitalId);

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
                fetchGender(requestDTO.getDoctorInfo().getGenderCode()));

        updateDoctorAppointmentCharge(
                doctor.getId(),
                requestDTO.getDoctorInfo().getAppointmentCharge(),
                requestDTO.getDoctorInfo().getAppointmentFollowUpCharge()
        );

        updateDoctorSpecialization(doctor.getId(), requestDTO.getDoctorSpecializationInfo(), hospitalId);

        updateDoctorQualification(doctor.getId(), requestDTO.getDoctorQualificationInfo());

        if (requestDTO.getDoctorInfo().getIsAvatarUpdate().equals(YES))
            updateDoctorAvatar(doctor, avatar);

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

                DoctorSalutation doctorSalutation = doctorSalutationRepository.findDoctorSalutationById(result.getDoctorSalutationId())
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

    private List<DoctorSalutation> validateDoctorSalutations(String ids) {
        return doctorSalutationRepository.validateDoctorSalutationCount(ids);
    }

    private void updateDoctorSalutation(List<DoctorSalutation> doctorSalutationListToUpdate) {
        doctorSalutationRepository.saveAll(doctorSalutationListToUpdate);
    }


    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, DOCTOR);

        Doctor doctor = findByIdAndHospitalId(deleteRequestDTO.getId(), getLoggedInHospitalId());

        convertToDeletedDoctor(doctor, deleteRequestDTO);

        log.info(DELETING_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DoctorMinimalResponseDTO> search(DoctorSearchRequestDTO searchRequestDTO,
                                                 Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, DOCTOR);

        List<DoctorMinimalResponseDTO> responseDTOS =
                doctorRepository.search(searchRequestDTO, getLoggedInHospitalId(), pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DoctorDropdownDTO> fetchActiveMinDoctor() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DOCTOR);

        List<DoctorDropdownDTO> responseDTOS = doctorRepository.fetchActiveMinDoctor(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DoctorDropdownDTO> fetchMinDoctor() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DOCTOR);

        List<DoctorDropdownDTO> responseDTOS = doctorRepository.fetchMinDoctorByHospitalId(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public DoctorDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, DOCTOR);

        DoctorDetailResponseDTO responseDTO = doctorRepository.fetchDetailsById(id, getLoggedInHospitalId());

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public DoctorUpdateResponseDTO fetchDetailsForUpdate(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, DOCTOR);

        DoctorUpdateResponseDTO responseDTO = doctorRepository.fetchDetailsForUpdate(id, getLoggedInHospitalId());

        List<DoctorSalutationResponseDTO> salutationResponseDTOList = doctorSalutationRepository.fetchDoctorSalutationByDoctorId(id);
        responseDTO.setDoctorSalutationResponseDTOS(salutationResponseDTOList);


        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public Double fetchDoctorAppointmentCharge(Long doctorId, Long hospitalId) {
        return doctorRepository.fetchDoctorAppointmentCharge(doctorId, hospitalId);
    }

    @Override
    public Double fetchDoctorFollowupAppointmentCharge(Long doctorId, Long hospitalId) {
        return doctorRepository.fetchDoctorAppointmentFollowUpCharge(doctorId, hospitalId);
    }

    @Override
    public Doctor fetchActiveDoctorByIdAndHospitalId(Long id, Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR);

        Doctor doctor = doctorRepository.fetchActiveDoctorByIdAndHospitalId(id, hospitalId)
                .orElseThrow(() -> DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return doctor;
    }

    @Override
    public List<DoctorDropdownDTO> fetchDoctorBySpecializationId(Long specializationId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DOCTOR);

        List<DoctorDropdownDTO> responseDTOS =
                doctorRepository.fetchDoctorBySpecializationAndHospitalId(specializationId, getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DoctorDropdownDTO> fetchDoctorByHospitalId() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DOCTOR);

        List<DoctorDropdownDTO> responseDTOS =
                doctorRepository.fetchDoctorByHospitalId(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    private String findDoctorSalutation(List<Long> salutationIds) {

        String salutations = "";
        if (salutationIds.size() > 0) {
            List<Salutation> salutationList = findActiveSalutations(salutationIds);
            if (salutationList.size() == 1) {
                salutations = salutationList.stream()
                        .map(request -> request.getCode()).collect(Collectors.joining());
            }

            if (salutationList.size() > 1) {
                salutations = salutationList.stream()
                        .map(request -> request.getCode())
                        .collect(Collectors.joining(" "));
            }
        }

        return salutations;
    }

    private List<Salutation> findActiveSalutations(List<Long> salutationIds) {

        String ids = salutationIds.stream()
                .map(request -> request.toString())
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

    private Gender fetchGender(Character genderCode) {
        return fetchGenderByCode(genderCode);
    }

    private Hospital fetchHospitalById(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    private void findSpecializationById(Long specializationId, Long hospitalId) {
        specializationService.fetchActiveSpecializationByIdAndHospitalId(specializationId, hospitalId);
    }

    private void fetchQualificationById(Long qualificationId) {
        qualificationService.fetchActiveQualificationById(qualificationId);
    }

    private void saveDoctorSpecialization(Long doctorId, List<Long> specializationIds,
                                          Long hospitalId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR_SPECIALIZATION);

        List<DoctorSpecialization> doctorSpecializations = specializationIds.stream()
                .map(specializationId -> {
                    /*VALIDATE IF THE SPECIALIZATION IS ACTIVE*/
                    findSpecializationById(specializationId, hospitalId);
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

    private void saveDoctorAvatar(Doctor doctor, MultipartFile file) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR_AVATAR);

        if (!Objects.isNull(file)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(doctor, new MultipartFile[]{file});
            saveDoctorAvatar(convertFileToDoctorAvatar(responseList.get(0), doctor));
        }

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR_AVATAR, getDifferenceBetweenTwoTime(startTime));
    }

    private List<FileUploadResponseDTO> uploadFiles(Doctor doctor, MultipartFile[] file) {
        String subDirectoryLocation = doctor.getName();

        return minioFileService.addAttachmentIntoSubDirectory(subDirectoryLocation, file);
    }

    private void saveDoctorAppointmentCharge(Doctor doctor, Double appointmentCharge, Double appointmentFollowUpCharge) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DOCTOR_APPOINTMENT_CHARGE);

        DoctorAppointmentCharge doctorAppointmentCharge =
                parseToDoctorAppointmentCharge(doctor, appointmentCharge, appointmentFollowUpCharge);

        doctorAppointmentChargeRepository.save(doctorAppointmentCharge);

        log.info(SAVING_PROCESS_COMPLETED, DOCTOR_APPOINTMENT_CHARGE, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorSpecialization(Long doctorId,
                                            List<DoctorSpecializationUpdateDTO> specializationUpdateRequestDTO,
                                            Long hospitalId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR_SPECIALIZATION);

        List<DoctorSpecialization> doctorSpecializations = specializationUpdateRequestDTO.stream()
                .map(requestDTO -> {
                    /*VALIDATE IF THE SPECIALIZATION IS ACTIVE*/
                    findSpecializationById(requestDTO.getSpecializationId(), hospitalId);

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

    private void updateDoctorAvatar(Doctor doctor, MultipartFile file) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, DOCTOR_AVATAR);

        DoctorAvatar doctorAvatar = doctorAvatarRepository.findByDoctorId(doctor.getId());

        if (Objects.isNull(doctorAvatar)) saveDoctorAvatar(doctor, file);
        else updateDoctorAvatar(doctor, doctorAvatar, file);

        log.info(UPDATING_PROCESS_COMPLETED, DOCTOR_AVATAR, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorAvatar(Doctor doctor,
                                    DoctorAvatar doctorAvatar,
                                    MultipartFile files) {
        if (!Objects.isNull(files)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(doctor, new MultipartFile[]{files});
            setAvatarFileProperties(responseList.get(0), doctorAvatar);
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

    private Doctor findByIdAndHospitalId(Long doctorId, Long hospitalId) {
        return doctorRepository.findDoctorByIdAndHospitalId(doctorId, hospitalId)
                .orElseThrow(() -> DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(doctorId));
    }

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, DOCTOR, id);
        throw new NoContentFoundException(Doctor.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> SALUTATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, SALUTATION, id);
        throw new NoContentFoundException(Salutation.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> DOCTOR_APPOINTMENT_CHARGE_WITH_GIVEN_DOCTOR_ID_NOT_FOUND = (doctorId) -> {
        log.error(DOCTOR_APPOINTMENT_CHARGE_NOT_FOUND, DoctorAppointmentCharge.class.getSimpleName(), doctorId);
        throw new NoContentFoundException(DoctorAppointmentCharge.class, "doctorId", doctorId.toString());
    };
}


