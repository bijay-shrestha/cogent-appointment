package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.patient.PatientUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientMinDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.patient.PatientResponseDTO;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.HospitalPatientInfoRepository;
import com.cogent.cogentappointment.admin.repository.PatientMetaInfoRepository;
import com.cogent.cogentappointment.admin.repository.PatientRepository;
import com.cogent.cogentappointment.admin.service.PatientService;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.PatientServiceMessages.DUPLICATE_PATIENT_MESSAGE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.PatientLog.*;
import static com.cogent.cogentappointment.admin.utils.PatientUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.*;

/**
 * @author smriti ON 16/01/2020
 */
@Service
@Transactional
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    private final PatientMetaInfoRepository patientMetaInfoRepository;

    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;

    public PatientServiceImpl(PatientRepository patientRepository,
                              PatientMetaInfoRepository patientMetaInfoRepository,
                              HospitalPatientInfoRepository hospitalPatientInfoRepository) {
        this.patientRepository = patientRepository;
        this.patientMetaInfoRepository = patientMetaInfoRepository;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
    }

    @Override
    public PatientDetailResponseDTO fetchDetailsById(Long hospitalPatientInfoId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        PatientDetailResponseDTO responseDTO = patientRepository.fetchDetailsById(hospitalPatientInfoId);

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<PatientResponseDTO> search(PatientSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, PATIENT);

        List<PatientResponseDTO> responseDTO = patientRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public void update(PatientUpdateRequestDTO updateRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, PATIENT);

        Patient patientToBeUpdated = patientRepository.getPatientByHospitalPatientInfoId(updateRequestDTO.getId());

        HospitalPatientInfo hospitalPatientInfoToBeUpdated = hospitalPatientInfoRepository
                .fetchHospitalPatientInfoByPatientId(updateRequestDTO.getId());

        Long patientCount = patientRepository.validatePatientDuplicity(updateRequestDTO,patientToBeUpdated.getId());

        validatePatientDuplicity(patientCount, updateRequestDTO.getName(),
                updateRequestDTO.getMobileNumber(), updateRequestDTO.getDateOfBirth());

        updatePatient(updateRequestDTO, patientToBeUpdated);

        saveHospitalPatientInfo(updateHospitalPatientInfo(updateRequestDTO, hospitalPatientInfoToBeUpdated));

        PatientMetaInfo patientMetaInfoToBeUpdated = patientMetaInfoRepository.fetchByPatientId(patientToBeUpdated.getId());

        savePatientMetaInfo(updatePatientMetaInfo(hospitalPatientInfoToBeUpdated,
                patientMetaInfoToBeUpdated,
                updateRequestDTO));

        log.info(UPDATING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DropDownResponseDTO> patientMetaInfoDropDownListByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        List<DropDownResponseDTO> responseDTOS = patientMetaInfoRepository
                .fetchPatientMetaInfoDropDownListByHospitalId(hospitalId);

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> patientMetaInfoActiveDropDownListByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        List<DropDownResponseDTO> responseDTOS = patientMetaInfoRepository
                .fetchActivePatientMetaInfoDropDownListByHospitalId(hospitalId);

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public void registerPatient(Long patientId, Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(REGISTERING_PATIENT_PROCESS_STARTED);

        HospitalPatientInfo hospitalPatientInfo =
                hospitalPatientInfoRepository.findByPatientAndHospitalId(patientId, hospitalId)
                        .orElseThrow(() -> PATIENT_WITH_GIVEN_ID_NOT_FOUND.apply(patientId));

        if (hospitalPatientInfo.getIsRegistered().equals(NO)) {
            String latestRegistrationNumber =
                    patientRepository.fetchLatestRegistrationNumber(hospitalPatientInfo.getHospital().getId());

            registerPatientDetails(hospitalPatientInfo, latestRegistrationNumber);

            PatientMetaInfo patientMetaInfo = patientMetaInfoRepository.fetchByPatientId(patientId);
            updatePatientMetaInfo(patientMetaInfo, hospitalPatientInfo.getRegistrationNumber());
        }

        log.info(REGISTERING_PATIENT_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public PatientMinDetailResponseDTO fetchDetailByAppointmentId(Long appointmentId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, PATIENT);

        PatientMinDetailResponseDTO patientInfo = patientRepository.fetchDetailByAppointmentId(appointmentId);

        log.info(FETCHING_PROCESS_COMPLETED, PATIENT, getDifferenceBetweenTwoTime(startTime));

        return patientInfo;
    }

    private Patient fetchPatientById(Long id) {
        return patientRepository.fetchPatientById(id)
                .orElseThrow(() -> PATIENT_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private void savePatientMetaInfo(PatientMetaInfo patientMetaInfo) {
        patientMetaInfoRepository.save(patientMetaInfo);
    }

    private void validatePatientDuplicity(Long patientCount, String name, String mobileNumber,
                                          Date dateOfBirth) {

        if (patientCount.intValue() != 0)
            throw new DataDuplicationException(String.format(DUPLICATE_PATIENT_MESSAGE,
                    name, mobileNumber, utilDateToSqlDate(dateOfBirth)));
    }

    private void saveHospitalPatientInfo(HospitalPatientInfo hospitalPatientInfo) {
        hospitalPatientInfoRepository.save(hospitalPatientInfo);
    }

    private Function<Long, NoContentFoundException> PATIENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Patient.class, "patientId", id.toString());
    };

}

