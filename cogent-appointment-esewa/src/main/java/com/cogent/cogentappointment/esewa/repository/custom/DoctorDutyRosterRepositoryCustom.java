package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.request.appointment.eSewa.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.eSewa.AvailableDoctorRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.DoctorWeekDaysDutyRosterAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.AvailableDoctorWithSpecialization;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.DoctorAvailabilityStatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.DutyRosterAppointmentDateAndDoctorDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.DutyRosterAppointmentDateAndSpecilizationDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 26/11/2019
 */
@Repository
@Qualifier("doctorDutyRosterRepositoryCustom")
public interface DoctorDutyRosterRepositoryCustom {

    DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterTime(Date date,
                                                              Long doctorId,
                                                              Long specializationId);


    List<DoctorDutyRosterAppointmentDate> getDutyRosterByDoctorAndSpecializationId(AppointmentDatesRequestDTO requestDTO);

    List<DoctorWeekDaysDutyRosterAppointmentDate> getWeekDaysDutyRosterDataByDutyRosterId(Long doctorDutyRosterId);

    List<String> getWeekDaysDutyRosterByDutyRosterId(Long doctorDutyRosterId);

    DoctorAvailabilityStatusResponseDTO fetchDoctorDutyRosterStatus(AppointmentDetailRequestDTO requestDTO);

    List<AvailableDoctorWithSpecialization> fetchAvailableDoctor(AppointmentDetailRequestDTO requestDTO);

    List<DutyRosterAppointmentDateAndSpecilizationDTO> getAvailableDatesAndSpecilizationByDoctorId(Long doctorId);

    List<DutyRosterAppointmentDateAndDoctorDTO> getAvailableDatesAndDoctorBySpecilizationId(Long specilizationId);

    List<AvailableDoctorWithSpecialization> fetchAvailableDoctor(AvailableDoctorRequestDTO requestDTO);
}
