package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.repository.BreakTypeRepository;
import com.cogent.cogentappointment.admin.service.BreakTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN;
import static com.cogent.cogentappointment.admin.log.constants.BreakTypeLog.BREAK_TYPE;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 06/05/20
 */
@Service
@Transactional
@Slf4j
public class BreakTypeServiceImpl implements BreakTypeService {

    private final BreakTypeRepository breakTypeRepository;

    public BreakTypeServiceImpl(BreakTypeRepository breakTypeRepository) {
        this.breakTypeRepository = breakTypeRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchBreakTypeByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, BREAK_TYPE);

        List<DropDownResponseDTO> minInfo = breakTypeRepository.fetchBreakTypeByHospitalId(hospitalId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, BREAK_TYPE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }
}
