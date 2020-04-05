package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentFollowUpResponseDTOWithStatus;
import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.AppointmentFollowUpResponseDTO;
import com.cogent.cogentappointment.esewa.repository.AppointmentFollowUpTrackerRepository;
import com.cogent.cogentappointment.esewa.repository.DoctorRepository;
import com.cogent.cogentappointment.esewa.repository.HospitalRepository;
import com.cogent.cogentappointment.esewa.service.AppointmentFollowUpTrackerService;
import com.cogent.cogentappointment.esewa.service.AppointmentReservationService;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.INACTIVE;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER_STATUS;
import static com.cogent.cogentappointment.esewa.utils.AppointmentFollowUpTrackerUtils.parseToAppointmentFollowUpResponseDTO;
import static com.cogent.cogentappointment.esewa.utils.AppointmentFollowUpTrackerUtils.parseToAppointmentFollowUpResponseDTOWithStatus;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;
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
    public AppointmentFollowUpResponseDTOWithStatus fetchAppointmentFollowUpDetails(AppointmentFollowUpRequestDTO requestDTO) {

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
            Double doctorAppointmentCharge = doctorRepository.fetchDoctorAppointmentCharge(
                    requestDTO.getDoctorId(), requestDTO.getHospitalId());

            responseDTO = parseToAppointmentFollowUpResponseDTO(NO, doctorAppointmentCharge, null,
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
                        requestDTO.getDoctorId(), requestDTO.getHospitalId());

                responseDTO = parseToAppointmentFollowUpResponseDTO(YES, doctorFollowUpCharge, parentAppointmentId,
                        savedAppointmentReservationId);
            } else {
                Double doctorAppointmentCharge = doctorRepository.fetchDoctorAppointmentCharge(
                        requestDTO.getDoctorId(), requestDTO.getHospitalId());

                responseDTO = parseToAppointmentFollowUpResponseDTO(NO, doctorAppointmentCharge, null,
                        savedAppointmentReservationId);
            }
        }

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));

        return parseToAppointmentFollowUpResponseDTOWithStatus(responseDTO);
    }

    @Override
    public void updateFollowUpTrackerStatus() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER_STATUS);

        List<AppointmentFollowUpTracker> followUpTrackers =
                appointmentFollowUpTrackerRepository.fetchActiveFollowUpTracker();

        followUpTrackers.forEach(followUpTracker -> {
            int intervalDays = hospitalRepository.fetchHospitalFollowUpIntervalDays(
                    followUpTracker.getHospitalId().getId());

            Date expiryDate = utilDateToSqlDate(
                    addDays(followUpTracker.getAppointmentApprovedDate(), intervalDays));

            Date currentDate = utilDateToSqlDate(new Date());

            if ((Objects.requireNonNull(expiryDate).compareTo(Objects.requireNonNull(currentDate))) < 0)
                followUpTracker.setStatus(INACTIVE);

        });

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER_STATUS, getDifferenceBetweenTwoTime(startTime));
    }


    private boolean isAppointmentActive(Date requestedDate, Date expiryDate) {

        return (Objects.requireNonNull(requestedDate).compareTo(Objects.requireNonNull(expiryDate))) < 0
                || (Objects.requireNonNull(requestedDate).compareTo(Objects.requireNonNull(expiryDate))) == 0;
    }
}
