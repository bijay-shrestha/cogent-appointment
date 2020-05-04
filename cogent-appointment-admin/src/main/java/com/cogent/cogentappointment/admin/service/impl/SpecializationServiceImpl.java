package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.SpecializationRepository;
import com.cogent.cogentappointment.admin.service.HospitalService;
import com.cogent.cogentappointment.admin.service.SpecializationService;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Specialization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.SpecializationLog.SPECIALIZATION;
import static com.cogent.cogentappointment.admin.utils.SpecializationUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.admin.utils.commons.NameAndCodeValidationUtils.validateDuplicity;

/**
 * @author smriti on 2019-08-11
 */
@Service
@Transactional
@Slf4j
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository specializationRepository;

    private final HospitalService hospitalService;

    public SpecializationServiceImpl(SpecializationRepository specializationRepository,
                                     HospitalService hospitalService) {
        this.specializationRepository = specializationRepository;
        this.hospitalService = hospitalService;
    }

    @Override
    public void save(SpecializationRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, SPECIALIZATION);

        List<Object[]> specializations = specializationRepository.validateDuplicity(
                requestDTO.getName(),
                requestDTO.getCode(),
                requestDTO.getHospitalId());

        validateDuplicity(specializations, requestDTO.getName(), requestDTO.getCode(),
                Specialization.class.getSimpleName());

        Hospital hospital = fetchHospitalById(requestDTO.getHospitalId());

        save(parseToSpecialization(requestDTO, hospital));

        log.info(SAVING_PROCESS_COMPLETED, SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(SpecializationUpdateRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, SPECIALIZATION);

        Specialization specialization = findBySpecializationId(requestDTO.getId());

        List<Object[]> specializations = specializationRepository.validateDuplicity(
                requestDTO.getId(),
                requestDTO.getName(),
                specialization.getCode(),
                requestDTO.getHospitalId()
        );

        validateDuplicity(specializations, requestDTO.getName(), specialization.getCode(),
                Specialization.class.getSimpleName());

        Hospital hospital = fetchHospitalById(requestDTO.getHospitalId());

        parseToUpdatedSpecialization(requestDTO, specialization, hospital);

        log.info(UPDATING_PROCESS_COMPLETED, SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, SPECIALIZATION);

        Specialization specialization = findBySpecializationId(deleteRequestDTO.getId());

        save(parseToDeletedSpecialization(specialization, deleteRequestDTO));

        log.info(DELETING_PROCESS_COMPLETED, SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<SpecializationMinimalResponseDTO> search(SpecializationSearchRequestDTO searchRequestDTO,
                                                         Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, SPECIALIZATION);

        List<SpecializationMinimalResponseDTO> responseDTOS =
                specializationRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveSpecializationForDropDown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_ACTIVE_DROPDOWN, SPECIALIZATION);

        List<DropDownResponseDTO> responseDTOS = specializationRepository.fetchActiveSpecializationForDropDown();

        log.info(FETCHING_PROCESS_FOR_ACTIVE_DROPDOWN_COMPLETED, SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchSpecializationForDropDown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, SPECIALIZATION);

        List<DropDownResponseDTO> responseDTOS = specializationRepository.fetchSpecializationForDropDown();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public SpecializationResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, SPECIALIZATION);

        SpecializationResponseDTO responseDTO = specializationRepository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<DropDownResponseDTO> fetchSpecializationByDoctorId(Long DoctorId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, SPECIALIZATION);

        List<DropDownResponseDTO> responseDTOS =
                specializationRepository.fetchSpecializationByDoctorId(DoctorId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveSpecializationByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_ACTIVE_DROPDOWN, SPECIALIZATION);

        List<DropDownResponseDTO> responseDTOS =
                specializationRepository.fetchActiveSpecializationByHospitalId(hospitalId);

        log.info(FETCHING_PROCESS_FOR_ACTIVE_DROPDOWN_COMPLETED, SPECIALIZATION,
                getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchSpecializationByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, SPECIALIZATION);

        List<DropDownResponseDTO> responseDTOS =
                specializationRepository.fetchSpecializationByHospitalId(hospitalId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, SPECIALIZATION,
                getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Specialization fetchActiveSpecializationById(Long specializationId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, SPECIALIZATION);

        Specialization specialization = specializationRepository.findActiveSpecializationById(specializationId)
                .orElseThrow(() -> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND.apply(specializationId));

        log.info(FETCHING_PROCESS_COMPLETED, SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));

        return specialization;
    }

    private void save(Specialization specialization) {
        specializationRepository.save(specialization);
    }

    private Specialization findBySpecializationId(Long specializationId) {
        return specializationRepository.findSpecializationById(specializationId)
                .orElseThrow(() -> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND.apply(specializationId));
    }

    private Function<Long, NoContentFoundException> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, SPECIALIZATION, id);
        throw new NoContentFoundException(Specialization.class, "id", id.toString());
    };

    private Hospital fetchHospitalById(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

}
