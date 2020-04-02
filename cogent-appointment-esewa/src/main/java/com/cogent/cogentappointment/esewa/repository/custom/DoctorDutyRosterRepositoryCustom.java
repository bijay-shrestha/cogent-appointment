package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.request.appointment.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.doctorDutyRoster.DoctorDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.doctorDutyRoster.DoctorExistingDutyRosterRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.DoctorWeekDaysDutyRosterAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.*;
import com.cogent.cogentappointment.esewa.dto.response.eSewa.AvailableDoctorResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.eSewa.DoctorAvailabilityStatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.eSewa.DutyRosterAppointmentDateAndDoctorDTO;
import com.cogent.cogentappointment.esewa.dto.response.eSewa.DutyRosterAppointmentDateAndSpecilizationDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 26/11/2019
 */
@Repository
@Qualifier("doctorDutyRosterRepositoryCustom")
public interface DoctorDutyRosterRepositoryCustom {

    Long validateDoctorDutyRosterCount(Long doctorId,
                                       Long specializationId,
                                       Date fromDate,
                                       Date toDate);

    List<DoctorDutyRosterMinimalResponseDTO> search(DoctorDutyRosterSearchRequestDTO searchRequestDTO,
                                                    Pageable pageable,
                                                    Long hospitalId);

    DoctorDutyRosterDetailResponseDTO fetchDetailsById(Long id, Long hospitalId);

    DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterTime(Date date,
                                                              Long doctorId,
                                                              Long specializationId);

    List<DoctorExistingDutyRosterResponseDTO> fetchExistingDoctorDutyRosters(
            DoctorExistingDutyRosterRequestDTO requestDTO, Long hospitalId);

    DoctorExistingDutyRosterDetailResponseDTO fetchExistingRosterDetails(Long doctorDutyRosterId);

    List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus
            (AppointmentStatusRequestDTO requestDTO,
             Long hospitalId);

    List<DoctorDutyRosterAppointmentDate> getDutyRosterByDoctorAndSpecializationId(AppointmentDatesRequestDTO requestDTO);

    List<DoctorWeekDaysDutyRosterAppointmentDate> getWeekDaysDutyRosterDataByDutyRosterId(Long doctorDutyRosterId);

    List<String> getWeekDaysDutyRosterByDutyRosterId(Long doctorDutyRosterId);

    DoctorAvailabilityStatusResponseDTO fetchDoctorDutyRosterStatus(AppointmentDetailRequestDTO requestDTO);

    List<AvailableDoctorResponseDTO> fetchAvailableDoctor(AppointmentDetailRequestDTO requestDTO);

    List<DutyRosterAppointmentDateAndSpecilizationDTO> getAvaliableDatesAndSpecilizationByDoctorId(Long doctorId);

    List<DutyRosterAppointmentDateAndDoctorDTO> getAvaliableDatesAndDoctorBySpecilizationId(Long specilizationId);
}
