package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentFollowUpResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentFollowUpTrackerRepository;
import com.cogent.cogentappointment.client.repository.DoctorRepository;
import com.cogent.cogentappointment.client.repository.HospitalRepository;
import com.cogent.cogentappointment.client.service.AppointmentFollowUpTrackerService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.client.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentFollowUpTrackerLog.APPOINTMENT_FOLLOW_UP_TRACKER;
import static com.cogent.cogentappointment.client.utils.AppointmentFollowUpTrackerUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getHospitalId;
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

    public AppointmentFollowUpTrackerServiceImpl(AppointmentFollowUpTrackerRepository appointmentFollowUpTrackerRepository,
                                                 HospitalRepository hospitalRepository,
                                                 DoctorRepository doctorRepository) {
        this.appointmentFollowUpTrackerRepository = appointmentFollowUpTrackerRepository;
        this.hospitalRepository = hospitalRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public AppointmentFollowUpResponseDTO fetchAppointmentFollowUpDetails(AppointmentFollowUpRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        List<Object[]> followUpDetails = appointmentFollowUpTrackerRepository.fetchFollowUpDetails(
                requestDTO.getPatientId(),
                requestDTO.getDoctorId(),
                requestDTO.getSpecializationId(),
                requestDTO.getHospitalId()
        );

        AppointmentFollowUpResponseDTO responseDTO;

        if (followUpDetails.isEmpty()) {
            responseDTO = parseToAppointmentFollowUpResponseDTO(NO, null, null);
        } else {
            Object[] followUpObject = followUpDetails.get(0);

            Long parentAppointmentId = (Long) get(followUpObject, 0);

            Date appointmentDate = (Date) get(followUpObject, 1);

            int intervalDays = hospitalRepository.fetchHospitalFreeFollowUpIntervalDays(requestDTO.getHospitalId());

            Date requestedDate = utilDateToSqlDate(requestDTO.getAppointmentDate());
            Date expiryDate = utilDateToSqlDate(addDays(appointmentDate, intervalDays));

            if (isAppointmentActive(requestedDate, expiryDate)) {
                Double doctorFollowUpCharge = doctorRepository.fetchDoctorAppointmentFollowUpCharge(
                        requestDTO.getDoctorId(), getHospitalId());
                responseDTO = parseToAppointmentFollowUpResponseDTO(YES, doctorFollowUpCharge, parentAppointmentId);
            } else {
                responseDTO = parseToAppointmentFollowUpResponseDTO(NO, null, null);
            }
        }

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public void updateFollowUpTracker(Long parentAppointmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        AppointmentFollowUpTracker followUpTracker =
                appointmentFollowUpTrackerRepository.fetchLatestAppointmentFollowUpTracker(parentAppointmentId);

        updateNumberOfFreeFollowUps(followUpTracker);

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void updateFollowUpTrackerStatus() {

    }

    @Override
    public void save(Long parentAppointmentId, String parentAppointmentNumber, Hospital hospital, Doctor doctor, Specialization specialization, Patient patient) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_FOLLOW_UP_TRACKER);

        Integer numberOfFreeFollowUps = hospitalRepository.fetchHospitalFollowUpCount(hospital.getId());

        save(parseToAppointmentFollowUpTracker(
                parentAppointmentId, parentAppointmentNumber, numberOfFreeFollowUps,
                doctor, specialization, patient, hospital));

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_FOLLOW_UP_TRACKER, getDifferenceBetweenTwoTime(startTime));
    }

    private boolean isAppointmentActive(Date requestedDate, Date expiryDate) {

        return (Objects.requireNonNull(requestedDate).compareTo(Objects.requireNonNull(expiryDate))) < 0
                || (Objects.requireNonNull(requestedDate).compareTo(Objects.requireNonNull(expiryDate))) == 0;
    }

    private void save(AppointmentFollowUpTracker followUpTracker) {
        appointmentFollowUpTrackerRepository.save(followUpTracker);
    }
}
