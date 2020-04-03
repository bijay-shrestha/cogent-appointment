package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.esewa.exception.DataDuplicationException;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.HospitalRepository;
import com.cogent.cogentappointment.esewa.repository.SpecializationRepository;
import com.cogent.cogentappointment.esewa.service.SpecializationService;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Specialization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.NAME_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.SpecializationLog.SPECIALIZATION;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.esewa.utils.commons.NameAndCodeValidationUtils.validateDuplicity;
import static com.cogent.cogentappointment.esewa.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

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
    public Specialization fetchActiveSpecializationByIdAndHospitalId(Long specializationId,
                                                                     Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, SPECIALIZATION);

        Specialization specialization = specializationRepository
                .findActiveSpecializationByIdAndHospitalId(specializationId, hospitalId)
                .orElseThrow(() -> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND.apply(specializationId));

        log.info(FETCHING_PROCESS_COMPLETED, SPECIALIZATION, getDifferenceBetweenTwoTime(startTime));

        return specialization;
    }



    private Function<Long, NoContentFoundException> SPECIALIZATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID,SPECIALIZATION,id);
        throw new NoContentFoundException(Specialization.class, "id", id.toString());
    };

}
