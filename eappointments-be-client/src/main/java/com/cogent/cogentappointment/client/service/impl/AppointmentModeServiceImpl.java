package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentModeRepository;
import com.cogent.cogentappointment.client.service.AppointmentModeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN;
import static com.cogent.cogentappointment.client.log.constants.AppointmentMode.APPOINTMENT_MODE;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 14/07/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentModeServiceImpl implements AppointmentModeService{

    private final AppointmentModeRepository appointmentModeRepository;

    public AppointmentModeServiceImpl(AppointmentModeRepository appointmentModeRepository) {
        this.appointmentModeRepository = appointmentModeRepository;
    }


    @Override
    public List<DropDownResponseDTO> fetchActiveMinAppointmentMode() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, APPOINTMENT_MODE);

        List<DropDownResponseDTO> minInfo = appointmentModeRepository.fetchActiveMinAppointmentMode();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, APPOINTMENT_MODE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }
}
