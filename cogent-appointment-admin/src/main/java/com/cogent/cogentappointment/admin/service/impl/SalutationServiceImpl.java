package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.SalutationRepository;
import com.cogent.cogentappointment.admin.service.SalutationService;
import com.cogent.cogentappointment.persistence.model.Salutation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.SalutationLog.SALUTATION;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

@Service
@Transactional
@Slf4j
public class SalutationServiceImpl implements SalutationService {

    private final SalutationRepository salutationRepository;

    public SalutationServiceImpl(SalutationRepository salutationRepository) {
        this.salutationRepository = salutationRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinSalutation() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, SALUTATION);

        List<DropDownResponseDTO> responseDTOS = salutationRepository.fetchActiveMinSalutation();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, SALUTATION, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Salutation fetchSalutationById(Long salutationId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, SALUTATION);

        Salutation salutation = salutationRepository.fetchActiveSalutationById(salutationId)
                .orElseThrow(() -> SALUTATION_WITH_GIVEN_ID_NOT_FOUND.apply(salutationId));

        log.info(FETCHING_PROCESS_COMPLETED, SALUTATION, getDifferenceBetweenTwoTime(startTime));

        return salutation;
    }

    private Function<Long, NoContentFoundException> SALUTATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, SALUTATION, id);
        throw new NoContentFoundException(Salutation.class, "id", id.toString());
    };
}
