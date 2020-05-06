package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentTransferRepository;
import com.cogent.cogentappointment.client.service.AppointmentTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.AppointmentTransferLog.APPOINTMENT_TRANSFER;
import static com.cogent.cogentappointment.client.log.constants.AppointmentTransferLog.FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.constants.AppointmentTransferLog.FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Service
@Slf4j
@Transactional
public class AppointmentTransferServiceImpl implements AppointmentTransferService{

    private final AppointmentTransferRepository repository;

    public AppointmentTransferServiceImpl(AppointmentTransferRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Date> fetchAvailableDatesByDoctorId(Long doctorId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_STARTED, APPOINTMENT_TRANSFER);
        List<Date> response=new ArrayList<>();
        List<DoctorDatesResponseDTO> rosterDates=repository.getDatesByDoctorId(doctorId);
        rosterDates.forEach(rosterId->{
            List<String> dayOffDay=repository.getDayOffDaysByRosterId(rosterId.getId());
        });
        List<DoctorDatesResponseDTO> overrideDates=repository.getOverrideDatesByDoctorId(doctorId);

        log.info(FETCHING_AVAILABLE_DATES_BY_DOCTOR_ID_PROCESS_COMPLETED, APPOINTMENT_TRANSFER, getDifferenceBetweenTwoTime(startTime));
        return response;
    }
}
