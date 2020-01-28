package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.admin.constants.StatusConstants;
import com.cogent.cogentappointment.admin.constants.StringConstant;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.doctor.*;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorUpdateResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.files.FileUploadResponseDTO;
import com.cogent.cogentappointment.admin.enums.Gender;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.log.CommonLogConstant;
import com.cogent.cogentappointment.admin.log.constants.DoctorLog;
import com.cogent.cogentappointment.admin.model.*;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.*;
import com.cogent.cogentappointment.admin.utils.DoctorUtils;
import com.cogent.cogentappointment.admin.utils.GenderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private final FileService fileService;

    private final DoctorAvatarRepository doctorAvatarRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             DoctorSpecializationRepository doctorSpecializationRepository,
                             SpecializationService specializationService,
                             QualificationService qualificationService,
                             DoctorQualificationRepository doctorQualificationRepository,
                             HospitalService hospitalService,
                             DoctorAppointmentChargeRepository doctorAppointmentChargeRepository,
                             FileService fileService,
                             DoctorAvatarRepository doctorAvatarRepository) {
        this.doctorRepository = doctorRepository;
        this.doctorSpecializationRepository = doctorSpecializationRepository;
        this.specializationService = specializationService;
        this.qualificationService = qualificationService;
        this.doctorQualificationRepository = doctorQualificationRepository;
        this.hospitalService = hospitalService;
        this.doctorAppointmentChargeRepository = doctorAppointmentChargeRepository;
        this.fileService = fileService;
        this.doctorAvatarRepository = doctorAvatarRepository;
    }

    @Override
    public String save(DoctorRequestDTO requestDTO, MultipartFile avatar) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, DoctorLog.DOCTOR);

        Long doctorCount = doctorRepository.validateDoctorDuplicity(requestDTO.getName(), requestDTO.getMobileNumber());

        validateDoctor(doctorCount, requestDTO.getName(), requestDTO.getMobileNumber());

        Doctor doctor = DoctorUtils.parseDTOToDoctor(requestDTO,
                fetchGender(requestDTO.getGenderCode()),
                fetchHospitalById(requestDTO.getHospitalId()));

        saveDoctor(doctor);

        saveDoctorAppointmentCharge(doctor, requestDTO.getAppointmentCharge());

        saveDoctorSpecialization(doctor.getId(), requestDTO.getSpecializationIds());

        saveDoctorQualifications(doctor.getId(), requestDTO.getQualificationIds());

        saveDoctorAvatar(doctor, avatar);

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, DoctorLog.DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return doctor.getCode();
    }

    @Override
    public void update(DoctorUpdateRequestDTO requestDTO, MultipartFile avatar) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, DoctorLog.DOCTOR);

        Doctor doctor = findById(requestDTO.getUpdateDTO().getId());

        Long doctorCount = doctorRepository.validateDoctorDuplicityForUpdate(
                requestDTO.getUpdateDTO().getId(),
                requestDTO.getUpdateDTO().getName(),
                requestDTO.getUpdateDTO().getMobileNumber());

        validateDoctor(doctorCount,
                requestDTO.getUpdateDTO().getName(),
                requestDTO.getUpdateDTO().getMobileNumber());

        DoctorUtils.convertToUpdatedDoctor(
                requestDTO.getUpdateDTO(),
                doctor,
                fetchGender(requestDTO.getUpdateDTO().getGenderCode()),
                fetchHospitalById(requestDTO.getUpdateDTO().getHospitalId()));

        updateDoctorAppointmentCharge(doctor.getId(), requestDTO.getUpdateDTO().getAppointmentCharge());

        updateDoctorSpecialization(doctor.getId(), requestDTO.getSpecializationUpdateRequestDTOS());

        updateDoctorQualification(doctor.getId(), requestDTO.getDoctorQualificationUpdateDTOS());

        updateDoctorAvatar(doctor, avatar);

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, DoctorLog.DOCTOR, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.DELETING_PROCESS_STARTED, DoctorLog.DOCTOR);

        Doctor doctor = findById(deleteRequestDTO.getId());

        DoctorUtils.convertToDeletedDoctor(doctor, deleteRequestDTO);

        log.info(CommonLogConstant.DELETING_PROCESS_COMPLETED, DoctorLog.DOCTOR, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DoctorMinimalResponseDTO> search(DoctorSearchRequestDTO searchRequestDTO,
                                                 Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SEARCHING_PROCESS_STARTED, DoctorLog.DOCTOR);

        List<DoctorMinimalResponseDTO> responseDTOS = doctorRepository.search(searchRequestDTO, pageable);

        log.info(CommonLogConstant.SEARCHING_PROCESS_COMPLETED, DoctorLog.DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DoctorDropdownDTO> fetchDoctorForDropdown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DoctorLog.DOCTOR);

        List<DoctorDropdownDTO> responseDTOS = doctorRepository.fetchDoctorForDropdown();

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DoctorLog.DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public DoctorDetailResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_STARTED, DoctorLog.DOCTOR);

        DoctorDetailResponseDTO responseDTO = doctorRepository.fetchDetailsById(id);

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_COMPLETED, DoctorLog.DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public DoctorUpdateResponseDTO fetchDetailsForUpdate(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_STARTED, DoctorLog.DOCTOR);

        DoctorUpdateResponseDTO responseDTO = doctorRepository.fetchDetailsForUpdate(id);

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_COMPLETED, DoctorLog.DOCTOR, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }


    @Override
    public Doctor fetchDoctorById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED, DoctorLog.DOCTOR);

        Doctor doctor = doctorRepository.findActiveDoctorById(id)
                .orElseThrow(() -> DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(CommonLogConstant.FETCHING_PROCESS_COMPLETED, doctor, getDifferenceBetweenTwoTime(startTime));

        return doctor;
    }

    @Override
    public List<DoctorDropdownDTO> fetchDoctorBySpecializationId(Long specializationId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, DoctorLog.DOCTOR);

        List<DoctorDropdownDTO> responseDTOS =
                doctorRepository.fetchDoctorBySpecializationId(specializationId);

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, DoctorLog.DOCTOR, getDifferenceBetweenTwoTime(startTime));

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

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, DoctorLog.DOCTOR_SPECIALIZATION);

        List<DoctorSpecialization> doctorSpecializations = specializationIds.stream()
                .map(specializationId -> {
                    /*VALIDATE IF THE SPECIALIZATION IS ACTIVE*/
                    findSpecializationById(specializationId);
                    return DoctorUtils.parseToDoctorSpecialization(doctorId, specializationId);
                }).collect(Collectors.toList());

        saveDoctorSpecialization(doctorSpecializations);

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, DoctorLog.DOCTOR_SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveDoctorQualifications(Long doctorId, List<Long> qualificationIds) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, DoctorLog.DOCTOR_QUALIFICATION);

        List<DoctorQualification> doctorQualifications = qualificationIds.stream()
                .map(qualificationId -> {
                    /*VALIDATE IF QUALIFICATION IS ACTIVE*/
                    fetchQualificationById(qualificationId);
                    return DoctorUtils.parseToDoctorQualification(doctorId, qualificationId);
                }).collect(Collectors.toList());

        saveDoctorQualification(doctorQualifications);

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, DoctorLog.DOCTOR_QUALIFICATION, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveDoctorAvatar(Doctor doctor, MultipartFile file) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, DoctorLog.DOCTOR_AVATAR);

        if (!Objects.isNull(file)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(doctor, new MultipartFile[]{file});
            saveDoctorAvatar(DoctorUtils.convertFileToDoctorAvatar(responseList.get(0), doctor));
        }

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, DoctorLog.DOCTOR_AVATAR, getDifferenceBetweenTwoTime(startTime));
    }

    private List<FileUploadResponseDTO> uploadFiles(Doctor Doctor, MultipartFile[] file) {
        String subDirectoryLocation = Doctor.getClass().getSimpleName()
                + StringConstant.FORWARD_SLASH + Doctor.getName() + StringConstant.SPACE + Doctor.getMobileNumber();

        return fileService.uploadFiles(file, subDirectoryLocation);
    }

    private void saveDoctorAppointmentCharge(Doctor doctor, Double appointmentCharge) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, DoctorLog.DOCTOR_APPOINTMENT_CHARGE);

        DoctorAppointmentCharge doctorAppointmentCharge = DoctorUtils.parseToDoctorAppointmentCharge(doctor, appointmentCharge);

        doctorAppointmentChargeRepository.save(doctorAppointmentCharge);

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, DoctorLog.DOCTOR_APPOINTMENT_CHARGE, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorSpecialization(Long doctorId,
                                            List<DoctorSpecializationUpdateDTO> specializationUpdateRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, DoctorLog.DOCTOR_SPECIALIZATION);

        List<DoctorSpecialization> doctorSpecializations = specializationUpdateRequestDTO.stream()
                .map(requestDTO -> {
                    /*VALIDATE IF THE SPECIALIZATION IS ACTIVE*/
                    findSpecializationById(requestDTO.getSpecializationId());

                    return DoctorUtils.parseToUpdatedDoctorSpecialization(doctorId, requestDTO);
                }).collect(Collectors.toList());

        saveDoctorSpecialization(doctorSpecializations);

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, DoctorLog.DOCTOR_SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorQualification(Long doctorId,
                                           List<DoctorQualificationUpdateDTO> doctorQualificationUpdateDTOS) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, DoctorLog.DOCTOR_QUALIFICATION);

        List<DoctorQualification> doctorQualifications = doctorQualificationUpdateDTOS.stream()
                .map(requestDTO -> {
                    /*VALIDATE IF QUALIFICATION IS ACTIVE*/
                    fetchQualificationById(requestDTO.getQualificationId());
                    return DoctorUtils.parseToUpdatedDoctorQualification(doctorId, requestDTO);
                }).collect(Collectors.toList());

        saveDoctorQualification(doctorQualifications);

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, DoctorLog.DOCTOR_QUALIFICATION, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorAppointmentCharge(Long doctorId,
                                               Double appointmentCharge) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, DoctorLog.DOCTOR_APPOINTMENT_CHARGE);

        DoctorAppointmentCharge doctorAppointmentCharge =
                doctorAppointmentChargeRepository.findByDoctorId(doctorId)
                        .orElseThrow(() -> new NoContentFoundException(DoctorAppointmentCharge.class));

        DoctorUtils.updateAppointmentCharge(doctorAppointmentCharge, appointmentCharge);

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, DoctorLog.DOCTOR_APPOINTMENT_CHARGE, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorAvatar(Doctor doctor, MultipartFile file) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, DoctorLog.DOCTOR_AVATAR);

        DoctorAvatar doctorAvatar = doctorAvatarRepository.findByDoctorId(doctor.getId());

        if (Objects.isNull(doctorAvatar)) saveDoctorAvatar(doctor, file);
        else updateDoctorAvatar(doctor, doctorAvatar, file);

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, DoctorLog.DOCTOR_AVATAR, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateDoctorAvatar(Doctor doctor,
                                    DoctorAvatar doctorAvatar,
                                    MultipartFile files) {
        if (!Objects.isNull(files)) {
            List<FileUploadResponseDTO> responseList = uploadFiles(doctor, new MultipartFile[]{files});
            DoctorUtils.setAvatarFileProperties(responseList.get(0), doctorAvatar);
        } else doctorAvatar.setStatus(StatusConstants.INACTIVE);

        saveDoctorAvatar(doctorAvatar);
    }

    private void validateDoctor(Long doctorCount, String name, String mobileNumber) {

        if (doctorCount.intValue() > 0)
            throw new DataDuplicationException(
                    String.format(ErrorMessageConstants.NAME_AND_MOBILE_NUMBER_DUPLICATION_MESSAGE, Doctor.class.getSimpleName(), name, mobileNumber),
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

    private void saveDoctorAvatar(DoctorAvatar doctorAvatar) {
        doctorAvatarRepository.save(doctorAvatar);
    }

    public Doctor findById(Long doctorId) {
        return doctorRepository.findDoctorById(doctorId)
                .orElseThrow(() -> DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(doctorId));
    }

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Doctor.class, "id", id.toString());
    };
}


