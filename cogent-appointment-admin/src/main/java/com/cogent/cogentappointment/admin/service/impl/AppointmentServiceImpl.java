package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author smriti on 2019-10-22
 */
@Service
@Transactional
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    public AppointmentServiceImpl() {
    }

//    @Override
//    public List<AppointmentStatusResponseDTO> fetchAppointmentForAppointmentStatus(
//            AppointmentStatusRequestDTO requestDTO) {
//
//        Long startTime = getTimeInMillisecondsFromLocalDate();
//
//        log.info(FETCHING_DETAIL_PROCESS_STARTED, APPOINTMENT);
//
//        List<AppointmentStatusResponseDTO> responseDTOS =
//                appointmentRepository.fetchAppointmentForAppointmentStatus(requestDTO);
//
//        log.info(RESCHEDULE_PROCESS_STARTED, getDifferenceBetweenTwoTime(startTime));
//        return responseDTOS;
//    }
}

