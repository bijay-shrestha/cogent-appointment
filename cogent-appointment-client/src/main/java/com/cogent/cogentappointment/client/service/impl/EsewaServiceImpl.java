package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.*;
import com.cogent.cogentappointment.client.repository.DoctorDutyRosterOverrideRepository;
import com.cogent.cogentappointment.client.repository.DoctorDutyRosterRepository;
import com.cogent.cogentappointment.client.service.EsewaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDatesBetween;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.client.utils.eSewaUtils.*;

@Service
@Transactional
@Slf4j
public class EsewaServiceImpl implements EsewaService {

    private final DoctorDutyRosterRepository dutyRosterRepository;

    private final DoctorDutyRosterOverrideRepository dutyRosterOverrideRepository;

    public EsewaServiceImpl(DoctorDutyRosterRepository dutyRosterRepository,
                            DoctorDutyRosterOverrideRepository dutyRosterOverrideRepository) {
        this.dutyRosterRepository = dutyRosterRepository;
        this.dutyRosterOverrideRepository = dutyRosterOverrideRepository;
    }

    @Override
    public AppointmentDatesResponseDTO doctorAvailableTime(AppointmentDatesRequestDTO requestDTO) {

        List<AvaliableDatesResponseDTO> apoointmentDateAndTime = new ArrayList<>();

        List<DoctorDutyRosterAppointmentDate> doctorDutyRosterAppointmentDates = dutyRosterRepository
                .getDutyRosterByDoctorAndSpecializationId(requestDTO);

        for (DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate : doctorDutyRosterAppointmentDates) {

            List<DoctorWeekDaysDutyRosterAppointmentDate> weekDaysDutyRosterAppointmentDate =
                    getWeekdaysTimeByRosterId(doctorDutyRosterAppointmentDate.getId());

            List<Date> dates = getDatesBetween(doctorDutyRosterAppointmentDate.getFromDate(),
                    doctorDutyRosterAppointmentDate.getToDate());

            List<AvaliableDatesResponseDTO> appointmentDatesResponseDTO =
                    getDutyRosterDatesAndTime(dates, weekDaysDutyRosterAppointmentDate);

            checkIfOverrideExists(doctorDutyRosterAppointmentDate, appointmentDatesResponseDTO, apoointmentDateAndTime);
        }
        return getFinalResponse(requestDTO, apoointmentDateAndTime);
    }

    private List<AvaliableDatesResponseDTO> getOverrideDatesAndTime(DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate) {

        final List<Date> dates = new ArrayList<>();

        List<AvaliableDatesResponseDTO> avaliableDates = new ArrayList<>();

        List<DoctorDutyRosterOverrideAppointmentDate> appointmentDatesAndTime = getDateAndTimeFromOverrideByRosterId(
                doctorDutyRosterAppointmentDate.getId());

        appointmentDatesAndTime.forEach(appointmentDate -> {

            getAllOverrideDates(appointmentDate, dates);

            for (Date date : dates) {

                AvaliableDatesResponseDTO datesResponseDTO = new AvaliableDatesResponseDTO();

                datesResponseDTO.setDate(utilDateToSqlDate(date));

                checkIfDayOff(appointmentDate, datesResponseDTO, avaliableDates);
            }
        });
        return avaliableDates;
    }

    private List<AvaliableDatesResponseDTO> getDutyRosterDatesAndTime(List<Date> dates,
                                                                      List<DoctorWeekDaysDutyRosterAppointmentDate>
                                                                              weekDaysDutyRosterAppointmentDate) {

        List<AvaliableDatesResponseDTO> avaliableDates = new ArrayList<>();

        for (Date date : dates) {

            AvaliableDatesResponseDTO datesResponseDTO = new AvaliableDatesResponseDTO();

            weekDaysDutyRosterAppointmentDate.forEach(weekdays -> {

                getAllDutyRosterDatesAndTime(date, weekdays, datesResponseDTO, avaliableDates);

            });
        }
        return avaliableDates;
    }

    private List<DoctorDutyRosterOverrideAppointmentDate> getDateAndTimeFromOverrideByRosterId(Long rosterId) {
        return dutyRosterOverrideRepository
                .getRosterOverrideByRosterId(rosterId);
    }

    private List<DoctorWeekDaysDutyRosterAppointmentDate> getWeekdaysTimeByRosterId(Long dutyRosterId) {
        return dutyRosterRepository
                .getWeekDaysDutyRosterByDutyRosterId(dutyRosterId);
    }

    private void checkIfOverrideExists(DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate,
                                       List<AvaliableDatesResponseDTO> appointmentDatesResponseDTO,
                                       List<AvaliableDatesResponseDTO> apoointmentDateAndTime) {
        if (doctorDutyRosterAppointmentDate.getHasOverride().equals('Y')) {

            List<AvaliableDatesResponseDTO> avaliableDatesResponseDTOS =
                    getOverrideDatesAndTime(doctorDutyRosterAppointmentDate);

            List<AvaliableDatesResponseDTO> datesResponseDTO = mergeRosterAndRosterOverride(
                    appointmentDatesResponseDTO,
                    avaliableDatesResponseDTOS);

            getAllDateAndTime(apoointmentDateAndTime, datesResponseDTO);

        } else {

            getAllDateAndTime(apoointmentDateAndTime, appointmentDatesResponseDTO);

        }
    }

}


