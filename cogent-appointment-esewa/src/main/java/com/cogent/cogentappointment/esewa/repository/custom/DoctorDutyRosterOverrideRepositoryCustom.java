package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.request.appointment.eSewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterOverrideAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.AvailableDoctorWithSpecialization;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.DoctorAvailabilityStatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentDetails.DutyRosterOverrideAppointmentDate;
import com.cogent.cogentappointment.esewa.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Qualifier("doctorDutyRosterOverrideRepositoryCustom")
public interface DoctorDutyRosterOverrideRepositoryCustom {

    DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterOverrideTime(Date date,
                                                                      Long doctorId,
                                                                      Long specializationId);


    List<DoctorDutyRosterOverrideAppointmentDate> getRosterOverrideByRosterId(Long doctorDutyRosterId);

    DoctorAvailabilityStatusResponseDTO fetchDoctorDutyRosterOverrideStatus(AppointmentDetailRequestDTO requestDTO);

    List<DutyRosterOverrideAppointmentDate> fetchDayOffRosterOverridebyRosterId(Long rosterId);

    List<AvailableDoctorWithSpecialization> fetchAvailableDoctor(AppointmentDetailRequestDTO requestDTO);

}
