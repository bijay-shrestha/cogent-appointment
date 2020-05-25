package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.BreakTypeRepository;
import com.cogent.cogentappointment.admin.service.BreakTypeService;
import com.cogent.cogentappointment.persistence.model.BreakType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
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

    @Override
    public BreakType fetchActiveBreakType(Long breakTypeId) {
        return breakTypeRepository.fetchActiveBreakType(breakTypeId)
                .orElseThrow(() -> NO_BREAK_TYPE_FOUND.apply(breakTypeId));
    }

    private Function<Long, NoContentFoundException> NO_BREAK_TYPE_FOUND = (breakTypeId) -> {
        log.error(CONTENT_NOT_FOUND, BREAK_TYPE);
        throw new NoContentFoundException(BreakType.class, "breakTypeId", breakTypeId.toString());
    };
}
