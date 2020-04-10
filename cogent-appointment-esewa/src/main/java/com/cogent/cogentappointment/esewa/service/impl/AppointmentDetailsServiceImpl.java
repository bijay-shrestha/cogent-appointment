package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.*;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.*;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.DoctorDutyRosterOverrideRepository;
import com.cogent.cogentappointment.esewa.repository.DoctorDutyRosterRepository;
import com.cogent.cogentappointment.esewa.service.AppointmentDetailsService;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.Doctor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentLog.APPOINTMENT;
import static com.cogent.cogentappointment.esewa.log.constants.DoctorLog.DOCTOR;
import static com.cogent.cogentappointment.esewa.log.constants.eSewaLog.*;
import static com.cogent.cogentappointment.esewa.utils.AppointmentDetailsUtils.*;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.*;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Sauravi Thapa ON 4/2/20
 */

@Service
@Transactional
@Slf4j
public class AppointmentDetailsServiceImpl implements AppointmentDetailsService {

    private final DoctorDutyRosterRepository dutyRosterRepository;

    private final DoctorDutyRosterOverrideRepository dutyRosterOverrideRepository;

    public AppointmentDetailsServiceImpl(DoctorDutyRosterRepository dutyRosterRepository,
                                         DoctorDutyRosterOverrideRepository dutyRosterOverrideRepository) {
        this.dutyRosterRepository = dutyRosterRepository;
        this.dutyRosterOverrideRepository = dutyRosterOverrideRepository;
    }

    /*RETURN MESSAGE IF DOCTOR IS AVAILABLE ON DATE*/
    @Override
    public DoctorAvailabilityStatusResponseDTO fetchDoctorAvailableStatus(AppointmentDetailRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_AVAILABLE_STATUS);

        DoctorAvailabilityStatusResponseDTO doctorAvailableStatus =
                dutyRosterOverrideRepository.fetchDoctorDutyRosterOverrideStatus(requestDTO);

        DoctorAvailabilityStatusResponseDTO responseDTO = (!Objects.isNull(doctorAvailableStatus))
                ? doctorAvailableStatus
                : dutyRosterRepository.fetchDoctorDutyRosterStatus(requestDTO);

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_AVAILABLE_STATUS, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    /*ALL AVAILABLE DOCTORS AND THEIR SPECIALIZATION ON THE CHOSEN DATE*/
    @Override
    public AvailableDoctorWithSpecializationResponseDTO fetchAvailableDoctorWithSpecialization(
            AppointmentDetailRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, AVAILABLE_DOCTOR_LIST);

        validateIfRequestedDateIsBeforeCurrentDate(requestDTO.getDate());

        List<AvailableDoctorWithSpecialization> availableDoctorFromDDROverride =
                dutyRosterOverrideRepository.fetchAvailableDoctor(requestDTO);

        List<AvailableDoctorWithSpecialization> availableDoctorFromDDR =
                dutyRosterRepository.fetchAvailableDoctor(requestDTO);

        if (ObjectUtils.isEmpty(availableDoctorFromDDROverride) && ObjectUtils.isEmpty(availableDoctorFromDDR)) {
            doctorNotAvailableError();
            throw DOCTORS_NOT_AVAILABLE.get();
        }

        List<AvailableDoctorWithSpecialization> mergedList =
                mergeOverrideAndActualDoctorList(availableDoctorFromDDROverride, availableDoctorFromDDR);

        log.info(FETCHING_PROCESS_COMPLETED, AVAILABLE_DOCTOR_LIST, getDifferenceBetweenTwoTime(startTime));

