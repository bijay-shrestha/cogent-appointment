package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationResponseDTO;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.log.CommonLogConstant;
import com.cogent.cogentappointment.admin.log.constants.SpecializationLog;
import com.cogent.cogentappointment.admin.model.Specialization;
import com.cogent.cogentappointment.admin.repository.SpecializationRepository;
import com.cogent.cogentappointment.admin.service.SpecializationService;
import com.cogent.cogentappointment.admin.utils.SpecializationUtils;
import com.cogent.cogentappointment.admin.utils.commons.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

/**
 * @author smriti on 2019-08-11
 */
@Service
@Transactional
@Slf4j
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationServiceImpl(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    @Override
    public String save(SpecializationRequestDTO requestDTO) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, SpecializationLog.SPECIALIZATION);

        validateName(specializationRepository.fetchSpecializationByName(requestDTO.getName()),
                requestDTO.getName());

        Specialization specialization = save(SpecializationUtils.parseToSpecialization(requestDTO));

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, SpecializationLog.SPECIALIZATION, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return specialization.getCode();
    }

    @Override
    public void update(SpecializationUpdateRequestDTO requestDTO) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.UPDATING_PROCESS_STARTED, SpecializationLog.SPECIALIZATION);

        Specialization specialization = findById(requestDTO.getId());

        validateName(specializationRepository.fetchSpecializationByIdAndName
                (requestDTO.getId(), requestDTO.getName()), requestDTO.getName());

        save(SpecializationUtils.parseToUpdatedSpecialization(requestDTO, specialization));

        log.info(CommonLogConstant.UPDATING_PROCESS_COMPLETED, SpecializationLog.SPECIALIZATION, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.DELETING_PROCESS_STARTED, SpecializationLog.SPECIALIZATION);

        Specialization specialization = findById(deleteRequestDTO.getId());

        save(SpecializationUtils.parseToDeletedSpecialization(specialization, deleteRequestDTO));

        log.info(CommonLogConstant.DELETING_PROCESS_COMPLETED, SpecializationLog.SPECIALIZATION, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<SpecializationMinimalResponseDTO> search(SpecializationSearchRequestDTO searchRequestDTO,
                                                         Pageable pageable) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SEARCHING_PROCESS_STARTED, SpecializationLog.SPECIALIZATION);

        List<SpecializationMinimalResponseDTO> responseDTOS =
                specializationRepository.search(searchRequestDTO, pageable);

        log.info(CommonLogConstant.SEARCHING_PROCESS_COMPLETED, SpecializationLog.SPECIALIZATION, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveSpecializationForDropDown() {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, SpecializationLog.SPECIALIZATION);

        List<DropDownResponseDTO> responseDTOS = specializationRepository.fetchActiveSpecializationForDropDown();

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, SpecializationLog.SPECIALIZATION, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public SpecializationResponseDTO fetchDetailsById(Long id) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_STARTED, SpecializationLog.SPECIALIZATION);

        SpecializationResponseDTO responseDTO = specializationRepository.fetchDetailsById(id);

        log.info(CommonLogConstant.FETCHING_DETAIL_PROCESS_COMPLETED, SpecializationLog.SPECIALIZATION, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public List<DropDownResponseDTO> fetchSpecializationByDoctorId(Long DoctorId) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN, SpecializationLog.SPECIALIZATION);

        List<DropDownResponseDTO> responseDTOS =
                specializationRepository.fetchSpecializationByDoctorId(DoctorId);

        log.info(CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, SpecializationLog.SPECIALIZATION, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Specialization fetchActiveSpecializationById(Long specializationId) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.FETCHING_PROCESS_STARTED, SpecializationLog.SPECIALIZATION);

        Specialization specialization = specializationRepository.findActiveSpecializationById(specializationId)
                .orElseThrow(() -> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND.apply(specializationId));

        log.info(CommonLogConstant.FETCHING_PROCESS_COMPLETED, SpecializationLog.SPECIALIZATION, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return specialization;
    }

    private void validateName(Long specializationCount, String name) {
        if (specializationCount.intValue() > 0)
            throw new DataDuplicationException(
                    String.format(ErrorMessageConstants.NAME_DUPLICATION_MESSAGE, Specialization.class.getSimpleName(), name));
    }

    private Specialization save(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    public Specialization findById(Long specializationId) {
        return specializationRepository.findSpecializationById(specializationId)
                .orElseThrow(() -> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND.apply(specializationId));
    }

    private Function<Long, NoContentFoundException> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Specialization.class, "id", id.toString());
    };
}
