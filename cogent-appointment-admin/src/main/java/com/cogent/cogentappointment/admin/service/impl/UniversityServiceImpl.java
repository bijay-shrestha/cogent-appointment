package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.model.University;
import com.cogent.cogentappointment.admin.repository.UniversityRepository;
import com.cogent.cogentappointment.admin.service.UniversityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.UniversityLog.UNIVERSITY;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 08/11/2019
 */
@Service
@Transactional
@Slf4j
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    public UniversityServiceImpl(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveUniversity() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, UNIVERSITY);

        List<DropDownResponseDTO> responseDTOS = universityRepository.fetchActiveUniversity();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public University fetchUniversityById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, UNIVERSITY);

        University university = universityRepository.fetchActiveUniversityById(id)
                .orElseThrow(() -> new NoContentFoundException(University.class, "id", id.toString()));

        log.info(FETCHING_PROCESS_COMPLETED, UNIVERSITY, getDifferenceBetweenTwoTime(startTime));

        return university;
    }
}