        return getAvailableDoctorWithSpecializationResponseDTO(mergedList);
    }

    /*RETURNS ALL THE AVAILABLE APPOINTMENT DATES AND TIME BY DOCTORID and SPECIALIZATIONID*/
    @Override
    public AppointmentDatesResponseDTO fetchAvailableDatesAndTime(AppointmentDatesRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_AVAILABLE_DATES_AND_TIME);

        List<AvailableDatesResponseDTO> appointmentDateAndTime = new ArrayList<>();

        List<DoctorDutyRosterAppointmentDate> doctorDutyRosterAppointmentDates = dutyRosterRepository
                .getDutyRosterByDoctorAndSpecializationId(requestDTO);

        for (DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate : doctorDutyRosterAppointmentDates) {

            List<DoctorWeekDaysDutyRosterAppointmentDate> weekDaysDutyRosterAppointmentDate =
                    getWeekdaysTimeByRosterId(doctorDutyRosterAppointmentDate.getId());

            List<Date> dates = getDates(doctorDutyRosterAppointmentDate.getFromDate(),
                    doctorDutyRosterAppointmentDate.getToDate());

            List<AvailableDatesResponseDTO> appointmentDatesResponseDTO =
                    getDutyRosterDatesAndTime(dates, weekDaysDutyRosterAppointmentDate);

            checkIfOverrideExists(doctorDutyRosterAppointmentDate, appointmentDatesResponseDTO, appointmentDateAndTime);
        }

        AppointmentDatesResponseDTO responseDTO = getFinalResponse(requestDTO, appointmentDateAndTime);

        if (ObjectUtils.isEmpty(responseDTO.getDates())) {
            appointmentNotAvailableError();
            throw APPOINTMENT_NOT_AVAILABLE.get();
        }
        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_AVAILABLE_DATES_AND_TIME, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    /*RETURNS ALL THE AVAILABLE APPOINTMENT DATES WITH SPECIALIZATION ID AND NAME BY DOCTORID*/
    @Override
    public AvailableDatesWithSpecializationResponseDTO fetchAvailableDatesWithSpecialization(Long doctorId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_AVAILABLE_DATES);

        List<AvailableDatesWithSpecialization> responseDTOList = new ArrayList<>();

        List<DutyRosterAppointmentDateAndSpecilizationDTO> appointmentDateAndSpecilizations = dutyRosterRepository
                .getAvaliableDatesAndSpecilizationByDoctorId(doctorId);

        appointmentDateAndSpecilizations.forEach(dateAndSpecilization -> {

            AvailableDatesWithSpecialization responseDTO = new AvailableDatesWithSpecialization();

            List<Date> availableDates = getDutyRosterDates(getDates(dateAndSpecilization.getFromDate(),
                    dateAndSpecilization.getToDate()),
                    getWeekdays(dateAndSpecilization.getId()));

            if (availableDates.size() != 0) {

                responseDTO.setSpecializationId(dateAndSpecilization.getSpecializationId());

                responseDTO.setSpecilaizationName(dateAndSpecilization.getSpecializationName());

                if (dateAndSpecilization.getHasOverride().equals(YES)) {

                    List<DutyRosterOverrideAppointmentDate> dateList = dutyRosterOverrideRepository
                            .fetchDayOffRosterOverridebyRosterId(dateAndSpecilization.getId());

                    dateList.forEach(date -> {
                        responseDTO.setAvaliableDates(mergeRosterAndRosterOverrideDates(availableDates,
                                getDatesBetween(date.getFromDate(), date.getToDate())));
                    });
                } else {
                    responseDTO.setAvaliableDates(availableDates);
                }
                responseDTOList.add(responseDTO);
            }
        });

        checkEmpty(responseDTOList);

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_AVAILABLE_DATES, getDifferenceBetweenTwoTime(startTime));

        return getAvailableDatesWithSpecializationResponseDTO(responseDTOList);
    }

    /*RETURNS ALL THE AVAILABLE APPOINTMENT DATES WITH DOCTOR ID AND NAME BY SPECIALIZATIONID*/
    @Override
    public AvailableDatesWithDoctorResponseDTO fetchAvailableDatesWithDoctor(Long specializationId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, SPECIALIZATION_AVAILABLE_DATES);

        List<AvailableDatesWithDoctor> responseDTOList = new ArrayList<>();

        List<DutyRosterAppointmentDateAndDoctorDTO> dateAndDoctorDTOS = dutyRosterRepository
                .getAvaliableDatesAndDoctorBySpecilizationId(specializationId);

        dateAndDoctorDTOS.forEach(dateAndSpecilization -> {

            AvailableDatesWithDoctor responseDTO = new AvailableDatesWithDoctor();

            List<String> weekDays = getWeekdays(dateAndSpecilization.getId());

            List<Date> rosterDates = getDates(dateAndSpecilization.getFromDate(),
                    dateAndSpecilization.getToDate());

            List<Date> availableDates = getDutyRosterDates(rosterDates, weekDays);

            if (availableDates.size() != 0) {

                responseDTO.setDoctorId(dateAndSpecilization.getDoctorId());

                responseDTO.setDoctorName(dateAndSpecilization.getDoctorName());

                if (dateAndSpecilization.getHasOverride().equals(YES)) {
                    List<DutyRosterOverrideAppointmentDate> dateList = dutyRosterOverrideRepository
                            .fetchDayOffRosterOverridebyRosterId(dateAndSpecilization.getId());

                    dateList.forEach(date -> {
                        responseDTO.setAvaliableDates(mergeRosterAndRosterOverrideDates(availableDates,
                                getDatesBetween(date.getFromDate(), date.getToDate())));
                    });
                } else {
                    responseDTO.setAvaliableDates(availableDates);
                }
                responseDTOList.add(responseDTO);
            }
        });

       checkEmpty(responseDTOList);

        log.info(FETCHING_PROCESS_COMPLETED, SPECIALIZATION_AVAILABLE_DATES, getDifferenceBetweenTwoTime(startTime));

        return getAvailableDatesWithDoctorResponseDTO(responseDTOList);
    }

    /*RETURNS ALL THE AVAILABLE APPOINTMENT DATES  BY DOCTORID AND SPECIALIZATIONID*/
    @Override
    public AllAvailableDatesResponseDTO fetchAvailableDates(AppointmentDatesRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, AVAILABLE_DATES_LIST);

        AllAvailableDatesResponseDTO responseDTO = new AllAvailableDatesResponseDTO();

        List<Date> avaliableDates = new ArrayList<>();

        List<DoctorDutyRosterAppointmentDate> doctorDutyRosterAppointmentDates = dutyRosterRepository
                .getDutyRosterByDoctorAndSpecializationId(requestDTO);

        doctorDutyRosterAppointmentDates.forEach(doctorDutyRosterAppointmentDate -> {

            List<String> weekDays = getWeekdays(doctorDutyRosterAppointmentDate.getId());

            List<Date> rosterDates = getDates(doctorDutyRosterAppointmentDate.getFromDate(),
                    doctorDutyRosterAppointmentDate.getToDate());

            List<Date> availableDates = getDutyRosterDates(rosterDates, weekDays);

            if (doctorDutyRosterAppointmentDate.getHasOverride().equals('Y')) {

                List<DoctorDutyRosterOverrideAppointmentDate> overrideAppointmentDates =
                        getDateAndTimeFromOverrideByRosterId(doctorDutyRosterAppointmentDate.getId());

                overrideAppointmentDates.forEach(date -> {
                    List<Date> dayOffDates = getDatesBetween(date.getFromDate(), date.getToDate());
                    if (dayOffDates.size() != 0) {
                        getAllDate(avaliableDates, mergeRosterAndRosterOverrideDates(availableDates, dayOffDates));
                    } else {
                        getAllDate(avaliableDates, availableDates);
                    }
                });
            } else {
                getAllDate(avaliableDates, availableDates);
            }
        });
        responseDTO.setAvaliableDates(avaliableDates);

        responseDTO.setResponseStatus(OK);

        responseDTO.setResponseCode(OK.value());

        if (ObjectUtils.isEmpty(responseDTO.getAvaliableDates())) {
            appointmentNotAvailableError();
            throw APPOINTMENT_NOT_AVAILABLE.get();
        }

        log.info(FETCHING_PROCESS_COMPLETED, AVAILABLE_DATES_LIST, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }


    private List<AvailableDatesResponseDTO> getDutyRosterDatesAndTime(List<Date> dates,
                                                                      List<DoctorWeekDaysDutyRosterAppointmentDate>
                                                                              weekDaysDutyRosterAppointmentDate) {

        List<AvailableDatesResponseDTO> availableDates = new ArrayList<>();

        for (Date date : dates) {

            AvailableDatesResponseDTO datesResponseDTO = new AvailableDatesResponseDTO();

            weekDaysDutyRosterAppointmentDate.forEach(weekdays -> {

                getAllDutyRosterDatesAndTime(date, weekdays, datesResponseDTO, availableDates);

            });
        }
        return availableDates;
    }

    private List<DoctorDutyRosterOverrideAppointmentDate> getDateAndTimeFromOverrideByRosterId(Long rosterId) {
        return dutyRosterOverrideRepository.getRosterOverrideByRosterId(rosterId);
    }

    private List<DoctorWeekDaysDutyRosterAppointmentDate> getWeekdaysTimeByRosterId(Long dutyRosterId) {
        return dutyRosterRepository.getWeekDaysDutyRosterDataByDutyRosterId(dutyRosterId);
    }

    private List<String> getWeekdays(Long dutyRosterId) {
        return dutyRosterRepository.getWeekDaysDutyRosterByDutyRosterId(dutyRosterId);
    }

    private void checkIfOverrideExists(DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate,
                                       List<AvailableDatesResponseDTO> appointmentDatesResponseDTO,
                                       List<AvailableDatesResponseDTO> apoointmentDateAndTime) {

        if (doctorDutyRosterAppointmentDate.getHasOverride().equals('Y')) {

            List<AvailableDatesResponseDTO> availableDatesResponseDTOS =
                    getOverrideDatesAndTime(doctorDutyRosterAppointmentDate);

            List<AvailableDatesResponseDTO> datesResponseDTO = mergeRosterAndRosterOverrideDatesAndTime(
                    appointmentDatesResponseDTO,
                    availableDatesResponseDTOS);

            getAllDateAndTime(apoointmentDateAndTime, datesResponseDTO);

        } else {

            getAllDateAndTime(apoointmentDateAndTime, appointmentDatesResponseDTO);

        }
    }

    private List<AvailableDatesResponseDTO> getOverrideDatesAndTime(
            DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate) {

        final List<Date> dates = new ArrayList<>();

        List<AvailableDatesResponseDTO> avaliableDates = new ArrayList<>();

        List<DoctorDutyRosterOverrideAppointmentDate> appointmentDatesAndTime =
                getDateAndTimeFromOverrideByRosterId(doctorDutyRosterAppointmentDate.getId());

        appointmentDatesAndTime.forEach(appointmentDate -> {

            getAllOverrideDates(appointmentDate, dates);

            for (Date date : dates) {

                AvailableDatesResponseDTO datesResponseDTO = new AvailableDatesResponseDTO();

                datesResponseDTO.setDate(utilDateToSqlDate(date));

                checkIfDayOff(appointmentDate, datesResponseDTO, avaliableDates);
            }
        });
        return avaliableDates;
    }

    private void validateIfRequestedDateIsBeforeCurrentDate(Date requestedDate) {
        boolean isRequestedDateBeforeCurrentDate = isFirstDateGreater(new Date(), requestedDate);

        if (isRequestedDateBeforeCurrentDate) {
            doctorNotAvailableError();
            throw DOCTORS_NOT_AVAILABLE.get();
        }
    }

    private Supplier<NoContentFoundException> DOCTORS_NOT_AVAILABLE = () -> new NoContentFoundException(Doctor.class);

    private Supplier<NoContentFoundException> APPOINTMENT_NOT_AVAILABLE = () -> new NoContentFoundException(Appointment.class);

    private List<Date> getDatesBetween(Date fromDate, Date toDate) {
        return utilDateListToSqlDateList(getDates(fromDate,
                toDate));
    }

    private void appointmentNotAvailableError() {
        log.error(CONTENT_NOT_FOUND, APPOINTMENT);
    }

    private void doctorNotAvailableError() {
        log.error(CONTENT_NOT_FOUND, DOCTOR);
    }


    private void checkEmpty(List list){
        if (ObjectUtils.isEmpty(list)) {
            appointmentNotAvailableError();
            throw APPOINTMENT_NOT_AVAILABLE.get();
        }
    }
}
