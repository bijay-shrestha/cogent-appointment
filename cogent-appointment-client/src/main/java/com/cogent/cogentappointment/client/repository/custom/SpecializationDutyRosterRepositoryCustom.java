package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentDatesRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AppointmentDetailRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.esewa.AvailableDoctorRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorExistingDutyRosterRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterAppointmentDate;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.DoctorWeekDaysDutyRosterAppointmentDate;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.*;
import com.cogent.cogentappointment.client.dto.response.eSewa.AvailableDoctorWithSpecialization;
import com.cogent.cogentappointment.client.dto.response.eSewa.DoctorAvailabilityStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.DutyRosterAppointmentDateAndDoctorDTO;
import com.cogent.cogentappointment.client.dto.response.eSewa.DutyRosterAppointmentDateAndSpecilizationDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/18/209
 */
@Repository
@Qualifier("specializationDutyRosterRepositoryCustom")
public interface SpecializationDutyRosterRepositoryCustom {

    Long validateSpecializationDutyRosterCount(Long specializationId,
                                       Date fromDate,
                                       Date toDate);
//
//    List<DoctorDutyRosterMinimalResponseDTO> search(DoctorDutyRosterSearchRequestDTO searchRequestDTO,
//                                                    Pageable pageable,
//                                                    Long hospitalId);
//
//    DoctorDutyRosterDetailResponseDTO fetchDetailsById(Long id, Long hospitalId);
//
//    DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterTime(Date date,
//                                                              Long specializationId);
//
//    List<DoctorExistingDutyRosterResponseDTO> fetchExistingDoctorDutyRosters(
//            DoctorExistingDutyRosterRequestDTO requestDTO, Long hospitalId);
//
//    DoctorExistingDutyRosterDetailResponseDTO fetchExistingRosterDetails(Long specializationDutyRosterId);
//
//    List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus
//            (AppointmentStatusRequestDTO requestDTO,
//             Long hospitalId);
//
//    List<DoctorDutyRosterAppointmentDate> getDutyRosterByDoctorAndSpecializationId(AppointmentDatesRequestDTO requestDTO);
//
//    List<DoctorWeekDaysDutyRosterAppointmentDate> getWeekDaysDutyRosterDataByDutyRosterId(Long specializationDutyRosterId);
//
//    List<String> getWeekDaysDutyRosterByDutyRosterId(Long specializationDutyRosterId);

}
