package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.ShiftRepository;
import com.cogent.cogentappointment.admin.service.ShiftService;
import com.cogent.cogentappointment.persistence.model.Shift;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.ShiftLog.SHIFT;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 06/05/20
 */
@Service
@Transactional
@Slf4j
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;

    public ShiftServiceImpl(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchShiftByHospitalId(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, SHIFT);

        List<DropDownResponseDTO> minInfo = shiftRepository.fetchShiftByHospitalId(hospitalId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, SHIFT, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    @Override
    public Shift fetchActiveShiftByIdAndHospitalId(Long shiftId, Long hospitalId) {
        return shiftRepository.fetchShiftByIdAndHospitalId(shiftId, hospitalId)
                .orElseThrow(() -> NO_SHIFT_FOUND.apply(shiftId));
    }

    @Override
    public String fetchNameByIds(String shiftIds) {
        return shiftRepository.fetchNameByIds(shiftIds);
    }

    private Function<Long, NoContentFoundException> NO_SHIFT_FOUND = (shiftId) -> {
        log.error(CONTENT_NOT_FOUND, SHIFT);
        throw new NoContentFoundException(Shift.class, "shiftId", shiftId.toString());
    };

}
