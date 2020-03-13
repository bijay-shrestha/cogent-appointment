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
        DoctorDutyRosterAppointmentDate doctorDutyRosterAppointmentDate = dutyRosterRepository
                .getDutyRosterByDoctorAndSpecializationId(requestDTO);

        if (doctorDutyRosterAppointmentDate.getHasOverride().equals('Y')) {
            DoctorDutyRosterOverrideAppointmentDate dutyRosterOverrideAppointmentDate = dutyRosterOverrideRepository
                    .getRosterOverrideByRosterId(doctorDutyRosterAppointmentDate.getId());
        }

        List<DoctorWeekDaysDutyRosterAppointmentDate> weekDaysDutyRosterAppointmentDate = dutyRosterRepository
                .getWeekDaysDutyRosterByDutyRosterId(doctorDutyRosterAppointmentDate.getId());

        List<Date> dates = getDatesBetween(doctorDutyRosterAppointmentDate.getFromDate(),
                doctorDutyRosterAppointmentDate.getToDate());

        System.out.println(dates);

        matachAndListDates(requestDTO,dates,weekDaysDutyRosterAppointmentDate);
        return null;
    }

    private AppointmentDatesResponseDTO matachAndListDates(AppointmentDatesRequestDTO requestDTO,
             List<Date> dates,
             List<DoctorWeekDaysDutyRosterAppointmentDate> weekDaysDutyRosterAppointmentDate) {

        AppointmentDatesResponseDTO appointmentDatesResponseDTO = new AppointmentDatesResponseDTO();
        appointmentDatesResponseDTO.setDoctorId(requestDTO.getDoctorId());
        appointmentDatesResponseDTO.setSpecializationId(requestDTO.getSpecializationId());

        List<AvaliableDatesResponseDTO> avaliableDates = new ArrayList<>();


      for (Date date:dates){
                  AvaliableDatesResponseDTO datesResponseDTO=new AvaliableDatesResponseDTO();
          weekDaysDutyRosterAppointmentDate.forEach(weekdays -> {
              if(date.toString().substring(0,3).toUpperCase().equals(weekdays.getWeekDay())){
                  datesResponseDTO.setDate(utilDateToSqlDate(date));
                  datesResponseDTO.setDoctorAvailableTime(weekdays.getStartTime()+"-"+weekdays.getEndTime());
                  avaliableDates.add(datesResponseDTO);
              }
          });
          appointmentDatesResponseDTO.setDates(avaliableDates);
      }

        System.out.println(appointmentDatesResponseDTO);
        return appointmentDatesResponseDTO;
    }
}

