package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.SalutationRepository;
import com.cogent.cogentappointment.client.service.SalutationService;
import com.cogent.cogentappointment.persistence.model.Salutation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.SalutationLog.SALUTATION;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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

//    @Override
//    public Salutation fetchSalutationById(Long salutationId) {
//        Long startTime = getTimeInMillisecondsFromLocalDate();
//
//        log.info(FETCHING_PROCESS_STARTED, SALUTATION);
//
//        Salutation salutation = salutationRepository.fetchActiveSalutationById(salutationId)
//                .orElseThrow(() -> SALUTATION_WITH_GIVEN_ID_NOT_FOUND.apply(salutationId));
//
//        log.info(FETCHING_PROCESS_COMPLETED, SALUTATION, getDifferenceBetweenTwoTime(startTime));
//
//        return salutation;
//    }

    private Function<Long, NoContentFoundException> SALUTATION_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, SALUTATION, id);
        throw new NoContentFoundException(Salutation.class, "id", id.toString());
    };
}
