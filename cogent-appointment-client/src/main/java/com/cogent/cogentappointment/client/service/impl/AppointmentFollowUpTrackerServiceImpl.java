package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentFollowUpResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentFollowUpTrackerRepository;
import com.cogent.cogentappointment.client.repository.DoctorRepository;
import com.cogent.cogentappointment.client.repository.HospitalRepository;
import com.cogent.cogentappointment.client.service.AppointmentFollowUpTrackerService;
import com.cogent.cogentappointment.client.service.AppointmentReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.client.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.client.utils.AppointmentFollowUpTrackerUtils.parseToAppointmentFollowUpResponseDTO;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static java.lang.reflect.Array.get;

/**
 * @author smriti on 16/02/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentFollowUpTrackerServiceImpl implements AppointmentFollowUpTrackerService {

    private final AppointmentFollowUpTrackerRepository appointmentFollowUpTrackerRepository;

    private final HospitalRepository hospitalRepository;

    private final DoctorRepository doctorRepository;

    private final AppointmentReservationService appointmentReservationService;

    public AppointmentFollowUpTrackerServiceImpl(AppointmentFollowUpTrackerRepository appointmentFollowUpTrackerRepository,
                                                 HospitalRepository hospitalRepository,
                                                 DoctorRepository doctorRepository,
                                                 AppointmentReservationService appointmentReservationService) {
        this.appointmentFollowUpTrackerRepository = appointmentFollowUpTrackerRepository;
        this.hospitalRepository = hospitalRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentReservationService = appointmentReservationService;
    }

    @Override
    public AppointmentFollowUpResponseDTO fetchAppointmentFollowUpDetails(AppointmentFollowUpRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        Long savedAppointmentReservationId = appointmentReservationService.save(requestDTO);

        List<Object[]> followUpDetails = appointmentFollowUpTrackerRepository.fetchFollowUpDetails(
                requestDTO.getPatientId(),
                requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getHospitalId()
        );

        AppointmentFollowUpResponseDTO responseDTO;

        if (followUpDetails.isEmpty()) {
            responseDTO = parseToAppointmentFollowUpResponseDTO(NO, null, null,
                    savedAppointmentReservationId);
        } else {
            Object[] followUpObject = followUpDetails.get(0);

            Long parentAppointmentId = (Long) get(followUpObject, 0);

            Date appointmentDate = (Date) get(followUpObject, 1);

            int intervalDays = hospitalRepository.fetchHospitalFreeFollowUpIntervalDays(requestDTO.getHospitalId());

            Date requestedDate = utilDateToSqlDate(requestDTO.getAppointmentDate());
            Date expiryDate = utilDateToSqlDate(addDays(appointmentDate, intervalDays));

            if (isAppointmentActive(requestedDate, expiryDate)) {
                Double doctorFollowUpCharge = doctorRepository.fetchDoctorAppointmentFollowUpCharge(
                        requestDTO.getDoctorId());
                responseDTO = parseToAppointmentFollowUpResponseDTO(YES, doctorFollowUpCharge, parentAppointmentId,
                        savedAppointmentReservationId);
            } else {
                responseDTO = parseToAppointmentFollowUpResponseDTO(NO, null, null,
                        savedAppointmentReservationId);
            }
        }

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    private boolean isAppointmentActive(Date requestedDate, Date expiryDate) {

        return (Objects.requireNonNull(requestedDate).compareTo(Objects.requireNonNull(expiryDate))) < 0
                || (Objects.requireNonNull(requestedDate).compareTo(Objects.requireNonNull(expiryDate))) == 0;
    }
}
